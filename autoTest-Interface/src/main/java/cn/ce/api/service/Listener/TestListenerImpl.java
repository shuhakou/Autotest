package cn.ce.api.service.Listener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListenerImpl implements ITestListener {

    //step3  存放-重复的ITestResult
    ArrayList<ITestResult> testsRepeatToBeRemoved = new ArrayList<ITestResult>();

    @Override
    public void onFinish(ITestContext context) {

        //Step1遍历得到成功的用例结果
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTestResult : context.getPassedTests().getAllResults()) {
            int passHashCodeId = getHashCode(passedTestResult);
            passedTestIds.add(passHashCodeId);
//        	System.out.println("成功用例结果hashcode--》" + passHashCodeId);
//        	System.out.println("成功用例结果：" + passedTestResult);
        }

        //Step1遍历得到失败的用例结果
        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTestResult : context.getFailedTests().getAllResults()) {
            int failHashCodeId = getHashCode(failedTestResult);

//        	System.out.println("失败用例结果hashcode--》" + failHashCodeId);
//        	System.out.println("失败用例结果：" + failedTestResult);

            //step3 在失败用例结果集中筛选出重复的
            if (failedTestIds.contains(failHashCodeId) || passedTestIds.contains(failHashCodeId)) {
                testsRepeatToBeRemoved.add(failedTestResult);
            } else {
                failedTestIds.add(failHashCodeId);
            }
        }

        //step4 从context.getFailedTests()中删除testsRepeatToBeRemoved记录重复的
        Iterator<ITestResult> TestResultIterator = context.getFailedTests().getAllResults().iterator();
        while(TestResultIterator.hasNext()){
            ITestResult testResult = TestResultIterator.next();
            if (testsRepeatToBeRemoved.contains(testResult)) {
                TestResultIterator.remove();
            }
        }

    }

    /*
     * step2
     * 每条ITestResult转化为hashcode
     * id = hashCode(class) +  hashCode(method) + hashCode(dataprovider)=>唯一
     */
    private int getHashCode(ITestResult testResult) {
        //类名hashcode
        int id = testResult.getTestClass().getName().hashCode();

        //类名hashcode+方法名hashcode
        id = id + testResult.getMethod().getMethodName().hashCode();

        //类名hashcode+方法名hashcode+请求参数hashcode
        id = id + (testResult.getParameters() != null ? Arrays.hashCode(testResult.getParameters()) : 0);

        return id;
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }
}