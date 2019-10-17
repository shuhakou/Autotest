package cn.ce.api.test;

import cn.ce.api.enums.EmailTypeEnum;
import cn.ce.api.enums.ResultEnum;
import cn.ce.api.exception.ResultException;
import cn.ce.api.util.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.Reporter;
import cn.ce.api.model.AutoLog;

import javax.mail.MessagingException;

public class TestRun {

    private String filePath = null;
    @BeforeTest
    public void initXmlConfig() {
        //得到配置文件路径
        String configPath = this.getClass().getResource("/api-config.xml").getFile();
        new ParseXmlUtil(configPath);
        System.out.println("test 1");
    }



    @BeforeTest
    @Parameters({"filePathParam"})
    public void beforeTest(String filePathParam) {
        System.out.println("test 2");
        this.filePath = filePathParam;
    }

    @Test(dataProvider = "testCaseData")
    public void httpReq(String id, String isExec, String testCase, String reqType, String reqHost, String reqInterface, String reqData, String expResult, String isDep, String depKey) {

        //初始化
        String actResult = "";
        String reqUrl = reqHost + reqInterface;

        //reqData-接口依赖处理
        reqData = StringUtil.buildParam(ParseJsonToMapUtil.getINameAndIMap(), reqData);

        //reqData-参数动态处理
        reqData = StringUtil.buildParam(reqData);

        //打印用例相关信息
        Reporter.log(ResultEnum.REQUEST_URL.getMsg() + reqUrl);
        Reporter.log(ResultEnum.REQUEST_TYPE.getMsg() + reqType);
        Reporter.log(ResultEnum.REQUEST_PARAM.getMsg() + reqData);
        Reporter.log(ResultEnum.EXPECTED_RESULT.getMsg() + expResult);


        //执行用例
        if ("YES".equals(isExec)) {
            if ("GET".equals(reqType)) {
                //发送get请求
                actResult = HttpReqUtil.sendGet(reqUrl, reqData);
            } else if ("POST".equals(reqType)) {
                //发送post请求
                actResult = HttpReqUtil.sendPost(reqUrl, reqData);
            } else if ("PUT".equals(reqType)) {
                //发送put请求
                actResult = HttpReqUtil.sendPut(reqUrl, reqData);
            } else {
                //发送delete请求
                actResult = HttpReqUtil.sendDelete(reqUrl, reqData);
            }
        } else {
            Reporter.log(ResultEnum.NOT_EXEC.getMsg());
            return;
        }

        //打印日志
        Reporter.log(ResultEnum.ACTUAL_RESULT.getMsg() + actResult);

        //接口被依赖,把接口服务器返回值转化为Map进行存储
        if ("YES".equals(isDep)) {
            Reporter.log(ResultEnum.IS_DEPEND.getMsg() + isDep);
            new ParseJsonToMapUtil().setINameAndIMap(reqInterface, actResult);
        }


        //预期值和实际值 Assert
        //实际结果为空，给出提示信息
        if (actResult == null) {
            throw new ResultException(ResultEnum.ACTUAL_RESULT_NULL);
        }
        //实际结果可能是json数据也可能是流数据（图片流等）
        boolean isJsonString = new ParseJsonToMapUtil().isJsonString(actResult);
        if (isJsonString) {
            Map<String, String> actResultMap = new ParseJsonToMapUtil().parseJsonToMap(actResult);
            Map<String, String> expectedResultMap = new ParseJsonToMapUtil().parseJsonToMap(expResult);
            //预期结果输入为空，给出提示信息
            if (expectedResultMap.isEmpty()) {
                throw new ResultException(ResultEnum.EXPECT_FORMAT_ERROR);
            }
            //预期结果没有实现全量对比，给出提示信息
            if (expectedResultMap.size() != actResultMap.size()) {
                Reporter.log(ResultEnum.UNREALIZED_FULL_COMPARISON.getMsg());
            }
            for (String key : expectedResultMap.keySet()) {
                //预期结果比实际结果多了某个字段，给出提示信息
                if (!actResultMap.containsKey(key)) {
                    throw new ResultException(ResultEnum.ACTUAL_FIELD_NULL.getCode(), ResultEnum.ACTUAL_FIELD_NULL.getMsg() + key);
                }
                //预期结果和实际结果某个字段不相等，给出该字段提示信息
                if (!actResultMap.get(key).equals(expectedResultMap.get(key))) {
                    throw new ResultException(ResultEnum.ACT_ISNOT_EXP.getCode(), ResultEnum.ACT_ISNOT_EXP.getMsg() + key);
                }
                Assert.assertEquals(actResultMap.get(key),expectedResultMap.get(key));
            }

        } else {
            //响应是图片流等场景时，只需判断实际结果是否包含文本流等头部信息
            Reporter.log(ResultEnum.UNREALIZED_FULL_COMPARISON.getMsg());
            Assert.assertTrue(!Objects.equals(expResult,"")&&actResult.contains(expResult));
        }

    }

    @DataProvider(name = "testCaseData")
    public Object[][] dp() {

        //初始化返回值
        Object[][] data = null;
        try {
            ExcelUtil excelUtil = new ExcelUtil(filePath);
            data = excelUtil.getArrayCellValue(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @AfterTest
    public void afterTest(){
        //测试结果入库
        //DbcpUtil.dbcpBatchUpdate(list);

    }

    @AfterSuite
    public void afterSuite() throws MessagingException {
        //发送邮件
        EmailUtil emailUtil = new EmailUtil();
        //TODO
        // 当前报文件找不到的错误，怎样才能先生成报告，再自动发送邮件？？？
        //emailUtil.send("1.5","测试完成了", EmailTypeEnum.ATTACHMENT);
    }

}