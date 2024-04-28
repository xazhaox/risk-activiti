package com.xazhao.mapper;

import com.xazhao.entity.ProcTaskHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

@Mapper
public interface ProcTaskHistoryMapper extends BaseMapper<ProcTaskHistory> {

    /**
     * 保存历史任务信息
     *
     * @param processTaskHistory 历史任务信息
     */
    void insertTaskHistory(@Param("taskHis") ProcTaskHistory processTaskHistory);
}

