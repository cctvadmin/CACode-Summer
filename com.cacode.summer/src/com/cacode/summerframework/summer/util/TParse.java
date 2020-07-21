package com.cacode.summerframework.summer.util;

import java.util.List;

/**
 * class Map<T,E> implements TParse{
 * <p>
 * ..
 * }
 * <p>
 * or
 * <p>
 * class List<T> implements TParse{
 * <p>
 * ...
 * }
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/15 17:27
 */
public interface TParse<T, E> {
    /**
     * 提供基础的查找T的值
     * <p>
     * com.cacode.summerframework.util.Map需要两个findT()/findE()
     *
     * @return
     */
    Class<T> findT();

    /**
     * 返回当前对象
     *
     * @return 返回自身对象
     */
    Object thisObj();

    /**
     * 提供查找E的值
     * <p>
     * com.cacode.summerframework.util.List 不需要此事件，所以将返回null
     *
     * @return
     */
    Class<E> findE();
}
