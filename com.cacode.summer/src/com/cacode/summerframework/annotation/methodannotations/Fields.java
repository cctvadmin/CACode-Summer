package com.cacode.summerframework.annotation.methodannotations;

import java.lang.annotation.*;

/**
 * 配置多个映射关系
 * <p>
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 6:19
 */
@Inherited()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Fields {
    Field[] field();
}
