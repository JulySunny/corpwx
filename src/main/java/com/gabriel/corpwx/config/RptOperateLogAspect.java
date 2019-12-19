//package com.gabriel.corpwx.config;
//
//import com.in.g.common.dto.SessionUserDTO;
//import com.in.g.data.mapper.RptOperateLogMapper;
//import com.in.g.data.mapper.model.RptOperateLog;
//import com.in.g.data.service.UserService;
//import com.in.g.data.util.AgentUserKit;
//import com.in.g.data.util.IpAddressUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.ArrayUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.Date;
//
///**
// * @ClassName: RptOperateLogAspect
// * @Author: yq
// * @Date: 2019-12-04 08:59
// * @Description: 报表日志操作切面类
// **/
//@Slf4j
//@Aspect
//@Component
//public class RptOperateLogAspect {
//
//    @Autowired
//    private RptOperateLogMapper rptOperateLogMapper;
//
//    @Autowired
//    private UserService userService;
//
//    //定义切点 @Pointcut
//    //在注解的位置切入代码
//    @Pointcut("@annotation(com.in.g.data.config.RptLog)")
//    public void logPoinCut() {
//    }
//
//    //切面 配置通知
//    @AfterReturning("logPoinCut()")
//    public void saveSysLog(JoinPoint joinPoint) {
//        System.out.println("报表系统记录操作日志");
//        //保存日志
//        RptOperateLog rptOperateLog = new RptOperateLog();
//
//        //从切面织入点处通过反射机制获取织入点处的方法
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        //获取切入点所在的方法
//        Method method = signature.getMethod();
//
//        //获取当前用户操作信息
//        Object[] args = joinPoint.getArgs();
//        if (ArrayUtils.isNotEmpty(args)){
//            Integer id = (Integer) args[0];
//            //记录操作用户的信息
//            SessionUserDTO user = userService.getUser(id);
//            rptOperateLog.setRole(user.getRoleName());
//            rptOperateLog.setUsername(user.getFullName());
//            rptOperateLog.setAccount(user.getUserName());
//        }
//        RptLog rptLog = method.getAnnotation(RptLog.class);
//        if (rptLog != null) {
//            String value = rptLog.value();
//            //操作名称
//            rptOperateLog.setOperateContent(value);
//        }
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        //操作时间
//        rptOperateLog.setOperateTime(new Date());
//        //操作用户ip地址
//        rptOperateLog.setIpAddress(IpAddressUtil.getIpAddr(request));
//        //操作设备信息
//        String deviceInfo = AgentUserKit.getDeviceInfo(request);
//        rptOperateLog.setDevice(deviceInfo);
//        //保存操作日志
//        rptOperateLogMapper.saveLog(rptOperateLog);
//        log.info("报表日志保存成功 rptOperateLog:[{}]",rptOperateLog);
//    }
//}
