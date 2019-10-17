package cn.ce.api.util;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpReqUtil {

    /**
     * 初始化
     */
    private static BasicCookieStore cookieStore = new BasicCookieStore();
    private static Map<String,String> headerConfig = new ParseXmlUtil().getHeaderConfig();
    private static Map<String,String> reqConfig = new ParseXmlUtil().getReqConfig();

    /**
     * @Title: httpReqConfig
     * @Description: 发送请求配置
     * @param: @param httpRequestBase
     * @return: void
     * @throws
     */
    public static void httpReqConfig(HttpRequestBase httpRequestBase){

        //header配置
        for(Entry<String, String> entry:headerConfig.entrySet()){
            httpRequestBase.setHeader(entry.getKey(),entry.getValue());
        }

        //请求超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(Integer.valueOf(reqConfig.get("reqTimeout")))
                .build();
        httpRequestBase.setConfig(config);
    }

    public static String sendGet(String url,String param){
        //初始化
        String result = null;
        CloseableHttpResponse response = null;
        String finalUrl = url + "?" + param;

        //创建httpclient
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        try{
            try{
                //添加header信息,发送get请求，
                if(param == null ||"".equals(param)){
                    finalUrl = url;
                }else {
                    finalUrl = url + "?" + param;
                }
                HttpGet httpGet = new HttpGet(finalUrl);

                httpReqConfig(httpGet);

                response = httpclient.execute(httpGet);

                //获取响应内容
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }else{
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭数据流
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally{
            try {
                //关闭http请求
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String sendDelete(String url,String param){
        //初始化
        String result = null;
        CloseableHttpResponse response = null;
        String finalUrl = url;

        //创建httpclient
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        try{
            try{
                //添加header信息,发送delete请求，
                if(param == null ||"".equals(param)){
                    finalUrl = url;
                }else {
                    finalUrl = url;
                }
                HttpDelete httpDelete = new HttpDelete(finalUrl);

                httpReqConfig(httpDelete);

                response = httpclient.execute(httpDelete);

                //获取响应内容
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }else{
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭数据流
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally{
            try {
                //关闭http请求
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    //	public static String sendDeleteWithParam(String url,String param) {
//		//初始化
//		String result = null;
//		CloseableHttpResponse response = null;
//		HttpDeleteWithBody httpDeleteWithBody = null;
//
//		//创建httpclient
//		CloseableHttpClient httpclient = HttpClients.custom()
//				.setDefaultCookieStore(cookieStore)
//				.build();
//
//		try {
//			try {
//				//添加header信息,创建HttpPost对象
//				HttpDelete httpDeleteWithParam = new HttpDelete(url);
//				httpReqConfig(httpDeleteWithParam);
//
//
//				//创建HttpEntity
//				StringEntity stringEntity = new StringEntity(param, "UTF-8");
//
//				if (new ParseJsonToMapUtil().isJsonString(param)) {
//					//请求json格式参数
//					stringEntity.setContentType(reqConfig.get("reqContentTypeJson"));
//				} else {
//					//请求form格式参数
//					stringEntity.setContentType(reqConfig.get("reqContentTypeForm"));
//				}
//				System.out.println("Param: " + stringEntity);
//				//发送delete请求
//				httpDeleteWithParam.setEntity(StringEntity);
//				System.out.println("test: " + httpDeleteWithParam);
//				response = httpclient.execute(httpDeleteWithParam);
//
//				//获取响应内容
//				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//					HttpEntity entity = response.getEntity();
//					result = EntityUtils.toString(entity);
//				} else {
//					HttpEntity entity = response.getEntity();
//					result = EntityUtils.toString(entity);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					//关闭数据流
//					response.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} finally {
//			try {
//				httpclient.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
    public static String sendPost(String url,String param){
        //初始化
        String result = null;
        CloseableHttpResponse response = null;

        //创建httpclient
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        try{
            try {
                //添加header信息,创建HttpPost对象
                HttpPost httpPost = new HttpPost(url);
                httpReqConfig(httpPost);

                //创建HttpEntity
                StringEntity stringEntity = new StringEntity(param,"UTF-8");

                if(new ParseJsonToMapUtil().isJsonString(param)){
                    //请求json格式参数
                    stringEntity.setContentType(reqConfig.get("reqContentTypeJson"));
                }else{
                    //请求form格式参数
                    stringEntity.setContentType(reqConfig.get("reqContentTypeForm"));
                }
                System.out.println("Param: " + stringEntity);
                //发送post请求
                httpPost.setEntity(stringEntity);
                System.out.println("test: " + httpPost);
                response = httpclient.execute(httpPost);

                //获取响应内容
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }else{
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭数据流
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally{
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * @param url
     * @param param
     * @return
     */
    public static String sendPut(String url,String param){
        //初始化
        String result = null;
        CloseableHttpResponse response = null;

        //创建httpclient
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        try{
            try {
                //添加header信息,创建HttpPut对象
                HttpPut httpPut = new HttpPut(url);
                httpReqConfig(httpPut);

                //创建HttpEntity
                StringEntity stringEntity = new StringEntity(param,"UTF-8");

                if(new ParseJsonToMapUtil().isJsonString(param)){
                    //请求json格式参数
                    stringEntity.setContentType(reqConfig.get("reqContentTypeJson"));
                }else{
                    //请求form格式参数
                    stringEntity.setContentType(reqConfig.get("reqContentTypeForm"));
                }

                //发送put请求
                httpPut.setEntity(stringEntity);
                response = httpclient.execute(httpPut);

                //获取响应内容
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }else{
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭数据流
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally{
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void main(String[] args) {
        //案例1
        String url_a = "http://www.nuandao.com/public/lazyentrance";
        String param_a = "isajax=1&remember=1&email=1477222170@qq.com&password=testing1?&agreeterms=1&itype=&book=1&m=0.528903239047282";
        String tmp = sendPost(url_a,param_a);
        System.out.println(tmp);

        //案例2
        String url_b = "http://www.nuandao.com/shopping/cart";
        String param_b = "countdown=1&m=0.3785440780894379";
        String tmp2 = sendPost(url_b,param_b);
        System.out.println(tmp2);
    }

}