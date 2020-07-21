package com.cacode.summerframework.excepotion;

import com.cacode.beanutil.excepotion.NoHaveMethod;
import com.cacode.beanutil.excepotion.QualifierException;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 18:45
 */
public class InvokeExcepotion extends ExcepotionTempleat {
    public InvokeExcepotion() {
        super(CACODE + "反射失败");
    }

    public InvokeExcepotion(String message) {
        super(message);
    }
}
