package com.xazhao.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xazhao.common.CurrentUser;
import com.xazhao.common.InvokeResult;
import com.xazhao.constant.Message;
import com.xazhao.entity.HistoricalTrack;
import com.xazhao.entity.ProcTask;
import com.xazhao.entity.ProcTaskHistory;
import com.xazhao.entity.user.UserInfo;
import com.xazhao.exception.ServiceException;
import com.xazhao.mapper.HistoricTrackMapper;
import com.xazhao.mapper.ProcTaskHistoryMapper;
import com.xazhao.mapper.ProcTaskMapper;
import com.xazhao.service.SignTaskService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TaskServiceImpl.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

@Service
public class SignTaskServiceImpl implements SignTaskService {

    /**
     * 任务转办
     */
    public static final String ASSIGNEE_TASK = "Transfer";

    /**
     * 会签
     */
    public static final String SIGN = "Sign";

    /**
     * 或签
     */
    public static final String OR_SIGNED = "OrSigned";

    /**
     * 流程委派标识
     */
    public static final String PENDING = "PENDING";

    /**
     * 加签任务未完成，在进行中
     */
    public static final String HAVE = "Have";

    /**
     * 加签任务已完成
     */
    public static final String FINISH = "Finish";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");

    @Resource
    private TaskService taskService;

    @Resource
    private ProcTaskHistoryMapper taskHistoryMapper;

    @Resource
    private ProcTaskMapper procTaskMapper;

    @Resource
    private HistoricTrackMapper historicTrackMapper;

