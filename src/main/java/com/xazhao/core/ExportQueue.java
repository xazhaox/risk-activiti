package com.xazhao.core;

import cn.hutool.core.collection.CollUtil;
import com.xazhao.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * 分别是用户、导出队列、导出执行方法
 * ExportQueue：维护一条定长队列，可以获取队列里前后排队的用户，提供查询，队列如果已经满了，其余的人需要进行等待
 * User信息：排队执行导出方法对应用户；
 * Export类：定义导出方法，异步执行，用户可以通过导出页面查看、下载，导出的文件；
 *
 * @Description Created on 2024/05/06.
 * @Author xaZhao
 */

@Slf4j
@Component
public class ExportQueue {

    /**
     * 队列最大容量
     */
    private static final int MAX_CAPACITY = 10;

    /**
     * 用户队列，LinkedList基于双链表实现，在LinkedList中，元素的添加和删除操作都是在链表的两端进行，所以它天然支持先进先出的特性。
     */
    private LinkedList<UserInfo> queue;

    // public ExportQueue() {
    //     this.queue = new LinkedList<>();
    // }

    public ExportQueue(LinkedList<UserInfo> queue) {
        this.queue = new LinkedList<>();
    }

    /**
     * 排队队列添加
     *
     * @param sysUser 用户
     * @return 用户队列
     */
    public LinkedList<UserInfo> add(UserInfo sysUser) {
        synchronized (this) {
            // 用户队列是否已满
            while (queue.size() >= MAX_CAPACITY) {
                try {
                    log.info("当前队列等待人数已达最大值，请等待...");
                    // 使当前线程无限期进行等待，直至队列中有位置
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 队列未满，将用户加入队列排队
            queue.add(sysUser);
            log.info("目前导出队列排队人数为：{}", queue.size());
            // 唤醒在此对象的监视器上等待的所有线程
            this.notifyAll();
            // 返回队列
            return queue;
        }
    }

    public UserInfo getNextUser() {
        synchronized (this) {
            while (!CollUtil.isNotEmpty(queue)) {
                try {
                    // 使当前线程无限期进行等待，直至队列中有位置
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            UserInfo user = queue.remove();
            // 唤醒在此对象的监视器上等待的所有线程
            this.notifyAll();
            // 返回下一个用户
            return user;
        }
    }
}
