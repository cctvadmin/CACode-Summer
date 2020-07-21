

package com.cacode.summerframework.annotation.use.parseAnnotation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * 类相关的工具类
 * <p>
 * from github:https://github.com/cctvadmin/pkg-scanner
 *
 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
 */
public class ClassUtil {
    /**
     * 取得某个接口下所有具有指定注解的类
     *
     * @param reference  参考类
     * @param annotation 注解
     * @return 具有指定注解的类
     */
    public java.util.List<Class<?>> getClassesByAnnotation(Class<?> reference, Class<?> annotation) {
        java.util.List<Class<?>> returnClassList = new ArrayList<>();
        java.util.List<Class<?>> classes = getClasses(reference.getPackageName());
        for (Class<?> item : classes) {
            if (item.isInterface()) {
                Annotation[] annotations = item.getAnnotations();
                if (annotations.length != 0) {
                    for (Annotation itemAnnotation : annotations) {
                        if (itemAnnotation.annotationType().equals(annotation)) {
                            returnClassList.add(item);
                        }
                    }
                }
            }
        }
        return returnClassList;
    }

    /**
     * 从包中获取所有的Class
     *
     * @param packageName 包名
     * @return 所有类
     */
    public java.util.List<Class<?>> getClasses(String packageName) {
        //第一个class类的集合
        java.util.List<Class<?>> classes = new ArrayList<>(5);
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    java.util.List<Class<?>> andAddClassesInPackageByFile = findAndAddClassesInPackageByFile(packageName, filePath, true);
                    if (andAddClassesInPackageByFile != null && andAddClassesInPackageByFile.size() != 0) {
                        classes.addAll(andAddClassesInPackageByFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param recursive   是否递归
     * @return 所有的类
     */
    private static java.util.List<Class<?>> findAndAddClassesInPackageByFile(String packageName,
                                                                             String packagePath,
                                                                             final boolean recursive
    ) {
        java.util.List<Class<?>> classes = null;
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        classes = new ArrayList<>();
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                List<Class<?>> andAddClassesInPackageByFile = findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive);
                if (andAddClassesInPackageByFile != null && andAddClassesInPackageByFile.size() != 0) {
                    classes.addAll(andAddClassesInPackageByFile);
                }
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}