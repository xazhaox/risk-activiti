package com.xazhao.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.xazhao.common.InvokeResult;
import com.xazhao.dto.BpmnModeDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName BpmnModelService.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

public interface BpmnModelService {

    /**
     * 编辑模板
     *
     * @param modelId 模板ID
     * @return 对象节点
     */
    ObjectNode getEditorJson(String modelId);

    /**
     * 设计模型，打开Activiti流程设计器
     *
     * @param request
     * @param response
     */
    void createModel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 设计模型，通过vue路由打开Activiti流程设计器
     *
     * @param bpmnModel 模型参数
     * @return 模型ID
     */
    InvokeResult designBpmnModel(BpmnModeDTO bpmnModel);

    /**
     * 保存模型
     *
     * @param modelId     模型ID
     * @param name        模型名称
     * @param jsonXml     json xml
     * @param svgXml      bpmn xml
     * @param description 模型描述
     */
    void saveModel(String modelId, String name, String jsonXml, String svgXml, String description);

    /**
     * 查询bpmn模型
     *
     * @param pageNum   当前页
     * @param pageSize  每页显示的条目数
     * @param bpmnModel 查询条件
     * @return bpmn模型
     */
    PageInfo<BpmnModeDTO> selectBpmnModelPage(Integer pageNum, Integer pageSize, BpmnModeDTO bpmnModel);

    /**
     * zip文件部署流程模型
     *
     * @param bpmnModel zip文件
     * @param modelName 流程名称
     * @return 部署结果
     */
    InvokeResult deployBpmnModel(MultipartFile bpmnModel, String modelName);

    /**
     * 部署模型
     *
     * @param bpmnSourceId 模型定义ID
     * @return 部署结果
     */
    InvokeResult deployBpmnRepository(String bpmnSourceId);

    /**
     * 导出
     *
     * @param bpmnModel 模型数据
     * @param response  响应数据
     * @return 导出结果
     */
    InvokeResult exportBpmnModel(BpmnModeDTO bpmnModel, HttpServletResponse response);

    /**
     * 删除模型
     *
     * @param bpmnId 模型ID
     * @return 删除结果
     */
    InvokeResult deleteBpmnModel(String bpmnId);
}
