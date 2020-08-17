package com.cacode.summerframework.annotation.fieldannotations;

import java.lang.annotation.*;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/8/16 18:14
 */
@Inherited()
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamType {

    /**
     * 字段与sql字段对应的键
     *
     * @return 键
     */
    String key();
}
