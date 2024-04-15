package com.xazhao.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.xazhao.common.InvokeResult;
import com.xazhao.dto.BpmnModeDTO;
import com.xazhao.service.BpmnModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName BpmnModelController.java
 * @Author AnZhaoxu
 * @Create 2024.03.26
 * @UpdateUser
 * @UpdateDate 2024.03.26
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/bpmn/model")
public class BpmnModelController {

    @Resource
    private BpmnModelService bpmnModelService;

    /**
     * 编辑模板
     *
     * @param modelId 模板ID
     * @return 对象节点
     */
    @GetMapping(value = "/{modelId}/json", produces = "application/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) {

        return bpmnModelService.getEditorJson(modelId);
    }

    /**
     * 设计模型，直接通过url打开Activiti流程设计器
     *
     * @param request
     * @param response
     */
    @GetMapping("/createModel")
    public void createModel(HttpServletRequest request, HttpServletResponse response) {

        bpmnModelService.createModel(request, response);
    }

    /**
     * 设计模型，通过vue路由打开Activiti流程设计器
     *
     * @param bpmnModel 模型参数
     * @return 模型ID
     */
    @PostMapping(value = "/designModel")
    public InvokeResult designBpmnModel(@RequestBody BpmnModeDTO bpmnModel) {

        return bpmnModelService.designBpmnModel(bpmnModel);
    }

    /**
     * 保存模型
     *
     * @param modelId     模型ID
     * @param name        模型名称
     * @param jsonXml     json xml
     * @param svgXml      bpmn xml
     * @param description 模型描述
     */
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/{modelId}/save")
    public void saveModel(@PathVariable String modelId,
                          @RequestParam("name") String name,
                          @RequestParam("json_xml") String jsonXml,
                          @RequestParam("svg_xml") String svgXml,
                          @RequestParam("description") String description) {

        bpmnModelService.saveModel(modelId, name, jsonXml, svgXml, description);
    }

    /**
     * 查询bpmn模型
     *
     * @param pageNum   当前页
     * @param pageSize  每页显示的条目数
     * @param bpmnModel 查询条件
     * @return bpmn模型
     */
    @GetMapping(value = "/queryPage")
    public PageInfo<BpmnModeDTO> selectBpmnModelPage(
            @RequestParam Integer pageNum, @RequestParam Integer pageSize, BpmnModeDTO bpmnModel) {

        return bpmnModelService.selectBpmnModelPage(pageNum, pageSize, bpmnModel);
    }

    /**
     * 部署模型
     *
     * @param bpmnSourceId 模型定义ID
     * @return 部署结果
     */
    @GetMapping("/deploy")
    public InvokeResult deployBpmnRepository(@RequestParam(name = "bpmnSourceId") String bpmnSourceId) {

        return bpmnModelService.deployBpmnRepository(bpmnSourceId);
    }

    /**
     * 导出
     *
     * @param bpmnModel 模型数据
     * @param response  响应数据
     * @return 导出结果
     */
    @PostMapping(value = "/export")
    public InvokeResult exportBpmnModel(@RequestBody BpmnModeDTO bpmnModel, HttpServletResponse response) {

        return bpmnModelService.exportBpmnModel(bpmnModel, response);
    }

    /**
     * 删除模型
     *
     * @param bpmnId 模型ID
     * @return 删除结果
     */
    @PostMapping(value = "/delete/{bpmnId}")
    public InvokeResult deleteBpmnModel(@PathVariable(name = "bpmnId") String bpmnId) {

        return bpmnModelService.deleteBpmnModel(bpmnId);
    }
}
