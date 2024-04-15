package com.xazhao.service;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * @ClassName ProcessInstanceService.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

public interface ProcessInstanceService {

    /**
     * 启动流程实例
     *
     * @param processKey 流程定义Key
     * @return 流程实例
     */
    ProcessInstance startProcessInstance(String processKey);

    /**
     * 启动流程实例
     *
     * @param processKey  流程定义Key
     * @param businessKey 业务唯一Key
     * @return 流程实例
     */
    ProcessInstance startProcessInstance(String processKey, String businessKey);

    /**
     * 启动流程实例
     *
     * @param processKey 流程定义Key
     * @param variables  需要传递的变量
     * @return 流程实例
     */
    ProcessInstance startProcessInstance(String processKey, Map<String, Object> variables);

    /**
     * 启动流程实例
     *
     * @param processKey  流程定义Key
     * @param businessKey 业务唯一Key
     * @param variables   需要传递的变量
     * @return 流程实例
     */
    ProcessInstance startProcessInstance(String processKey, String businessKey, Map<String, Object> variables);
}
