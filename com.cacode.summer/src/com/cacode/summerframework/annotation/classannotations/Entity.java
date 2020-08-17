package com.cacode.summerframework.annotation.classannotations;

import java.lang.annotation.*;

/**
 * 数据库实体类
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 6:37
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    /**
     * 对应表名
     *
     * @return 表名
     */
    String tabName();
}
