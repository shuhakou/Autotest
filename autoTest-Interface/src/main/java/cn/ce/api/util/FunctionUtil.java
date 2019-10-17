package cn.ce.api.util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.ce.api.functions.Function;

public class FunctionUtil {
    /**
     * 初始化functionsMap
     */
    private static final Map<String, Class<? extends Function>> functionsMap = new HashMap<String, Class<? extends Function>>();

    static{
        List<Class<?>> clazzes = ClassFinderUtil.getAllAssignedClass(Function.class);
        for(Class<?> clazz:clazzes){
            try{
                //对类进行实例化,并进行上传
                Function tempFunc = (Function) clazz.newInstance();
                String referenceKey = tempFunc.getReferenceKey();

                if (referenceKey.length() > 0) {
                    functionsMap.put(referenceKey, tempFunc.getClass());
                    //Random-->RandomDecimalFunction
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isFunction(String functionName){
        return functionsMap.containsKey(functionName);
    }

    public static String getValue(String functionName,String[] args){
        try{
            return functionsMap.get(functionName).newInstance().execute(args);
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String tmp = "()";
        System.out.println(getValue("Random",tmp.split(",")));
    }

}