    /**
     * 任务转办
     *
     * @param procInstId 任务ID
     * @param userCode   接受任务用户ID
     * @return 转办结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvokeResult assigneeTask(String procInstId, String userCode) {
        try {
            UserInfo userInfo = CurrentUser.getCurrentUser();
            if (!ObjectUtil.isNotEmpty(userInfo)) {
                return InvokeResult.failure(Message.SIGN_BACK_IN);
            }
            String currentUserCode = userInfo.getUserCode();
            // 转办时若是或签任务，参与或签的应该人应该都可以处理任务
            setOrSignProcAssignee(userInfo, procInstId);
            Task task = taskService.createTaskQuery()
                    .processInstanceId(procInstId).taskCandidateOrAssigned(currentUserCode).singleResult();
            if (ObjectUtil.isNotEmpty(task)) {
                // 当前任务若是加签任务需要将审批链用户修改
                String taskId = task.getId();
                if (ObjectUtil.isNotEmpty(task.getDelegationState()) && PENDING.equals(task.getDelegationState().name())) {
                    ProcTask procTask = procTaskMapper.selectDelegateTask(taskId, procInstId, null);
                    procTask.setApprovalChain(procTask.getApprovalChain().replace(currentUserCode, userCode));
                    procTask.setCurrentTaskUser(userCode);
                    procTaskMapper.updateTaskApprovalChain(procTask);
                }
                taskService.setAssignee(taskId, userCode);
                taskService.setOwner(taskId, userCode);
                taskService.addComment(taskId, task.getProcessInstanceId(),
                        "<" + userInfo.getUserName() + "> 转办任务给 <" + userCode + ">");
                // 记录流程信息
                String procDefinitionName = saveHistoryTask(task, ASSIGNEE_TASK, "发起任务转办");
                ProcTaskHistory processTaskHistory = ProcTaskHistory.builder()
                        .procTaskId(0L)
                        .taskModule(procDefinitionName)
                        .taskNode(task.getTaskDefinitionKey())
                        .taskNodeId(taskId)
                        .approvalChain(userCode)
                        .taskType(ASSIGNEE_TASK)
                        .currentTaskUser(userCode)
                        .procInsId(procInstId)
                        .taskId("0")
                        .operatorCode(currentUserCode)
                        .build();
                taskHistoryMapper.insertTaskHistory(processTaskHistory);
                return InvokeResult.success(null, Message.TRANSFER_SUCCESS);
            } else {
                return InvokeResult.failure(Message.IS_NOT_OPERATOR);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new ServiceException(Message.ACT_TRANSFER_EXCEPTION);
        }
    }

    /**
     * 任务加签
     *
     * @param procInstId  流程实例ID
     * @param userCodeArr 接受任务用户
     * @param signType    加签类型 会签（Sign）/或签（OrSigned）
     * @return 加签结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvokeResult delegateTask(String procInstId, String[] userCodeArr, String signType) {
        try {
            UserInfo userInfo = CurrentUser.getCurrentUser();
            if (!ObjectUtil.isNotEmpty(userInfo)) {
                return InvokeResult.failure(Message.SIGN_BACK_IN);
            }
            Task task = taskService.createTaskQuery()
                    .processInstanceId(procInstId).taskCandidateOrAssigned(userInfo.getUserCode()).singleResult();
            if (ObjectUtil.isNotEmpty(task)) {
                if (ObjectUtil.isNotEmpty(task.getDelegationState()) && PENDING.equals(task.getDelegationState().name())) {
                    return InvokeResult.failure(Message.COMPLETE_CURRENT_DELEGATE_TASK);
                }
                TaskEntity newTask = createNewTask(task);
                taskService.saveTask(newTask);
                String newTaskId = newTask.getId();
                taskService.addComment(newTaskId, task.getProcessInstanceId(),
                        "<" + userInfo.getUserName() + "> 委派任务给 <" + userCodeArr[0] + ">");
                // 委派任务，默认先委派给第一个用户
                taskService.delegateTask(task.getId(), userCodeArr[0]);
                taskService.complete(newTaskId);
                // 保存审批记录
                saveHistoryTask(task, null, "发起流程加签");
                String approvalUser = String.join(",", userCodeArr);
                insertActProcTask(task, SIGN.equals(signType) ? SIGN : OR_SIGNED, approvalUser, userCodeArr[0], newTaskId);
                return InvokeResult.success(null, Message.SIGN_FINISH);
            } else {
                return InvokeResult.failure(Message.IS_NOT_OPERATOR);
            }
        } catch (Exception e) {
            // 抛出自定义异常，手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new ServiceException(Message.ACT_EXCEPTION);
        }
    }

    /**
     * 设置或签流程持有人
     *
     * @param currentUser 当前登录用户
     * @param procInstId  流程实例ID
     */
    public void setOrSignProcAssignee(UserInfo currentUser, String procInstId) {
        try {
            Task taskOrSign = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
            String userCode = currentUser.getUserCode();
            if (ObjectUtil.isNotEmpty(taskOrSign.getDelegationState())
                    && PENDING.equals(taskOrSign.getDelegationState().name())) {
                String taskOrSignId = taskOrSign.getId();
                // 有可能出现会签，只需要查询或签任务
                ProcTask procTask = procTaskMapper.selectDelegateTask(taskOrSignId, procInstId, OR_SIGNED);
                if (!ObjectUtil.isEmpty(procTask)) {
                    String[] approvalChain = procTask.getApprovalChain().split(",");
                    if (ArrayUtil.contains(approvalChain, userCode) && !procTask.getCurrentTaskUser().equals(userCode)) {
                        // 保证同一时刻只能设置一个用户
                        synchronized (this) {
                            taskService.setAssignee(taskOrSignId, userCode);
                            taskService.setOwner(taskOrSignId, userCode);
                            // 设置流程持有人
                            procTask.setCurrentTaskUser(userCode);
                            procTaskMapper.updateTaskApprovalChain(procTask);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存加签/转办流程轨迹记录
     *
     * @param task     当前任务
     * @param signType 任务类型，加签/转办
     * @param opinion  审批意见
     */
    public String saveHistoryTask(Task task, String signType, String opinion) {
        UserInfo user = CurrentUser.getCurrentUser();
        String procDefinitionName = ProcessEngines.getDefaultProcessEngine()
                .getRepositoryService().getProcessDefinition(task.getProcessDefinitionId()).getName();
        String assigneeText = ASSIGNEE_TASK.equals(signType) ? "任务转办" : "流程加签";
        HistoricalTrack historicalTrack = HistoricalTrack.builder()
                .procInsId(task.getProcessInstanceId())
                .procDefId(procDefinitionName)
                .userCode(user.getUserCode())
                .userName(user.getUserName())
                .time(SIMPLE_DATE_FORMAT.format(new Date()))
                .tagName(task.getName() + assigneeText)
                .tagStatus(assigneeText)
                .opinion(opinion)
                .build();
        historicTrackMapper.insert(historicalTrack);
        return procDefinitionName;
    }

    /**
     * 保存加签/转办记录
     *
     * @param task         当前任务
     * @param signType     任务类型，加签/转办
     * @param approvalUser 审批链
     * @param userCode     接收任务用户
     * @param taskId       流程会签/或签时生成的任务ID
     */
    public void insertActProcTask(Task task, String signType, String approvalUser, String userCode, String taskId) {
        UserInfo user = CurrentUser.getCurrentUser();
        String processDefinitionName = ProcessEngines.getDefaultProcessEngine()
                .getRepositoryService().getProcessDefinition(task.getProcessDefinitionId()).getName();
        // 记录历史流程信息
        ProcTask procTask = ProcTask.builder()
                .taskModule(processDefinitionName)
                .taskNode(task.getTaskDefinitionKey())
                .taskNodeId(task.getId())
                .approvalChain(approvalUser)
                .taskType(signType)
                .currentTaskUser(userCode)
                .procInsId(task.getProcessInstanceId())
                .taskId(taskId)
                .operatorCode(user.getUserCode())
                .isFinishTask(HAVE)
                .build();
        procTaskMapper.insertProcTask(procTask);
    }

    /**
     * 创建新流程任务
     *
     * @param currentTask 当前流程任务
     * @return 新流程任务
     */
    public TaskEntity createNewTask(Task currentTask) {
        TaskEntity task = (TaskEntity) taskService.newTask();
        task.setCategory(currentTask.getCategory());
        task.setDescription(currentTask.getDescription());
        task.setTenantId(currentTask.getTenantId());
        task.setAssignee(currentTask.getAssignee());
        task.setName(currentTask.getName());
        task.setProcessDefinitionId(currentTask.getProcessDefinitionId());
        task.setProcessInstanceId(currentTask.getProcessInstanceId());
        task.setTaskDefinitionKey(currentTask.getTaskDefinitionKey());
        task.setPriority(currentTask.getPriority());
        task.setCreateTime(new Date());
        return task;
    }
}
