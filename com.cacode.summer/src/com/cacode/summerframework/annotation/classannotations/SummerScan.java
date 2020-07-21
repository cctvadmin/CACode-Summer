package com.cacode.summerframework.annotation.classannotations;

import java.lang.annotation.*;

/**
 * 提供接口扫描
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 6:39
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SummerScan {
}
