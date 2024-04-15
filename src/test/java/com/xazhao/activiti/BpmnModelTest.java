package com.xazhao.activiti;

import cn.hutool.json.JSONUtil;
import com.xazhao.common.InvokeResult;
import com.xazhao.service.BpmnModelService;
import com.xazhao.utils.MultipartFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName BpmnModelTest.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BpmnModelTest {

    @Resource
    private BpmnModelService bpmnModelService;

    /**
     * 自定义方式，使用activiti提供的默认方式来创建MySQL的表
     */
    @Test
    public void createActivitiDatabaseTest() {
        // 配置文件的名称可以自定义, bean的id也可以自定义
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        log.info(processEngine.getName());
    }

    /**
     * Zip部署流程
     */
    @Test
    public void deployBpmnModelTest() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/Sign.zip");
            MultipartFile multipartFile = MultipartFileUtil.convert(inputStream, "Sign.zip");
            InvokeResult result = bpmnModelService.deployBpmnModel(multipartFile, "会签流程测试");
            log.info(JSONUtil.toJsonStr(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
