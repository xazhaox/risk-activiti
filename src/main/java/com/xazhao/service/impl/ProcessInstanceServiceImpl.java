package com.xazhao.service.impl;

import com.xazhao.constant.Message;
import com.xazhao.exception.ServiceException;
import com.xazhao.service.ProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @ClassName ProcessInstanceServiceImpl.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;


    /**
     * 启动流程实例
     *
     * @param processKey 流程定义Key
     * @return 流程实例
     */
    @Override
    public ProcessInstance startProcessInstance(String processKey) {
        if (!isNotBlank(processKey)) {
            throw new ServiceException(Message.PROCESS_KEY_ISBLANK);
        }
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 启动流程实例
     *
     * @param processKey  流程定义Key
     * @param businessKey 业务唯一Key
     * @return 流程实例
     */
    @Override
    public ProcessInstance startProcessInstance(String processKey, String businessKey) {
        if (!isNotBlank(processKey) && isNotBlank(businessKey)) {
            throw new ServiceException(Message.PROCESS_KEY_AND_BUSINESS_KEY_ISBLANK);
        }
        return runtimeService.startProcessInstanceByKey(processKey, businessKey);
    }

    /**
     * 启动流程实例
     *
     * @param processKey 流程定义Key
     * @param variables  需要传递的变量
     * @return 流程实例
     */
    @Override
    public ProcessInstance startProcessInstance(String processKey, Map<String, Object> variables) {
        if (!isNotBlank(processKey)) {
            throw new ServiceException(Message.PROCESS_KEY_ISBLANK);
        }
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 启动流程实例
     *
     * @param processKey  流程定义Key
     * @param businessKey 业务唯一Key
     * @param variables   需要传递的变量
     * @return 流程实例
     */
    @Override
    public ProcessInstance startProcessInstance(String processKey, String businessKey, Map<String, Object> variables) {
        if (!isNotBlank(processKey) && isNotBlank(businessKey)) {
            throw new ServiceException(Message.PROCESS_KEY_AND_BUSINESS_KEY_ISBLANK);
        }
        variables.put("businessKey", businessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        // 设置启动人
        Authentication.setAuthenticatedUserId("Mock current login user");
        // 将流程定义名称作为流程实例名称
        runtimeService.setProcessInstanceName(
                processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionName());
        return processInstance;
    }
}
