package com.cacode.summerframework.summer.util;

import java.lang.reflect.ParameterizedType;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/15 16:36
 */
public class Map<T, E> extends java.util.HashMap<T, E> implements TParse {
    @Override
    public Class<T> findT() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    @Override
    public Class<E> findE() {
        Class<E> eClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return eClass;
    }

    @Override
    public Object thisObj() {
        return this;
    }
}
