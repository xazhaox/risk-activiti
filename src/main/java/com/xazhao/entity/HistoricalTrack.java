package com.xazhao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ClassName HistoricalTrack.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

@Entity
@Data
@Table(name = "com_his_track")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class HistoricalTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 流程实例id
     */
    @Column(name = "proc_ins_id")
    private String procInsId;

    /**
     * 操作人代码
     */
    @Column(name = "user_code")
    private String userCode;

    /**
     * 操作时间
     */
    @Column(name = "time")
    private String time;

    /**
     * 流程节点名称
     */
    @Column(name = "tag_name")
    private String tagName;

    /**
     * 审批状态
     */
    @Column(name = "tag_status")
    private String tagStatus;

    /**
     * 操作人姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 流程定义id
     */
    @Column(name = "proc_def_id")
    private String procDefId;

    /**
     * 备注
     */
    @Column(name = "temp")
    private String temp;

    /**
     * 审批内容 或 审批意见
     */
    @Column(name = "opinion")
    private String opinion;
}
