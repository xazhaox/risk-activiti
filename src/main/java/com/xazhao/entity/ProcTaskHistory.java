package com.xazhao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 流程任务信息记录表
 *
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProcTaskHistoryId.class)
@Entity
@Table(name = "act_proc_task_history")
public class ProcTaskHistory implements Serializable {

    private static final long serialVersionUID = 854677004008827650L;

    @Id
    @Column(name = "id")
    private Long id;

    /**
     * proc_task表主键
     */
    @Id
    @Column(name = "proc_task_id")
    private Long procTaskId;

    /**
     * 流程所属模块
     */
    @Column(name = "task_module")
    private String taskModule;

    /**
     * 发起会签/或签任务节点
     */
    @Column(name = "task_node")
    private String taskNode;

    /**
     * 发起会签/或签/转办任务节点ID
     */
    @Column(name = "task_node_id")
    private String taskNodeId;

    /**
     * 流程审批链
     */
    @Column(name = "approval_chain")
    private String approvalChain;

    /**
     * 任务类型：会签（Sign），或签（OrSigned），转办（Transfer）
     */
    @Column(name = "task_type")
    private String taskType;

    /**
     * 当前任务节点负责人
     */
    @Column(name = "current_task_user")
    private String currentTaskUser;

    /**
     * 工作流当前任务的流程实例ID
     */
    @Column(name = "proc_ins_id")
    private String procInsId;

    /**
     * 流程会签/或签时生成的任务ID，转办为0
     */
    @Column(name = "task_id")
    private String taskId;

    /**
     * 发起加签/转办用户
     */
    @Column(name = "operator_code")
    private String operatorCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

