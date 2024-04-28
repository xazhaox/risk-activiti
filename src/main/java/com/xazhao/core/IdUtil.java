package com.xazhao.core;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.context.annotation.Configuration;

/**
 * @Description Created on 2024/03/22.
 * @Author xaZhao
 */

@Configuration
public class IdUtil implements IdGenerator {

    @Override
    public String getNextId() {

        return cn.hutool.core.util.IdUtil.getSnowflake().nextIdStr();
    }
}
