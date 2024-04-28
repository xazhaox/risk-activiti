package com.xazhao.common;

import org.springframework.util.StringUtils;

/**
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

public interface IBaseParam {

    /**
     * 自定义DTO可以实现本方法自行输出入参
     *
     * @return 入参参数
     */
    String paramInfo();


    /**
     * 工具默认方法，把参数转换成逗号分隔字符串在日志中输出
     *
     * @param params 参数数组
     * @return 逗号分隔字符串
     */
    default String buildParamInfo(String... params) {
        return StringUtils.arrayToCommaDelimitedString(params);
    }
}
