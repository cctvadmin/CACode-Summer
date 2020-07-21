package com.cacode.summerframework.annotation.methodannotations;

import java.lang.annotation.*;

/**
 * 使用教程：
 * <p>
 * \@Delete(sql="UPDATE test SET email='-email-' WHERE id=-id-")
 * public Integer updateEmailById(int id,String email){
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
public @interface Update {
    String sql();
}
