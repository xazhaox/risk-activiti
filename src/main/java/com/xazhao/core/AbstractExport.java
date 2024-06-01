package com.xazhao.core;

import com.xazhao.entity.user.UserInfo;

/**
 * @Description Created on 2024/05/06.
 * @Author xaZhao
 */

public abstract class AbstractExport<T, K> {

    /**
     * 导出
     *
     * @param sysUser 用户
     * @throws InterruptedException InterruptedException
     */
    public abstract void export(UserInfo sysUser) throws InterruptedException;


}
