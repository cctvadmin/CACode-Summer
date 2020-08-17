package com.cacode.summerframework.annotation.use.parseautoinit;

import com.cacode.beanutil.excepotion.QualifierException;
import com.cacode.beanutil.maintool.BeanUtil;
import com.cacode.summerframework.annotation.fieldannotations.Autoinit;
import com.cacode.summerframework.annotation.use.parseautoinit.ref.ClassToNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承此类将自动实例化带@Autoinit()注解的字段
 * <p>
 * 注意：不能实例化当前使用的类，也就是this，否者将无终止循环
 * <p>
 * English:
 * <p>
 * Inheriting this class automatically instantiates fields annotated with @Autoinit()
 * <p>
 * Note: you cannot instantiate the currently used class, i.e., this, or the loop will not terminate
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 16:32
 */
public abstract class BaseAuto {
    private final Object thisSon = returnObj();
    private BeanUtil beanUtil;

    public BaseAuto() {
        this.init();
    }

    /**
     * 这个方法重写之后你得返回当前对象，这样summer才能获取到你的当前的类
     * <p>
     * 示例代码：return this;
     * <p>
     * English:
     * <p>
     * After this method is overridden you have to return the current object so that summer can retrieve your current class
     * <p>
     * Example code: Return this;
     *
     * @return 当前子类
     */
    public abstract Object returnObj();

    /**
     * 初始化
     * <p>
     * English:
     * <p>
     * initialize
     */
    private void init() {
        Annotation[] annotations = this.thisSon.getClass().getAnnotations();
        beanUtil = new BeanUtil(this.thisSon);
        boolean isAutoAll = false;
        //类之上是否包含@Autoinit注解
        for (Annotation item : annotations) {
            if (item.annotationType().equals(Autoinit.class)) {
                isAutoAll = true;
                break;
            }
        }
        //类之上加载@Autoinit
        Field[] fields = beanUtil.getFields();
        if (isAutoAll) {
            for (Field item : fields) {
                if (item.getAnnotation(Autoinit.class) != null) {
                    autoInit(item);
                } else {
                    Class<?> type = item.getType();
                    Class<?>[] consClass = item.getAnnotation(Autoinit.class).consClass();
                    initField(beanUtil, item, type, consClass);
                }
            }
        } else {            //类之上 不 加载@Autoinit
            //保留的属性列表
            List<Field> haveAnnotationFields = new ArrayList<>();
            for (Field item : fields) {
                Annotation[] fieldAnnotation = item.getAnnotations();
                for (Annotation ignored : fieldAnnotation) {
                    //如果字段包含@Autoinit注解，则保留
                    if (ignored.annotationType().equals(Autoinit.class)) {
                        haveAnnotationFields.add(item);
                        break;
                    }
                }
                for (Field field : haveAnnotationFields) {
                    autoInit(field);
                }
            }
        }
    }

    /**
     * 自动实例化字段
     * <p>
     * English:
     * <p>
     * Auto initialize field
     *
     * @param field 字段
     */
    public void autoInit(Field field) {
        Class<?> toClass = field.getAnnotation(Autoinit.class).toClass();
        Class<?>[] consClass = field.getAnnotation(Autoinit.class).consClass();
        //如果toClass参数为空，则指向字段当前类型
        //如String str = new String();
        if (toClass.equals(ClassToNull.class)) {
            toClass = field.getType();
        }
        initField(beanUtil, field, toClass, consClass);
    }

    /**
     * 实例化字段
     * <p>
     * English:
     * <p>
     * initialize field
     *
     * @param beanUtil 实例化之后的beanUtil
     * @param field    要实例化的字段
     */
    private void initField(BeanUtil beanUtil, Field field, Class<?> toClass, Class<?>[] consClass) {
        try {
            Object o = null;
            if (consClass[0] != ClassToNull.class) {
                Class<?>[] classes = new Class<?>[consClass.length];
                Class<?>[] objs = new Class<?>[consClass.length];
                for (int i = 0; i < consClass.length; i++) {
                    classes[i] = Class.class;
                    objs[i] = consClass[i].getConstructor().newInstance().getClass();
                }
                Constructor<?> constructor = toClass.getConstructor(classes);
                o = constructor.newInstance(objs);
                beanUtil.setField(field, o);
            } else {
                Constructor<?> constructor = toClass.getConstructor();
                o = constructor.newInstance();
            }
            beanUtil.setField(field, o);
        } catch (IllegalAccessException | QualifierException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
