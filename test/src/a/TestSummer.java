package a;

import com.cacode.summerframework.annotation.fieldannotations.Autoinit;
import com.cacode.summerframework.annotation.use.parseautoinit.BaseAuto;
import com.cacode.summerframework.summer.SummerBeanFactory;

import java.util.List;


/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/22 7:07
 */
public class TestSummer extends BaseAuto {
    @Override
    public Object getThisSon() {
        return this;
    }

    @Autoinit(consClass = Properties.class)
    public SummerBeanFactory summerBeanFactory;

    public void a() {
        InterfaceTest bean = (InterfaceTest) summerBeanFactory.getBean(InterfaceTest.class);
        List<Info> info = bean.findInfo(2);
        System.out.println(info.toString());
        int i = bean.addTitle(2);
        System.out.println(i);
        List<Info> info_ = bean.findInfo(2);
        System.out.println(info_.toString());
    }
}
