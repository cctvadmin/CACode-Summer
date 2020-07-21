package com.cacode.summerframework.annotation.methodannotations;

import java.lang.annotation.*;

/**
 * 配置实体类和数据库表的字段映射
 * <p>
 * 比如你有一个表的字段是user_name，而在你的实体类里面叫userName
 * <p>
 * 那么你就可以这样：
 * <p>
 * \@Field(pro="userName",col="user_name")
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 6:19
 */
@Inherited()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Field {
    /**
     * 实体类字段
     *
     * @return
     */
    String pro();

    /**
     * 数据库字段
     *
     * @return
     */
    String col();
}
