package cn.ce.api.util;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
public class PropsUtil {

    //日志初始化
    private static Logger logger = Logger.getLogger(PropsUtil.class);

    //声明配置文件
    private static  String[] confProps = {"config.properties"};
    private static PropertiesConfiguration conf = null;

    /**
     * 获取所有配置对象
     * @param
     * @return PropertiesConfiguration
     */
    public static PropertiesConfiguration getConf(){

        conf = new PropertiesConfiguration();//初始化对象

		/*把所有props配置文件一次加载,共后续使用*/
        for(int i=0;i<confProps.length;i++){
            try {
                conf.load(confProps[i]);
            } catch (ConfigurationException e) {
                conf = null;
                logger.error(e);}
        }

        return conf;
    }

    /*
     * PropsUtil getInt()
     * @param key
     * @return Integer
     */
    public static Integer getInt(String key){

        /**声明返回值**/
        Integer value = null;

        try{
			/*获取value值*/
            conf = getConf();
            value = conf.getInt(key);
        }catch(Exception e){
            logger.error("PropsUtil getInt" + e);
        }

        return value;
    }

    /*
     * PropsUtil getString()
     * @param key
     * @return String
     */
    public static String getString(String key){

        /**声明返回值**/
        String value = null;

        try{
			/*获取value值*/
            conf = getConf();
            value = conf.getString(key);
        }catch(Exception e){
            logger.error("PropsUtil getString" + e);
        }

        return value;
    }

    /*
     * PropsUtil getLong()
     * @param key
     * @return Long
     */
    public static Long getLong(String key){

        /**声明返回值**/
        Long value = null;

        try{
			/*获取value值*/
            conf = getConf();
            value = conf.getLong(key);
        }catch(Exception e){
            logger.error("PropsUtil getLong" + e);
        }

        return value;
    }

    /*
     * PropsUtil getBoolean()
     * @param key
     * @return boolean
     */
    public static boolean getBoolean(String key){

        /**声明返回值**/
        boolean value = true;

        try{
			/*获取value值*/
            conf = getConf();
            value = conf.getBoolean(key);
        }catch(Exception e){
            value = false;
            logger.error("PropsUtil getBoolean" + e);
        }

        return value;
    }

    /*
     * PropsUtil getList()
     * @param key
     * @return String
     */
    public static String[] getStringArray(String key){

        /**声明返回值**/
        String[] value = null;

        try{
			/*获取value值*/
            conf = getConf();
            value = conf.getStringArray(key);
        }catch(Exception e){
            logger.error("PropsUtil getStringArray" + e);
        }

        return value;
    }

    /*
     * PropsUtil getJsonHeaders()
     * @param key String
     * @return JSONObject
     */
    public static JSONObject getJsonHeaders(String key){

        JSONObject json = null;//声明返回值
        try{
            /**解析*.Properties文件**/
            conf = getConf();
            String value = conf.getString(key).replace("=", ",");
            json = JSON.parseObject(value);
        }catch(Exception e){
            logger.error("PropsUtil getJsonHeaders" + e);
        }

        return json;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(getString("dbcp.url"));

//		String[] tmp = getStringArray("Email_to");
//		for(String to:tmp){
//			System.out.println(to);
//		}

//		JSONObject json = getJsonHeaders("httpHead");
//		for(Entry<String, Object> entry:json.entrySet()){
//			System.out.println(entry.getKey() + "-----" +  entry.getValue().toString());
//		}

//		System.out.println(getInt("dbcp.url"));

//		System.out.println(getInt("redis.minIdle"));
//		String[] tmp = getStringArray("redis.redisSlaveIp");
//		for(int i=0;i<tmp.length;i++){
//			System.out.println(tmp[i].split(":")[0]);
//			System.out.println(tmp[i].split(":")[1]);
//		}
    }

}