package com.xazhao.constant;

/**
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

public class Message {

    public Message() {}

    public static final String PROCESS_KEY_ISBLANK = "流程定义Key不能为空！";

    public static final String PROCESS_KEY_AND_BUSINESS_KEY_ISBLANK = "流程定义Key及业务唯一Key不能为空！";

    public static final String IS_NOT_OPERATOR = "您不是当前任务办理人，请按流程操作！";

    public static final String ACT_EXCEPTION = "流程加签出现异常！";

    public static final String ACT_TRANSFER_EXCEPTION = "转办任务出现异常！";

    public static final String TRANSFER_SUCCESS = "任务转办成功！";

    public static final String SIGN_FINISH = "流程加签完成！";

    public static final String COMPLETE_CURRENT_DELEGATE_TASK = "请先完成当前加签任务，由发起人进行加签！";

    public static final String SIGN_BACK_IN = "用户登录失效，请重新登录系统";

}
