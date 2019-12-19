package com.gabriel.corpwx.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: yq
 * @date: 2019/12/19 16:26
 * @discription 企业微信消息推送响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMessageResp {
    /** 状态码 */
    private Integer errcode;

    /** 消息状态 */
    private String errmsg;

    /** 无效用户 */
    private String invaliduser;
}
