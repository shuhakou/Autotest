package cn.ce.api.service.Listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
/*
* 第二种实现方案
* */
import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;
    private static String OUTPUT_DIR = "test-output/";

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance("index.html");

        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        File file = new File(OUTPUT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = OUTPUT_DIR + fileName;
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(fileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setReportName(fileName);
        htmlReporter.config().setDocumentTitle("ExtentReports - Created by ExtentReport");
        htmlReporter.config().setReportName("ExtentReports - Created by ExtentReport");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        return extent;
    }
}
