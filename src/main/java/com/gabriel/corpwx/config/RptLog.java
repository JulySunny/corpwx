package com.gabriel.corpwx.config;

import java.lang.annotation.*;

/**
 * @ClassName: RptOperateLog
 * @Author: yq
 * @Date: 2019-12-04 08:58
 * @Description: 自定义日志注解
 **/
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface RptLog {
    String value() default "";
}
