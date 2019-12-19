package com.gabriel.corpwx.comom;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yq
 * @date: 2019/12/19 16:58
 * @discription 微信消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum WxMsgTypeEnum {
    /** 文本消息 */
    TEXT(1, "text"),
    /** 图片消息 */
    IMAGE(2, "image"),
    /** 语音消息 */
    VOICE(3, "voice"),
    /** 视频消息 */
    VIDEO(4, "video"),
    /** 文件消息 */
    FILE(5, "file"),
    /** 文本卡片消息 */
    TEXTCARD(6, "textcard"),
    /** 图文消息 */
    MPNEWS(7, "mpnews"),
    /** markdown消息 */
    MARKDOWN(8, "markdown"),
    /** 小程序通知消息 */
    MINIPROGRAM_NOTICE(9, "miniprogram_notice"),
    /** 任务卡片消息 */
    TASKCARD(10, "taskcard");


    private Integer code;
    private String type;
}
