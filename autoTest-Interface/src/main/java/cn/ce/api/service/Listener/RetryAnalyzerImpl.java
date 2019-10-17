package cn.ce.api.service.Listener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer{
    //retry初始值,最大值
    private int retryCount = 0;
    private int retryMaxCount = 1;

    /**
     * true if the test method has to be retried, false otherwise.
     */
    @Override
    public boolean retry(ITestResult result){
        boolean flag = false;

        if(retryCount < retryMaxCount){
            retryCount++;
            flag = true;
            System.out.println("The current  method is re executed " + (retryCount) + " times");
        }else{
            resetRetryCount();
        }
        return flag;
    }

    /*
     * 重置retryCount
     */
    public void resetRetryCount() {
        retryCount = 0;
    }

}