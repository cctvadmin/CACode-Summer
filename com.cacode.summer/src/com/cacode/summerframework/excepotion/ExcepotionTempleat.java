package com.cacode.summerframework.excepotion;

import com.cacode.beanutil.excepotion.QualifierException;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 18:47
 */
public class ExcepotionTempleat extends QualifierException {
    public static String CACODE = "com.cacode.summer";

    public ExcepotionTempleat() {
        super(CACODE);
    }

    public ExcepotionTempleat(String message) {
        super(message);
    }
}
