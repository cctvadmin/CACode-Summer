package a;

import com.cacode.summerframework.annotation.classannotations.SummerScan;
import com.cacode.summerframework.annotation.fieldannotations.Param;
import com.cacode.summerframework.annotation.methodannotations.Insert;
import com.cacode.summerframework.annotation.methodannotations.Select;

import java.util.List;

/**
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/22 14:08
 */
@SummerScan
public interface InterfaceTest {
    @Select(sql = "SELECT * FROM info WHERE title=$-title-$")
    List<Info> findInfo(@Param(name = "title") int title);

    @Insert(sql = "INSERT INTO info VALUES($-title-$)")
    int addTitle(@Param(name = "title") int title);
}
