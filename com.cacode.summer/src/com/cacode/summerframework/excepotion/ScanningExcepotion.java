package com.cacode.summerframework.excepotion;

import com.cacode.beanutil.excepotion.NoHaveMethod;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 16:22
 */
public class ScanningExcepotion extends NoHaveMethod {
    /**
     * 默认字符串：CACode-SummerFramework-扫描包失败，请检查注解是否已配置在指定位置
     */
    public ScanningExcepotion() {
        super("CACode-SummerFramework-扫描包失败，请检查注解是否已配置在指定位置");
    }
}
