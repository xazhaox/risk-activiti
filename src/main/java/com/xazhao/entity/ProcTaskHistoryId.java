package com.xazhao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description Created on 2024/03/04.
 * @Author xaZhao
 */

@Data
public class ProcTaskHistoryId implements Serializable {

    private static final long serialVersionUID = 752725726893345134L;

    /**
     * ID
     */
    private Long id;

    /**
     * process_task表主键
     */
    private Long procTaskId;
}
