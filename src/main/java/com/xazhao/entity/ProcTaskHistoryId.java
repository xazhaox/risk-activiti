package com.xazhao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ActProcTaskHistoryId.java
 * @Author AnZhaoxu
 * @Create 2024.03.04
 * @UpdateUser
 * @UpdateDate 2024.03.04
 * @Version 2024.0.1
 * @Description
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
