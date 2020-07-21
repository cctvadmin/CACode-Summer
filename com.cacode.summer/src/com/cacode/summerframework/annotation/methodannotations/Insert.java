package com.cacode.summerframework.annotation.methodannotations;

import java.lang.annotation.*;

/**
 * 使用教程：
 * <p>
 * \@Insert(sql="INSERT INTO test VALUES({-id-},'{-email-}','{-phone-}','{-nickName-}')")
 * public Integer addUser(int id,String email,String phone,String nickName){
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
public @interface Insert {
    String sql();
}
