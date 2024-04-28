package com.xazhao.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xazhao.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

import java.io.IOException;

/**
 * @Description Created on 2024/03/25.
 * @Author xaZhao
 */

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BpmnUtil {

    public static byte[] bpmnJsonToXmlBytes(byte[] jsonBytes) throws IOException {
        if (jsonBytes == null) {
            return null;
        }
        // json字节码转成 BpmnModel 对象
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonBytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);

        if (bpmnModel.getProcesses().size() == 0) {
            return null;
        }
        // 将bpmnModel转为xml
        return new BpmnXMLConverter().convertToXML(bpmnModel);
    }
}
