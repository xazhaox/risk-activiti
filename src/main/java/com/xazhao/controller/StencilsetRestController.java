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
 * @ClassName StencilsetRestResource.java
 * @Author AnZhaoxu
 * @Create 2024.04.01
 * @UpdateUser
 * @UpdateDate 2024.04.01
 * @Version 2024.0.1
 * @Description
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
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("activiti/stencilset_en.json");
        try {
            return IOUtils.toString(stencilsetStream, UTF8);
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }
}
