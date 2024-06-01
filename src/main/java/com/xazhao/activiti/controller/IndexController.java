package com.xazhao.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.*;

/**
 * @Description Created on 2024/04/25.
 * @Author xaZhao
 */

// @RestController
// @Controller
public class IndexController {


    // 注：在使用Controller控制器跳转访问时需要注意

    // 不能在使用@RestController去声明一个Bean，而是要使用@Controller声明Bean

    // @RestController是@Controller和@ResponseBody的结合，用在类上表示该类的所有方法返回的都是数据，
    // 而不是视图。这意味着返回的数据将直接发送到客户端，通常是以 JSON 为数据格式

    // @Controller标记一个类作为 Spring MVC 的控制器，可以返回一个视图名称，
    // Spring MVC 会根据这个视图名称来查找并渲染相应的视图（如 HTML、JSP、Thymeleaf 模板等）

    @RequestMapping("/router")
    public String routerActivitiDesigner() {
        return "templates";
    }
}
