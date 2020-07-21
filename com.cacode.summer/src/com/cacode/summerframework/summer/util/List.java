package com.cacode.summerframework.summer.util;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/15 11:27
 */
public class List<T> extends LinkedList<T> implements TParse {
    /**
     * 给扫描器适配返回T的类型进行实例化
     *
     * @return T的类型，如：Object
     */
    @Override
    public Class<T> findT() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 返回当前对象
     *
     * @return 当前对象
     */
    @Override
    public Object thisObj() {
        return this;
    }

    @Override
    public Class findE() {
        return null;
    }
}
