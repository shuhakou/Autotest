package cn.ce.api.util;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil{

    //定义正则表达式提取req_data-接口依赖${}数据
    private static String depParamPattern = "\\$\\{([a-z/]+):([a-z]+)\\}";

    //定义正则表达式提取req_data内定义的函数
    private static String funPattern  = "\\$\\{__(\\w+)(\\([\\w\\,]*\\))\\}";

    public static String buildParam(String reqData){
        //定义规则及规则执行
        Pattern p = Pattern.compile(funPattern);
        Matcher m = p.matcher(reqData);
        while (m.find()){
            String funName = m.group(1);
            String funParam = m.group(2).replace("(", "").replace(")", "");
            String value = FunctionUtil.getValue(funName, funParam.split(","));
            System.out.println("value=" + value);

            //替换值
            reqData = replaceFirst(reqData, m.group(), value);
        }
        return reqData;
    }

    /**
     * @Title: buildParam
     * @Description: 解决req_data接口依赖
     * @param: @param iNameAndIJsonToMapResult
     * @param: @param reqData
     * @param: @return
     * @return: String
     * @throws
     */
    public static String buildParam(Map<String,Map<String,String>> iNameAndIJsonToMapResult,String reqData){

        //定义规则及规则执行
        Pattern p = Pattern.compile(depParamPattern);
        Matcher m = p.matcher(reqData);
        while (m.find()){

            String reqDataIName = m.group(1);
            String reqDataIResKey = m.group(2);

            //Map中找reqDataIName=/public/lazyentrance的map格式值
            Map<String, String> iJsonToMapResult = iNameAndIJsonToMapResult.get(reqDataIName);

            //再从Map值中找reqDataIResKey=status的值
            String value = iJsonToMapResult.get(reqDataIResKey);

            //替换值
            reqData = replaceFirst(reqData, m.group(), value);
        }
        return reqData;
    }

    /**
     * @Title: replaceFirst
     * @Description: TODO
     * @param: @param sourceStr 待替换字符串
     * @param: @param matchStr 匹配字符串
     * @param: @param replaceStr 目标替换字符串
     * @param: @return
     * @return: String
     * @throws
     */
    public static String replaceFirst(String sourceStr,String matchStr,String replaceStr){

        int index = sourceStr.indexOf(matchStr);
        String beginStr = sourceStr.substring(0,index);

        int matLength = matchStr.length();
        int sourLength = sourceStr.length();
        String endStr = sourceStr.substring(index+matLength,sourLength);

        //重新拼接
        sourceStr = beginStr+replaceStr+endStr;
        return sourceStr;
    }


    /**
     * @Title: getFindstrCount
     * @Description: 计算字符串中子串出现的次数
     * @param: @param parentStr
     * @param: @param findStr
     * @param: @return
     * @return: int
     * @throws
     */
    public static int getFindstrCount(String parentStr,String findStr){
        //初始化值
        int count = 0;
        while(true){
            int index = parentStr.indexOf(findStr);
            if(index != -1){
                parentStr = parentStr.substring(index+findStr.length(), parentStr.length());
                count++;
            }else{
                break;
            }
        }
        return count;
    }

    /**
     * @Title: isNotEmpty
     * @Description: 判断str是否为非空
     * @param: @param str
     * @param: @return
     * @return: boolean
     * @throws
     */
    public static boolean isNotEmpty(String str) {
        return null != str && !"".equals(str);
    }

    /**
     * @Title: isEmpty
     * @Description: 判断str是否为空
     * @param: @param str
     * @param: @return
     * @return: boolean
     * @throws
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    public static void main(String[] args) {

//		String tmp = "";
//		String[] strArray = tmp.split(",");
//		System.out.println();
//		for(String str:strArray){
//			System.out.println(str);
//		}

        String reqData = "countdown=1&m=${__Random(,)}";
        System.out.println(buildParam(reqData));


//		String parentStr = "isajax=1&remember=${A=A1}&email=${A=A2}&password=${B=B1}&agreeterms=1&itype=&book=1&m=0.2757277030262314";
//		String findStr = "pp";
//		int count = getFindstrCount(parentStr,findStr);
//		System.out.println("共出现次数为：" + count);

        //System.out.println("countdown=${/public/lazyentrance:status}&m=0.809720557888405".indexOf("${/public/lazyentrance:status}"));
    }

}