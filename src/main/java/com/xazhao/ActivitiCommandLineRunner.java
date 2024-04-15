package com.xazhao;

import com.xazhao.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Spring Boot的CommandLineRunner接口是一个函数式接口，用于在Spring Boot应用程序启动后执行一些初始化操作<br/>
 * 实现CommandLineRunner接口，重写run方法来定义的初始化逻辑，该方法在应用程序启动后被调用<br/>
 * 使用CommandLineRunner接口，可以在应用程序启动后执行一些必要的初始化操作<br/>
 * 例如：加载配置文件、初始化数据库连接、创建默认数据等
 *
 * @ClassName ActivitiCommandLineRunner.java
 * @Author AnZhaoxu
 * @Create 2024.04.11
 * @UpdateUser
 * @UpdateDate 2024.04.11
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@Component
public class ActivitiCommandLineRunner implements CommandLineRunner {

    @Value("${browser.auto.is-open-browser}")
    private Boolean isOpenBrowser;

    @Resource
    private Environment environment;

    /**
     * 定义自己的初始化逻辑
     *
     * @param args incoming main method arguments
     * @throws Exception Exception
     */
    @Override
    public void run(String... args) throws Exception {
        /* 打开流程管理页面：http://127.0.0.1:49086/index.html */

        // ip
        String ip = InetAddress.getLocalHost().getHostAddress();
        // port
        String port = environment.getProperty("server.port");
        // Activiti流程设计器Url
        String activitiDesignerUrl = Constant.HTTP + Constant.LOCAL_HOST + ":" + port + "/index.html";
        // 是否打开浏览器
        if (isOpenBrowser) {
            try {
                Runtime.getRuntime().exec("cmd /c start " + activitiDesignerUrl);
            } catch (IOException e) {
                // 打开失败关闭项目
                System.exit(1);
            }
        }
        log.info("Activiti流程设计器：{}", activitiDesignerUrl);

        // 初始化Redis
    }

}
