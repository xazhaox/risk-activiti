package com.xazhao.core;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName IdUtil.java
 * @Author AnZhaoxu
 * @Create 2024.03.22
 * @UpdateUser
 * @UpdateDate 2024.03.22
 * @Version 2024.0.1
 * @Description
 */

@Configuration
public class IdUtil implements IdGenerator {

    @Override
    public String getNextId() {

        return cn.hutool.core.util.IdUtil.getSnowflake().nextIdStr();
    }
}
