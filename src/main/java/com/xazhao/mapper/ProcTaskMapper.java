package com.xazhao.mapper;

import com.xazhao.entity.ProcTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @ClassName ActProcTaskMapper.java
 * @Author An
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Description
 */

@Mapper
public interface ProcTaskMapper extends BaseMapper<ProcTask> {

    /**
     * 保存任务信息
     *
     * @param procTask 任务信息
     */
    void insertProcTask(@Param("task") ProcTask procTask);

    /**
     * 查询加签任务
     *
     * @param taskNodeId 任务ID
     * @param procInstId 流程实例ID
     * @param signType   任务类型
     * @return 加签任务
     */
    ProcTask selectDelegateTask(
            @Param("taskNodeId") String taskNodeId, @Param("procInstId") String procInstId, @Param("signType") String signType);

    /**
     * 修改加签任务为完成状态
     *
     * @param procTask 任务记录
     */
    void updateTaskFinishStatus(@Param("task") ProcTask procTask);

    /**
     * 修改加签任务信息
     *
     * @param procTask 任务信息
     */
    void updateTaskApprovalChain(@Param("actTask") ProcTask procTask);

    /**
     * 查询当前用户参与的所有或签任务
     *
     * @param userCode 用户代码
     * @return 或签任务数据集
     */
    List<String> selectDelegateRunTask(@Param("userCode") String userCode);
}

