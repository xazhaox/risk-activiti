package com.xazhao.activiti;

import cn.hutool.core.util.IdUtil;
import com.xazhao.service.ProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProcessInstanceTest.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessInstanceTest {

    @Resource
    private ProcessInstanceService processInstanceService;

    @Resource
    private TaskService taskService;

    private static final String[] TASK_USER = new String[]{"risk", "bg1", "shanxi1"};


    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceTest() {
        String businessKey = IdUtil.getSnowflake().nextIdStr();
        Map<String, Object> variables = new HashMap<>();
        ProcessInstance processInstance = processInstanceService.startProcessInstance(
                "RCSAJob", businessKey, variables);
        // 设置用户
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        for (String taskUser : TASK_USER) {
            taskService.addCandidateUser(task.getId(), taskUser);
        }
    }

    /**
     * 启动流程实例，设置会签节点
     * 通过定义流程模型设置并行/串行会签节点
     */
    @Test
    public void startSignProcessInstanceTest() {
        String businessKey = IdUtil.getSnowflake().nextIdStr();
        Map<String, Object> variables = new HashMap<>();
        List<String> taskUserList = new ArrayList<>();
        taskUserList.add("risk");
        taskUserList.add("bg1");
        taskUserList.add("shanxi1");
        variables.put("assigneeList", taskUserList);
        ProcessInstance processInstance = processInstanceService.startProcessInstance("Sign", businessKey, variables);
        log.info("流程开启成功，流程ID为：{}", processInstance.getProcessInstanceId());
    }

}
