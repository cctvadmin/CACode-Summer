package com.cacode.summerframework.summer;

import com.cacode.beanutil.maintool.BeanUtil;
import com.cacode.jdbcutil.JdbcUtil;
import com.cacode.summerframework.annotation.classannotations.Entity;
import com.cacode.summerframework.annotation.fieldannotations.Param;
import com.cacode.summerframework.annotation.methodannotations.Delete;
import com.cacode.summerframework.annotation.methodannotations.Insert;
import com.cacode.summerframework.annotation.methodannotations.Select;
import com.cacode.summerframework.annotation.methodannotations.Update;
import com.cacode.summerframework.annotation.use.parseAnnotation.ParsingMethodAnnotation;
import com.cacode.summerframework.summer.util.Map;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import java.util.List;
import java.util.Objects;

/**
 * 生成javabean
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/21 22:51
 */
public class SummerBeanFactory implements InvocationHandler {

    private Class<?> proxyInterface = null;
    //这里可以维护一个缓存，存这个接口的方法抽象的对象
    /**
     * 指向配置参考类
     */
    private SummerProp prop;

    private Object bean = null;

    /**
     * 生产javabean
     *
     * @param proxyInterface 需要实现的接口
     * @param prop           配置文件
     */
    public SummerBeanFactory(Class<?> proxyInterface, Class<?> prop) {
        this.proxyInterface = proxyInterface;
        try {
            this.prop = (SummerProp) prop.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        ClassLoader classLoader = proxyInterface.getClassLoader();
        this.bean = Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[]{proxyInterface}, this);
    }

    /**
     * 生产javabean
     *
     * @param prop 配置文件
     */
    public SummerBeanFactory(Class<?> prop) {
        try {
            this.prop = (SummerProp) prop.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public SummerBeanFactory() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //切入
        this.prop.before();
        Object re = null;
        //相同方法名String sql(),使用BeanUtil统一生成
        BeanUtil bean = null;
        //是否为查询语句
        boolean isSelect = false;
        //获取注解
        Annotation[] annotations = method.getAnnotations();
        //获取形参
        Parameter[] parameters = method.getParameters();
        //获取方法的返回值
        Class<?> returnType = method.getReturnType();
        //设置字段对应值
        Map<String, Object> map = new Map<>();
        int index = 0;
        for (Parameter item : parameters) {
            map.put(item.getAnnotation(Param.class).name(), args[index++]);
        }
        //判断sql类型以及解析
        for (Annotation item : annotations) {
            if (item.annotationType().equals(Insert.class)) {
                bean = new BeanUtil(method.getAnnotation(Insert.class));
            } else if (item.annotationType().equals(Delete.class)) {
                bean = new BeanUtil(method.getAnnotation(Delete.class));
            } else if (item.annotationType().equals(Update.class)) {
                bean = new BeanUtil(method.getAnnotation(Update.class));
            } else if (item.annotationType().equals(Select.class)) {
                isSelect = true;
                bean = new BeanUtil(method.getAnnotation(Select.class));
            }
            if (bean != null) {
                Method sql = bean.selectMethod("sql");
                String sqlStr = (String) sql.invoke(bean.getaObj());
                //解析sql语句
                ParsingMethodAnnotation parse = new ParsingMethodAnnotation(sqlStr);
                //获取解析残留字段
                List<String> strArgs = parse.getArgs();
                //排序后或取与字段对应的值
                parse.sort(map);
                List<Object> argsValue = parse.getArgsValue();

                JdbcUtil jdbcUtil = new JdbcUtil(prop.prop(new Map<>()));
                String replaceStr = parse.getReplaceStr();
                Object[] objects = argsValue.toArray(new Object[]{});
                List<List<Object>> list = null;
                if (isSelect) {
                    list = jdbcUtil.readAll(replaceStr, objects);
                } else {
                    re = jdbcUtil.update(replaceStr, objects);
                }
                switch (returnType.getName()) {
                    case "java.lang.String":
                        return re.toString();
                    case "int":
                        return Integer.parseInt(re.toString());
                    case "com.cacode.summerframework.summer.util.List":
                    case "java.util.List":
                    case "java.lang.Object":
                        if (isSelect) {
                            boolean isEntity = false;
                            for (Annotation annotation : returnType.getAnnotations()) {
                                if (annotation.annotationType().equals(Entity.class)) {
                                    isEntity = true;
                                    break;
                                }
                            }
                            if (isEntity) {
                                BeanUtil beanUtil = new BeanUtil(returnType);
                                Field[] fields = beanUtil.getFields();
                                List<?> tool = beanUtil.getTool(list);
                            } else {

                            }
                            return list;
                        } else {
                            return re;
                        }
                    default:
                        break;
                }
            }
        }
        //切出
        this.prop.after();
        return re;
    }

    /**
     * 获取实例化之后的接口对象
     *
     * @return 接口对象
     */
    public Object getBean() {
        return this.bean;
    }

    /**
     * 获取实例化之后的接口对象
     *
     * @param proxyInterface 实例化的接口
     * @return 接口对象
     */
    public Object getBean(Class<?> proxyInterface) {
        SummerBeanFactory summerBeanFactory = null;
        try {
            Constructor<? extends SummerBeanFactory> constructor = SummerBeanFactory.class.getConstructor(Class.class, Class.class);
            summerBeanFactory = constructor.newInstance(proxyInterface, this.prop.getClass());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(summerBeanFactory).getBean();
    }
}