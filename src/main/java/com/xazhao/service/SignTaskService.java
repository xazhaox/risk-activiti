package com.xazhao.service;

import com.xazhao.common.InvokeResult;

/**
 * @ClassName TaskService.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

public interface SignTaskService {

    /**
     * 任务转办
     *
     * @param procInstId 流程实例ID
     * @param userCode   接受任务用户ID
     * @return 转办结果
     */
    InvokeResult assigneeTask(String procInstId, String userCode);

    /**
     * 任务加签
     *
     * @param procInstId  流程实例ID
     * @param userCodeArr 接受任务用户
     * @param signType    加签类型 会签（Sign）/或签（OrSigned）
     * @return 加签结果
     */
    InvokeResult delegateTask(String procInstId, String[] userCodeArr, String signType);
}
