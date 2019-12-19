package com.gabriel.corpwx.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class HttpClientUtils {

    

    /**
     * 发送HTTP_GET请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html;
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param userName
     * @return 远程主机响应正文
     */
    public static String sendGetRequest(String reqURL, String userName) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

            HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Cookie", "username=" + userName + "");

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    // respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * 发送HTTP_GET请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html;
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param userName
     * @return 远程主机响应正文
     */
    public static String sendGetRequest(String reqURL, String userName, String userAgent) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

            HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Cookie", "username=" + userName + "");

            if (!StringUtils.isEmpty(userAgent)) {
                httpGet.setHeader("User-Agent", userAgent);
            }

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    // respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * GET 请求
     *
     * @param reqURL
     * @param requestHeaderMap
     * @return
     */
    public static String doGet(String reqURL, Map<String, String> requestHeaderMap) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

            HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
            httpGet.setConfig(requestConfig);

            if (requestHeaderMap != null && requestHeaderMap.size() > 0) {
                requestHeaderMap.forEach((k, v) -> httpGet.setHeader(k, v));
            }

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    // respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html;
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL   请求地址(含参数)
     * @param userName
     * @param params
     */
    public static void sendPostRequestAsyn(final String reqURL, final String userName, final Map<String, Object> params) {
        log.info("reqURL -> " + reqURL);

        new Runnable() {
            @Override
            public void run() {
                log.info("-------------------------------------------------------------------------------------------");

                CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.
                try {
                    String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
                    HttpPost httpPost = new HttpPost(reqURL);
                    httpPost.setConfig(requestConfig);
                    httpPost.setHeader("Cookie", "username=" + userName + "");

                    if (params != null) {
                        List<NameValuePair> formParams = new ArrayList<NameValuePair>();// 创建参数队列
                        for (Entry<String, Object> entry : params.entrySet()) {
                            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                        }
                        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
                        httpPost.setEntity(uefEntity);
                    }

                    HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity(); // 获取响应实体
                        if (null != entity) {
                            Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                            respContent = EntityUtils.toString(entity, respCharset);
                            // Consume response content
                            EntityUtils.consume(entity);
                        }
                    } else {
                        HttpEntity entity = response.getEntity(); // 获取响应实体
                        if (null != entity) {
                            Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                            respContent = EntityUtils.toString(entity, respCharset);
                            log.info("respContent -> " + respContent);
                        }
                    }

                    StringBuilder respHeaderDatas = new StringBuilder();
                    for (Header header : response.getAllHeaders()) {
                        respHeaderDatas.append(header.toString()).append("\r\n");
                    }
                    String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
                    String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
                    String respBodyMsg = respContent; // HTTP应答报文体信息
                    log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

                } catch (ConnectTimeoutException cte) {
                    log.error("请求通信[" + reqURL + "]时连接超时", cte);
                } catch (SocketTimeoutException ste) {
                    log.error("请求通信[" + reqURL + "]时读取超时", ste);
                } catch (ClientProtocolException cpe) {
                    log.error("请求通信[" + reqURL + "]时协议超时", cpe);
                } catch (ParseException pe) {
                    log.error("请求通信[" + reqURL + "]时解析异常", pe);
                } catch (IOException ioe) {
                    log.error("请求通信[" + reqURL + "]时网络异常", ioe);
                } catch (Exception e) {
                    log.error("请求通信[" + reqURL + "]时异常", e);
                } finally {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        log.error(JSON.toJSONString(e));
                    }
                }

                log.info("-------------------------------------------------------------------------------------------");
            }
        };

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html;
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param userName
     * @param params
     * @return 远程主机响应正文
     */
    public static String sendPostRequest(String reqURL, String userName, Map<String, Object> params) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setConfig(requestConfig);
            if (!StringUtils.isEmpty(userName)) {
                httpPost.setHeader("Cookie", "username=" + userName + "");
            }
            if (params != null) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();// 创建参数队列
                for (Entry<String, Object> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
                httpPost.setEntity(uefEntity);
            }

            HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html; 5)该方法只需要传一个字符串参数
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param userName
     * @param params
     * @return 远程主机响应正文
     */
    public static String sendPostRequest(String reqURL, String userName, String params, String userAgent) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setConfig(requestConfig);
            if (!StringUtils.isEmpty(userName)) {
                httpPost.setHeader("Cookie", "username=" + userName + "");
            }
            if (!StringUtils.isEmpty(userAgent)) {
                httpPost.setHeader("User-Agent", userAgent);
            }
            if (!StringUtils.isEmpty(params)) {
                StringEntity se = new StringEntity(params, "UTF-8");
                httpPost.setEntity(se);
            }

            HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * get请求数据
     *
     * @param destUrl
     * @return
     */
    public static String doOuterGet(final String destUrl) { //final Map<String, String> params
        log.info("destUrl -> " + destUrl);

        String resultJson = null; // 响应内容
        if (destUrl == null || "".equals(destUrl)) {
            return resultJson;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();// 创建默认的httpClient实例.
        CloseableHttpResponse response = null;
        HttpGet httpget = null;
        try {
            StringBuffer url = new StringBuffer("");
            url.append(destUrl);

//			if (destUrl.contains("?")) {
//				url.append(destUrl.substring(0, destUrl.indexOf("?")));
//			} else {
//				url.append(destUrl);
//			}
//			if (params != null && !params.isEmpty()) {
//				String key = null;
//				for (Iterator<String> ite = params.keySet().iterator(); ite.hasNext();) {
//					key = ite.next();
//					url.append(key).append("=").append(params.get(key)).append("&");
//				}
//			}

            log.info("请求路径：" + url);
            httpget = new HttpGet(url.toString());// 创建httppost
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultJson = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            log.error("请求异常", e);
        } finally {
            try {
                httpclient.close();// 关闭连接,释放资源
            } catch (IOException e) {
                log.error("关闭请求异常", e);
            }

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("资源释放异常", e);
                }
            }

            if (httpget != null) {
                httpget.abort();
            }
        }
        return resultJson;
    }

    /**
     * post请求数据
     *
     * @param destUrl url
     * @param params  参数
     * @return
     */
    public static String doOuterPost(String destUrl, Map<String, String> params) {
        log.info("destUrl -> " + destUrl);

        String resultJson = null; // 响应内容
        if (destUrl == null || "".equals(destUrl)) {
            return resultJson;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();// 创建默认的httpClient实例.
        CloseableHttpResponse response = null;
        HttpPost httppost = null;
        try {
            log.info("请求路径：" + destUrl);

            httppost = new HttpPost(destUrl);// 创建httppost
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();// 创建参数队列
            if (params != null && !params.isEmpty()) {
                String key = null;
                for (Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ) {
                    key = ite.next();
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultJson = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();

            log.error("请求异常", e);
        } finally {
            try {
                httpclient.close();// 关闭连接,释放资源
            } catch (IOException e) {
                log.error("关闭请求异常", e);
            }

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("资源释放异常", e);
                }
            }

            if (httppost != null) {
                httppost.abort();
            }
        }
        return resultJson;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html; 5)该方法只需要传一个字符串参数
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param headers
     * @param params
     * @return 远程主机响应正文
     */
    public static String sendPostRequest(String reqURL, Map<String, String> headers, String params) {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);
        log.info("headers -> " + JSON.toJSONString(headers));

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();

            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setConfig(requestConfig);
            if (headers != null && headers.size() > 0) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            if (!StringUtils.isEmpty(params)) {
                StringEntity se = new StringEntity(params, "UTF-8");
                httpPost.setEntity(se);
            }

            HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 1)该方法会自动关闭连接,释放资源 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * 4)该方法会自动获取到响应消息头中[Content-Type:text/html; 5)该方法只需要传一个字符串参数
     * charset=GBK]的charset值作为响应报文的解码字符集
     * 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     *
     * @param reqURL
     * @param headers
     * @param params
     * @return 远程主机响应正文
     */
    public static String sendSapPostRequest(String reqURL, Map<String, String> headers, String params) throws Exception {
        log.info("-------------------------------------------------------------------------------------------");
        log.info("reqURL -> " + reqURL);

        String respContent = "{\"result\":-1,\"msg\":\"系统繁忙\"}"; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();// 创建默认的httpClient实例.

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();

            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setConfig(requestConfig);
            if (headers != null && headers.size() > 0) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            if (!StringUtils.isEmpty(params)) {
                StringEntity se = new StringEntity(params, "UTF-8");
                httpPost.setEntity(se);
            }

            HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    // Consume response content
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                    respContent = EntityUtils.toString(entity, respCharset);
                    log.info("respContent -> " + respContent);
                }
            }

            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
            String respBodyMsg = respContent; // HTTP应答报文体信息
            log.info("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时", cte);
            throw cte;
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时", ste);
            throw ste;
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议超时", cpe);
            throw cpe;
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常", pe);
            throw pe;
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常", ioe);
            throw ioe;
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时异常", e);
            throw e;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(e));
                throw e;
            }
        }

        log.info("-------------------------------------------------------------------------------------------");
        return respContent;
    }


    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        String authorization = String.format("Bearer %s", "b0a06839-af57-31ce-9271-59a7c70bb56d");
        headers.put("Authorization", authorization);
        headers.put("Content-Type", "text/xml");
        log.info(HttpClientUtils.sendPostRequest("http://218.97.27.74:8280/00001000000053/1.0.0/esbStandard", headers, ""));
    }
}
