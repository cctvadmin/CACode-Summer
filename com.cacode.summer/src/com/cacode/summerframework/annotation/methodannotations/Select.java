package com.cacode.summerframework.annotation.methodannotations;

import java.lang.annotation.*;

/**
 * 使用教程：
 * <p>
 * \@Select(sql="SELECT * FROM test WHERE id=-id-")
 * public Integer findUserById(int id){
 * ...<p>
 * }
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 5:47
 */
@Inherited()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String sql();
}
