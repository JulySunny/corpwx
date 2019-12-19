package com.gabriel.corpwx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: yq
 * @date: 2019/12/17 9:10
 * @discription
 */
@Data
@Component
@ConfigurationProperties(prefix = "corpwechat")
public class CorpWechatConfig {


    /** 企业Id */
    private String corpid;

    /** 企业号密钥 */
    private String corpsecret;
    
    /** 企业应用的id */
    private Integer agentId;
    
    /** 消息推送url */
    private String sendMessageUrl;
}
