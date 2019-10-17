package cn.ce.api.service.Listener;

import cn.ce.api.enums.ResultEnum;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtentTestNGIReporterListener implements IReporter {
    private static final String OUTPUT_FOLDER = "test-output/";
    private static final String FILE_NAME = "index.html";

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();


        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            ExtentTest extentTest = null;


            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                //如果xml中的test不只一个，那么创建父节点，并把它们归属到同一类（类名使用xml中配置的test-name）
                if (result.size() > 1) {
                    extentTest = extent.createTest(context.getName())
                            .assignCategory(context.getName());


                    extentTest.getModel().setStartTime(context.getStartDate());
                    extentTest.getModel().setEndTime(context.getEndDate());
                    //统计该节点下的成功，失败，跳过用例数
                    int failCount = context.getFailedTests().size();
                    int skipCount = context.getSkippedTests().size();
                    int passCount = context.getPassedTests().size();
                    extentTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", passCount, failCount, skipCount));
                }

                buildTestNodes(extentTest, context.getFailedTests(), Status.FAIL);
                buildTestNodes(extentTest, context.getPassedTests(), Status.PASS);
                buildTestNodes(extentTest, context.getSkippedTests(), Status.SKIP);

            }
        }

        extent.flush();
    }

    private void init() {
        File file = new File(OUTPUT_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setDocumentTitle("ExtentReports - Created by ExtentReport");
        htmlReporter.config().setReportName("ExtentReports - Created by ExtentReport");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setEncoding("gbk");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }

    private void buildTestNodes(ExtentTest extentTest, IResultMap tests, Status status) {
        ExtentTest test;

        if (tests.size() > 0) {
            Set<ITestResult> treeSet = new TreeSet<>(new Comparator<ITestResult>() {
                @Override
                public int compare(ITestResult o1, ITestResult o2) {
                    return (int) (o1.getStartMillis()-o2.getStartMillis());
                }
            });
            treeSet.addAll(tests.getAllResults());
            for (ITestResult result : treeSet) {
                Object[] parameters = result.getParameters();
                if (extentTest == null) {
                    test = extent.createTest(parameters[2].toString());
                } else {
                    test = extentTest.createNode(parameters[2].toString());
                    test.assignCategory(extentTest.getModel().getCategory(0).getName());

                }

                List<String> output = Reporter.getOutput(result);
                boolean isExec = true;
                boolean fullValid = true;
                for (String s : output) {
                    if (s.equals(ResultEnum.NOT_EXEC.getMsg())) {
                        isExec = false;
                        test.getModel().setStatus(Status.SKIP);
                    }
                    else if (s.equals(ResultEnum.UNREALIZED_FULL_COMPARISON.getMsg())) {
                        fullValid = false;
                        test.getModel().setStatus(Status.WARNING);
                    } else {
                        test.debug(s);
                    }
                }

                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                } else {
                    if (!isExec) {
                        test.log(Status.SKIP, ResultEnum.NOT_EXEC.getMsg());
                    } else {
                        if (!fullValid) {
                            Markup m = MarkupHelper.createLabel(ResultEnum.UNREALIZED_FULL_COMPARISON.getMsg(), ExtentColor.ORANGE);
                            test.warning(m);
                            //test.warning(ResultEnum.SIZE_NOT_SAME.getMsg());
                        }
                        test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                    }
                }
                //如果是note节点，开始时间设置无用。note节点显示的时间为结束时间，差值为note节点结束时间-父节点结束时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
