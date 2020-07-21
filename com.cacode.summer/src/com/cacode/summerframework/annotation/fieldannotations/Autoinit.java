package com.cacode.summerframework.annotation.fieldannotations;

import com.cacode.summerframework.annotation.use.parseautoinit.ref.ClassToNull;

import java.lang.annotation.*;

/**
 * 设置于字段之上，用于自动初始化
 * <p>
 * 标注于类上，将自动实例化所有字段
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 16:28
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Autoinit {
    /**
     * 实例化成的类
     * <p>
     * 设置为空将实例化本身
     *
     * @return 注解指向的类
     */
    Class<?> toClass() default ClassToNull.class;

    Class<?>[] consClass() default ClassToNull.class;
}
