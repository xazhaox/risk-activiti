package com.xazhao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

@Slf4j
@MapperScan("com.xazhao.mapper")
@SpringBootApplication(exclude = {
        org.activiti.spring.boot.JpaProcessEngineAutoConfiguration.class,
        // Springboot和activity-rest一般默认自带http basic的security安全验证，需要排除以下Class
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
