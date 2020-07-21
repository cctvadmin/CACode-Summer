package com.cacode.summerframework.summer;

import com.cacode.summerframework.summer.util.Map;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/22 2:45
 */
public interface SummerProp {
    /**
     * 切入头
     */
    void before();

    /**
     * 在这里设置你的配置文件
     * <p>
     * 以键值对方式存储，如：
     * <pre>
     * map.put("url", "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC");
     * map.put("driverClassName", "com.mysql.cj.jdbc.Driver");
     * map.put("username", "root");
     * map.put("password", "123456");
     *
     * JdbcUtil jdbcutil = new JdbcUtil(map);
     * jdbcutil.getDs();
     * List<List<Object>> info = jdbcutil.read("info");
     * System.out.println(info);
     * </pre>
     *
     * @param properties 配置文件初始
     * @return 配置文件
     */
    Map<String, String> prop(Map<String, String> properties);

    /**
     * 切入尾
     */
    void after();
}
