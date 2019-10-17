package cn.ce.api.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.Class;
import cn.ce.api.functions.Function;


public class ClassFinderUtil{

    //存储实例化类对象
    private static List<Class<?>> classesInstance = new ArrayList<Class<?>>();

    /**
     * @Title: getAllAssignedClass
     * @Description: 得到类的子类对象
     * @param: @param cls
     * @param: @return
     * @return: List<Class<?>>
     * @throws
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * @Title: getClasses
     * @Description: 同一路径下得到所有类的实例化对象
     * @param: @param cls
     * @param: @return
     * @return: List<Class<?>>
     * @throws
     */
    public static List<Class<?>> getClasses(Class<?> cls){
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        try{
            String dirPath = cls.getClassLoader().getResource(path).getPath();
            classes = getClasses(new File(dirPath),pk);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * @Title: getClasses
     * @Description: 同一路径下得到所有类的实例化对象
     * @param: @param dir
     * @param: @param pk
     * @param: @return
     * @return: List<Class<?>>
     * @throws
     */
    private static List<Class<?>> getClasses(File dir, String pk){
        for (File f : dir.listFiles()){
            if (f.isDirectory()){
                getClasses(f, pk + "." + f.getName());
            }

            String name = f.getName();
            if (name.endsWith(".class")){
                try{
                    classesInstance.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return classesInstance;
    }

    public static void main(String[] args){

        List<Class<?>> tmp = getClasses(Function.class);
        for(Class<?> clazz:tmp){
            System.out.println(clazz.toString());
        }

        System.out.println("---------------------------");

        List<Class<?>> tmp2 = getAllAssignedClass(Function.class);
        for(Class<?> clazz2:tmp2){
            System.out.println(clazz2.toString());
        }

    }

}