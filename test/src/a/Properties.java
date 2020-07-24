package a;

import com.cacode.summerframework.summer.SummerProp;
import com.cacode.summerframework.summer.util.Map;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/22 7:08
 */
public class Properties implements SummerProp {
    @Override
    public void before() {

    }

    @Override
    public Map<String, String> prop(Map<String, String> properties) {
        properties.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        properties.put("url", "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC");
        properties.put("username", "root");
        properties.put("password", "123456");
        return properties;
    }

    @Override
    public void after() {

    }
}
