package com.xazhao.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @Description Created on 2024/03/26.
 * @Author xaZhao
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BpmnModeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 模型key
     */
    private String key;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型描述
     */
    private String description;

    /**
     * 模型版本
     */
    private String version;

    /**
     * 流程分类
     */
    private String category;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

    /**
     * 元数据信息
     */
    private String metaInfo;

    /**
     * 发布状态 已发布、未发布
     */
    private String publishStatus;

    /**
     * 发布ID
     */
    private String deploymentId;
}
