package com.xazhao;

import lombok.extern.slf4j.Slf4j;
import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName ActivitiApplication.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@MapperScan("com.sinosoft.mapper")
@SpringBootApplication(exclude = {
        JpaProcessEngineAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class ActivitiApplication {

    public static void main(String[] args) {

        /* 直接打开流程设计器：http://127.0.0.1:49086/bpmn/model/createModel */

        /* 打开流程管理页面：http://127.0.0.1:49086/index.html */

        SpringApplication.run(ActivitiApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  risk-activiti启动成功...   ლ(´ڡ`ლ)ﾞ");

    }
}
