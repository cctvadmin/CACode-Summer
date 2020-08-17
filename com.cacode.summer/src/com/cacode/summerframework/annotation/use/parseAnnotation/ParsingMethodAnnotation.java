package com.cacode.summerframework.annotation.use.parseAnnotation;

import com.cacode.summerframework.summer.util.List;
import com.cacode.summerframework.summer.util.Map;

/**
 * summer sql解析器
 *
 * @author CACode http://www.adminznh.ren
 * @version 1.0
 * @date 2020/7/14 6:45
 */
public class ParsingMethodAnnotation {
    /**
     * 原hillside语句
     */
    private String str;
    /**
     * 解析之后的字段名
     */
    private final List<String> args;
    /**
     * 排序后的参数值
     */
    private final List<Object> argsValue = new List<>();
    /**
     * 解析之后的sql语句
     */
    private String replaceStr;

    /**
     * @return 获取解析之后的所有字段d的值
     */
    public List<Object> getArgsValue() {
        return argsValue;
    }

    /**
     * @return 获取解析之后的所有字段
     */
    public List<String> getArgs() {
        return args;
    }

    /**
     * @return 获取解析之后的sql语句
     */
    public String getReplaceStr() {
        return replaceStr;
    }

    /**
     * @param str 传入需要解析的hillside
     */
    public ParsingMethodAnnotation(String str) {
        this.args = this.getArgs(str);
    }

    /**
     * 解析注解上的字段
     *
     * @param str 一整段字符串
     * @return 特殊语法$-*-$分割的字符串
     */
    private List<String> getArgs(String str) {
        List<String> field = new List<>();
        String[] split = str.split("\\$*\\$");
        for (String s : split) {
            String pars = pars(s);
            if (pars != null) {
                field.add(pars);
            }
        }
        this.replaceStr = str.replaceAll("\\$.*?\\$", "?");
        return field;
    }

    /**
     * 过滤器，一次过滤一次
     *
     * @param text 要过滤的文本
     * @return 特殊语句中间的字段
     */
    private String pars(String text) {
        int start = -1;
        int end = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.toCharArray()[i] == '-') {
                if (start == -1) {
                    start = i;
                } else {
                    end = i;
                    break;
                }
            }
        }
        if (start != -1 && end != -1) {
            return text.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 将排序好的字段与键值对排序
     */
    public void sort(Map<String, Object> map) {
        for (String item : this.getArgs()) {
            this.argsValue.add(map.get(item));
        }
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
