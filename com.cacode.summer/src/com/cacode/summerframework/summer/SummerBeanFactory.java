package com.cacode.summerframework.summer;

import com.cacode.beanutil.maintool.BeanUtil;
import com.cacode.summerframework.annotation.classannotations.Entity;
import com.cacode.summerframework.annotation.fieldannotations.Param;
import com.cacode.summerframework.annotation.fieldannotations.ParamType;
import com.cacode.summerframework.annotation.methodannotations.Delete;
import com.cacode.summerframework.annotation.methodannotations.Insert;
import com.cacode.summerframework.annotation.methodannotations.Select;
import com.cacode.summerframework.annotation.methodannotations.Update;
import com.cacode.summerframework.annotation.use.parseAnnotation.ParsingMethodAnnotation;
import com.cacode.summerframework.excepotion.InvokeExcepotion;
import com.cacode.summerframework.summer.util.Map;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * factory生成javabean
 * <p>
 * 生成规则：
 * <p>
 * 收集注解类->获取方法注解和参数->对注解进行解析->取得sql语句->解析sql语句->执行sql语句->>
 * <p>
 * 得到返回值->得到返回类型->解析返回值->注入bean->>
 * <p>
 * 返回执行结果
 * <p>
 * English:
 * <p>
 * Factory generates Javabeans
 * <p>
 * Generation rules:
 * <p>
 * collect annotations class -> get method annotations and parameters -> parse annotations ->>
 * <p>
 * get SQL statements -> parse SQL statements -> execute SQL statements ->>
 * <p>
 * get the return value -> get the return type -> parse the return value -> inject bean->>
 * <p>
 * return the result of execution
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/21 22:51
 * @since jdk13
 */
public class SummerBeanFactory implements InvocationHandler {

    private Class<?> proxyInterface = null;
    // 这里可以维护一个缓存，存这个接口的方法抽象的对象
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
            Constructor<?> constructor = prop.getConstructor();
            this.prop = (SummerProp) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.bean = Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[]{proxyInterface}, this);
    }

    /**
     * 生产javabean
     *
     * @param prop 配置文件
     */
    public SummerBeanFactory(Class<?> prop) {
        try {
            Constructor<?> constructor = prop.getConstructor();
            this.prop = (SummerProp) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public SummerBeanFactory() {
    }

    /**
     * 统一生成bean
     *
     * @param proxy  反射对象
     * @param method 执行的方法
     * @param args   参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 切入
        this.prop.before();
        Object re = null;
        // 相同方法名String sql(),使用BeanUtil统一生成
        BeanUtil bean = null;
        // 是否为查询语句
        boolean isSelect = false;
        // 获取注解
        Annotation[] annotations = method.getAnnotations();
        // 获取形参
        Parameter[] parameters = method.getParameters();
        // 获取方法的返回值
        Class<?> returnType = method.getReturnType();
        // 设置字段对应值
        Map<String, Object> map = new Map<>();
        int index = 0;
        for (Parameter item : parameters) {
            map.put(item.getAnnotation(Param.class).name(), args[index++]);
        }
        // 判断sql类型以及解析
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
                // 解析sql语句
                ParsingMethodAnnotation parse = new ParsingMethodAnnotation(sqlStr);
                parse.setStr(sqlStr);
                // 获取解析残留字段
                List<String> strArgs = parse.getArgs();
                // 排序后或取与字段对应的值
                parse.sort(map);
                List<Object> argsValue = parse.getArgsValue();

                //获取jdbcutil
                Class<?> jdbcUtilClass = Class.forName("com.cacode.jdbcutil.JdbcUtil");

                if (jdbcUtilClass == null) {
                    throw new InvokeExcepotion("找不到包:com.cacode.jdbcutil.JdbcUtil");
                }

                Constructor<?> constructor = jdbcUtilClass.getConstructor(java.util.Map.class);
                Object jdbcUtil = constructor.newInstance(prop.prop(new Map<>()));
                // JdbcUtil jdbcUtil = new JdbcUtil(prop.prop(new Map<>()));
                BeanUtil jdbcBean = new BeanUtil(jdbcUtil);
                String replaceStr = parse.getReplaceStr();
                Object[] objects = argsValue.toArray(new Object[]{});
                List<List<Object>> list = null;
                if (isSelect) {
                    Method readAll = jdbcBean.selectMethod("readAll", String.class, Object[].class);
                    list = Collections
                            .unmodifiableList(
                                    (List<List<Object>>) readAll.invoke(
                                            jdbcUtil,
                                            replaceStr,
                                            objects
                                    )
                            );
                } else {
                    Method update = jdbcBean.selectMethod("update", String.class, Object[].class);
                    re = update.invoke(jdbcUtil, replaceStr, objects);
                }
                switch (returnType.getName()) {
                    case "java.lang.String":
                        assert list != null;
                        return list.get(0).get(0);
                    case "java.lang.Integer":
                        return Integer.parseInt(re.toString());
                    case "com.cacode.summerframework.summer.util.List":
                    case "java.util.List":
                        return list;
                    default:
                        Method columnNames = jdbcBean.selectMethod("columnNames", String.class);
                        List<String> invoke = (List<String>) columnNames.invoke(
                                returnType.getAnnotation(
                                        Entity.class
                                ).tabName()
                        );

                        Field[] declaredFields = returnType.getDeclaredFields();
                        for (Field declaredField : declaredFields) {
                            invoke.forEach((T) -> {
                                if (declaredField.getAnnotation(ParamType.class).key().equals(T)) {

                                }
                            });
                        }

                        if (returnType.getAnnotation(Entity.class) != null) {
                            return list.get(0).getClass();
                        }
                        break;
                }
            }
        }
        // 切出
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
            Constructor<? extends SummerBeanFactory> constructor = SummerBeanFactory.class.getConstructor(Class.class,
                    Class.class);
            summerBeanFactory = constructor.newInstance(proxyInterface, this.prop.getClass());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(summerBeanFactory).getBean();
    }
}