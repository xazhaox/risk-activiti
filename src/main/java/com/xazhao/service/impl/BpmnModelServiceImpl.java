package com.xazhao.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.xazhao.common.InvokeResult;
import com.xazhao.constant.Activiti;
import com.xazhao.constant.Charsets;
import com.xazhao.constant.Constant;
import com.xazhao.constant.Suffix;
import com.xazhao.dto.BpmnModeDTO;
import com.xazhao.exception.ServiceException;
import com.xazhao.service.BpmnModelService;
import com.xazhao.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @Description Created on 2024/03/22.
 * @Author xaZhao
 */

@Slf4j
@Service
public class BpmnModelServiceImpl implements BpmnModelService, ModelDataJsonConstants {

    private final static String NOT = "未发布";

    private final static String ALREADY = "已发布";

    private final static String NODE_NAMESPACE = "namespace";

    private final static String STENCILSET = "stencilset";

    private final static String MODEL_ID = "modelId";

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 编辑模板
     *
     * @param modelId 模板ID
     * @return 对象节点
     */
    @Override
    public ObjectNode getEditorJson(String modelId) {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);

        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
                        new String(repositoryService.getModelEditorSource(model.getId()), StandardCharsets.UTF_8));
                modelNode.replace("model", editorJsonNode);

            } catch (Exception e) {
                log.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

    /**
     * 设计模型，打开Activiti流程设计器
     *
     * @param request
     * @param response
     */
    @Override
    public void createModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put(NODE_NAMESPACE, Activiti.NAMESPACE);
            editorNode.replace(STENCILSET, stencilSetNode);
            Model bpmnModel = repositoryService.newModel();
            // 创建空Json对象
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "Input bpmn model name");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "Input bpmn model description");
            bpmnModel.setMetaInfo(modelObjectNode.toString());
            bpmnModel.setName("Input bpmn model name");
            bpmnModel.setKey(StringUtils.defaultString("Input bpmn model key"));
            // 保存模型
            repositoryService.saveModel(bpmnModel);
            repositoryService.addModelEditorSource(bpmnModel.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
            // 记录模型ID
            request.setAttribute(MODEL_ID, bpmnModel.getId());
            // 重定向到编辑器页面
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + bpmnModel.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 设计模型，通过vue路由打开Activiti流程设计器
     *
     * @param bpmnModel 模型参数
     * @return 模型ID
     */
    @Override
    public InvokeResult designBpmnModel(BpmnModeDTO bpmnModel) {
        try {
            String bpmnKey = bpmnModel.getKey();
            String bpmnName = bpmnModel.getName();
            if (!isNotBlank(bpmnKey) && !isNotBlank(bpmnName)) {
                return InvokeResult.failure("流程模型名称或流程模型Key不能为空！");
            }
            Model checkModel = repositoryService.createModelQuery().modelKey(bpmnKey).singleResult();
            if (ObjectUtil.isNotNull(checkModel)) {
                return InvokeResult.failure("流程模型Key已存在！");
            }
            Integer version = 1;
            // 初始空的模型
            Model model = repositoryService.newModel();
            model.setKey(bpmnKey);
            model.setName(bpmnName);
            model.setVersion(version);
            // 封装模型json对象
            ObjectMapper objectMapper = JsonUtils.getObjectMapper();
            ObjectNode modelNodeJson = objectMapper.createObjectNode();
            modelNodeJson.put(ModelDataJsonConstants.MODEL_NAME, bpmnName);
            modelNodeJson.put(ModelDataJsonConstants.MODEL_REVISION, version);
            modelNodeJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, bpmnModel.getDescription());
            model.setMetaInfo(modelNodeJson.toString());
            // 保存初始化模型基本数据
            repositoryService.saveModel(model);
            // 封装模型对象基础数据Json数据
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put(NODE_NAMESPACE, Activiti.NAMESPACE);
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.replace(STENCILSET, stencilSetNode);
            // 标识key
            ObjectNode propertiesNode = objectMapper.createObjectNode();
            propertiesNode.put("process_id", bpmnKey);
            propertiesNode.put("name", bpmnName);
            editorNode.replace("properties", propertiesNode);
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
            // 返回模型ID由Vue进行路由，不使用重定向
            return InvokeResult.success(model.getId(), "流程模型创建成功！");
            /* response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + bpmnModel.getId()); */
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("流程模型创建失败！");
        }
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
    @Override
    public void saveModel(String modelId, String name, String jsonXml, String svgXml, String description) {
        try {

            Model model = repositoryService.getModel(modelId);
            if (!ObjectUtil.isNotEmpty(model)) {
                throw new ActivitiException("模型不存在！");
            }
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(jsonXml));
            InputStream svgStream = new ByteArrayInputStream(StrUtil.utf8Bytes(svgXml));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            log.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }

    /**
     * 查询bpmn模型
     *
     * @param pageNum   当前页
     * @param pageSize  每页显示的条目数
     * @param bpmnModel 查询条件
     * @return bpmn模型
     */
    @Override
    public PageInfo<BpmnModeDTO> selectBpmnModelPage(Integer pageNum, Integer pageSize, BpmnModeDTO bpmnModel) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        // 根据流程名称匹配
        String bpmnName = bpmnModel.getName();
        if (StringUtils.isNotBlank(bpmnName)) {
            modelQuery.modelNameLike("%" + bpmnName + "%");
        }
        // 根据流程定义Key查询
        String bpmnKey = bpmnModel.getKey();
        if (StringUtils.isNotBlank(bpmnKey)) {
            modelQuery.modelKey(bpmnKey);
        }
        modelQuery.orderByLastUpdateTime().desc();
        // 创建时间降序排列
        modelQuery.orderByCreateTime().desc();
        /*
            注：activiti中的listPage(int firstResult, int maxResults)，其中firstResult参数的意思是从哪条数据开始，maxResults显示多少条
            并不是firstResult是页码，maxResults每页显示的条目数
        */
        int firstResult = (pageNum - 1) * pageSize;
        // 查询模型
        List<Model> modelList = modelQuery.listPage(firstResult, pageSize);
        List<BpmnModeDTO> bpmnModelList = new ArrayList<>();
        modelList.forEach(model -> {
            BpmnModeDTO bpmnMode = new BpmnModeDTO();
            bpmnMode.setId(model.getId());
            bpmnMode.setKey(model.getKey());
            String modelName = model.getName();
            bpmnMode.setName(modelName);
            bpmnMode.setCreateTime(DateUtil.format(model.getCreateTime(), Constant.NORM_SLASH));
            bpmnMode.setLastUpdateTime(DateUtil.format(model.getLastUpdateTime(), Constant.NORM_SLASH));
            bpmnMode.setMetaInfo(model.getMetaInfo());
            bpmnMode.setVersion(Activiti.NOT_DEPLOYED);
            // 模型描述
            JSONObject metaInfo = JSONUtil.parseObj(model.getMetaInfo());
            String description = metaInfo.getStr(ModelDataJsonConstants.MODEL_DESCRIPTION);
            if (isNotBlank(description)) {
                bpmnMode.setDescription(description);
            }
            bpmnMode.setPublishStatus(NOT);
            // 部署ID不为空则已部署
            String deploymentId = model.getDeploymentId();
            if (isNotBlank(deploymentId)) {
                bpmnMode.setDeploymentId(deploymentId);
                bpmnMode.setPublishStatus(ALREADY);
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .deploymentId(model.getDeploymentId()).orderByDeploymentId().desc().singleResult();
                // 部署版本号
                bpmnMode.setVersion(String.valueOf(processDefinition.getVersion()));
            }
            bpmnModelList.add(bpmnMode);
        });
        PageInfo<BpmnModeDTO> bpmnPageInfo = new PageInfo<>(bpmnModelList);
        bpmnPageInfo.setTotal(modelQuery.count());
        return bpmnPageInfo;
    }

    /**
     * zip文件部署流程模型
     *
     * @param bpmnModel zip文件
     * @param modelName 流程名称
     * @return 部署结果
     */
    @Override
    public InvokeResult deployBpmnModel(MultipartFile bpmnModel, String modelName) {
        try {
            if (null != bpmnModel) {
                // 定义zip输入流
                ZipInputStream zipInputStream = new ZipInputStream(bpmnModel.getInputStream());
                // 流程部署
                Deployment deployment = repositoryService.createDeployment()
                        .name(isNotBlank(modelName) ? modelName : "")
                        .addZipInputStream(zipInputStream)
                        .deploy();
                // 输出部署信息
                log.info("{}流程ID : {}", modelName, deployment.getId());
                log.info("{}流程名称 : {}", modelName, deployment.getName());
            } else {
                return InvokeResult.failure(modelName + "模型不存在！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("流程部署失败！");
        }
        return InvokeResult.success(null, modelName + "流程部署成功！");
    }

    /**
     * 部署模型
     *
     * @param bpmnSourceId 模型定义ID
     * @return 部署结果
     */
    @Override
    public InvokeResult deployBpmnRepository(String bpmnSourceId) {
        try {
            if (!isNotBlank(bpmnSourceId)) {
                return InvokeResult.failure("参数错误！");
            }
            // 查询模型基本信息
            Model model = repositoryService.getModel(bpmnSourceId);
            if (!ObjectUtil.isNotEmpty(model)) {
                return InvokeResult.failure("模型数据为空，请先设计流程模型后再进行部署！");
            }
            String modelKey = model.getKey();
            // 查询模型xml字节数组
            byte[] modelSourceXml = repositoryService.getModelEditorSource(model.getId());
            // 查询Json数据
            ObjectNode modelNode = (ObjectNode) JsonUtils.getObjectMapper().readTree(modelSourceXml);
            // 将Json数据转为xml数据
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (bpmnModel.getProcesses().size() == 0) {
                return InvokeResult.failure("数据模型不符要求，请至少设计一条主线流程！");
            }
            // 获取字节数组
            byte[] bpmnModelBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            // xml资源名称，对应act_ge_bytearray表中的name_字段
            String processName = modelKey + Suffix.BPMN20_XML;
            // 流程部署
            Deployment deployment = repositoryService.createDeployment()
                    // 部署名称
                    .name(modelKey)
                    .addString(processName, new String(bpmnModelBytes, Charsets.CHARSET_UTF_8))
                    .deploy();
            log.info("{}流程ID : {}", model.getName(), deployment.getId());
            log.info("{}流程名称 : {}", model.getName(), deployment.getName());
            // 更新部署ID到流程定义模型数据表中
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
            return InvokeResult.success(null, "模型发布成功.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    /**
     * 导出
     *
     * @param bpmnModel 模型数据
     * @param response  响应数据
     * @return 导出结果
     */
    @Override
    public InvokeResult exportBpmnModel(BpmnModeDTO bpmnModel, HttpServletResponse response) {
        Model model = repositoryService.getModel(bpmnModel.getId());
        if (!ObjectUtil.isNotEmpty(model)) {
            return InvokeResult.failure("模型不存在！");
        }
        try (OutputStream outputStream = response.getOutputStream()) {
            // 查询模型xml字节数组
            byte[] modelSourceXml = repositoryService.getModelEditorSource(model.getId());
            if (!ArrayUtil.isNotEmpty(modelSourceXml)) {
                return InvokeResult.failure("模型数据为空，请先设计流程并成功保存！");
            }
            // 查询Json数据
            ObjectNode editorNode = (ObjectNode) JsonUtils.getObjectMapper().readTree(modelSourceXml);
            // 将Json数据转为xml数据
            BpmnModel bpmn = new BpmnJsonConverter().convertToBpmnModel(editorNode);
            if (bpmn.getProcesses().size() == 0) {
                return InvokeResult.failure("数据模型不符要求，请至少设计一条主线流程！");
            }
            // xml资源名称
            String bpmnKey = model.getKey();
            String processName = bpmnKey + Suffix.BPMN;
            // 创建ZIP并添加文件条目
            ZipOutputStream zipStream = new ZipOutputStream(outputStream);
            // 获取bpmn xml字节数组
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmn);
            if (ArrayUtil.isNotEmpty(bpmnBytes)) {
                // 添加BPMN模型
                insertFileToZip(zipStream, processName, IoUtil.toStream(bpmnBytes));
            }
            // png
            byte[] pngBytes = repositoryService.getModelEditorSourceExtra(bpmnModel.getId());
            if (ArrayUtil.isNotEmpty(pngBytes)) {
                // 添加BPMN模型
                insertFileToZip(zipStream, bpmnKey + Suffix.PNG, IoUtil.toStream(pngBytes));
            }
            // 下载zip文件
            String bpmnZipName = model.getName() + Suffix.ZIP;
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(bpmnZipName, Charsets.UTF_8));
            response.flushBuffer();
            // 关流
            zipStream.close();
            return InvokeResult.success(bpmnZipName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("导出Bpmn文件出现未知错误！");
        }
    }

    /**
     * 将文件装入zip包中
     *
     * @param zipStream   zip文件流
     * @param fileName    文件名称
     * @param inputStream inputStream
     */
    public static void insertFileToZip(ZipOutputStream zipStream, String fileName, InputStream inputStream) {
        try {
            ZipEntry zipEntry = new ZipEntry(fileName);
            // 向zip输出流中添加一个zip实体
            zipStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                zipStream.write(buffer, 0, bytesRead);
            }

            zipStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除模型
     *
     * @param bpmnId 模型ID
     * @return 删除结果
     */
    @Override
    public InvokeResult deleteBpmnModel(String bpmnId) {
        if (isNotBlank(bpmnId)) {
            if ("90001".equals(bpmnId)) {
                return InvokeResult.failure("错误，初始化流程模型无法删除. ");
            }
            try {
                repositoryService.deleteModel(bpmnId);
                return InvokeResult.success(null, "删除成功！");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("删除模型出现异常！");
            }
        } else {
            return InvokeResult.failure("参数异常！");
        }
    }
}
