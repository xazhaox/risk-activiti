package com.xazhao.controller;

import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * 模具集Rest资源
 *
 * @Description Created on 2024/04/01.
 * @Author xaZhao
 */

@SuppressWarnings("all")
@RestController
public class StencilsetRestController {

    public static final String UTF8 = "utf-8";

    /**
     * 加载模板集
     *
     * @return
     */
    @RequestMapping(value = "/bpmn/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public @ResponseBody String getStencilset() {
        // String stencilset = "activiti/stencilset.json";
        String stencilset = "activiti/stencilset_en.json";
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream(stencilset);
        try {
            return IOUtils.toString(stencilsetStream, UTF8);
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }
}
