package com.cacode.summerframework.annotation.fieldannotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 在接口的方法参数中指定当前字段在sql语句上的名称
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/22 2:10
 */
@Inherited()
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(PARAMETER)
public @interface Param {
    /**
     * 设置当前字段对应的名字
     *
     * @return 字段对应的名字
     */
    String name() default "";
}
