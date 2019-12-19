package com.gabriel.corpwx.task;

import com.alibaba.fastjson.JSON;
import com.gabriel.corpwx.comom.WxMsgConstant;
import com.gabriel.corpwx.comom.WxMsgTypeEnum;
import com.gabriel.corpwx.config.CorpWechatConfig;
import com.gabriel.corpwx.service.RedisService;
import com.gabriel.corpwx.util.DateUtils;
import com.gabriel.corpwx.util.HttpClientUtils;
import com.gabriel.corpwx.util.LocalDateTimeUtils;
import com.gabriel.corpwx.vo.resp.WxMessageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yq
 * @date: 2019/12/13 17:35
 * @discription
 */
@Slf4j
@Component
public class WxMessageTask {

    @Autowired
    private CorpWechatConfig corpWechatConfig;

    @Autowired
    private RedisService redisService;


    /**
    * @Description: 企业微信消息推送 每天9点推送一次
    * @Param: [] Scheduled(cron = "0 0 9 1/1 * ? *") 正式环境使用 每天9点推送消息
    * @return: void
    * @Date: 2019-12-17
    */
    //@Scheduled(cron = "0 */5 * * * ?") //每5分钟发送一次 调试用
    @Scheduled(cron = "0 0 9 1/1 * ?")
    public void pushMessage(){
        log.info("消息发送  开始=> 时间:[{}]", LocalDateTimeUtils.formatTime(LocalDateTime.now(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        //1.获取access_token -这里我是将access_token放到了redis缓存并添加失效时间,使用时取出来即可
        String accessToken = (String) redisService.get(WxMsgConstant.TOKEN);
        if (StringUtils.isBlank(accessToken)){
            //redis里面有重试
            log.info("消息推送:access_token获 取失败");
            throw new RuntimeException("access_token无法获取");
        }

        //2.构造请求
        String url = String.format(corpWechatConfig.getSendMessageUrl(), accessToken);
        //请求头
        Map<String,String> headers=new HashMap(16);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        //请求体
        Map<String,Object> params=new HashMap<>(16);
        //TODO 暂时用自己的userId测试
        params.put(WxMsgConstant.TO_USER, "YangQiang");
        params.put(WxMsgConstant.MSG_TYPE, WxMsgTypeEnum.TEXT.getType());
        params.put(WxMsgConstant.AGENT_ID, corpWechatConfig.getAgentId());
        Map<String,String> contentMap =new HashMap<>(16);
        contentMap.put(WxMsgConstant.CONTENT, "企业日报已生成,请及时查看");
        params.put(WxMsgConstant.TEXT, contentMap);
        log.info("企业微信消息推送==>> body:[{}],header:[{}],url:[{}]",params,headers,url);
        String result = HttpClientUtils.sendPostRequest(url, headers, JSON.toJSONString(params));

        if (StringUtils.isBlank(result)){
            throw new RuntimeException("消息发送失败");
        }
        WxMessageResp wxMessageResp = JSON.parseObject(result, WxMessageResp.class);
        if (wxMessageResp.getErrcode()!=0){
            log.info("消息发送失败 WxMessageResp:[{}],时间:[{}]",wxMessageResp,
                    LocalDateTimeUtils.formatTime(LocalDateTime.now(), DateUtils.YYYY_MM_DD_HH_MM_SS));
            throw  new RuntimeException("消息发送失败");
        }
        log.info("消息发送成功=> wxMessageResp[{}],时间:[{}]",wxMessageResp,
                LocalDateTimeUtils.formatTime(LocalDateTime.now(), DateUtils.YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 手动测试的方法
     * @param args
     */
    public static void main(String[] args) {
        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s",
                "Z8fUMY3Z_BEhC8qFpXdLhjyu0B6NeTtgI3CRLzht-_FJugFS9705NFvuHmpT8kYezLY7C_sEwwr15UeN8LTcdrckIsKaIaSxhKka3UN_NFb2hvahUSBBnuYNMr-kGOZU1vv2TBIXDs5siXqI52Dq9Rw7cnTmq5t07soGu4qEvgIwwfNwp4CkBW1utf4giEVCDfWXn9rlAKZzo3UZ1Ie9uw");
        Map<String,String> headers=new HashMap(16);
        headers.put("Content-Type", "application/json; charset=UTF-8");

        Map<String,Object> params=new HashMap<>();
        params.put(WxMsgConstant.TO_USER, "YangQiang");
        params.put(WxMsgConstant.MSG_TYPE,"text");
        params.put(WxMsgConstant.AGENT_ID, 1000002);
        Map<String,String> contentMap =new HashMap<>(16);
        contentMap.put(WxMsgConstant.CONTENT, "测试消息-企业日报已生成,请及时查看");
        params.put(WxMsgConstant.TEXT, contentMap);

        String result = HttpClientUtils.sendPostRequest(url, headers, JSON.toJSONString(params));
        if (StringUtils.isBlank(result)){
            throw new RuntimeException("消息发送失败");
        }
        WxMessageResp wxMessageResp = JSON.parseObject(result, WxMessageResp.class);
        if (wxMessageResp.getErrcode()!=0){
            log.info("消息发送失败 WxMessageResp:[{}]",wxMessageResp);
            throw  new RuntimeException("消息发送失败");
        }
        log.info("消息发送成功:[{}]",wxMessageResp);
    }

}
