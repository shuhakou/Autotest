package cn.ce.api.functions;
import cn.ce.api.util.DateTimeUtil;
import cn.ce.api.util.StringUtil;

/**
 * @ClassName:  TimeFunction
 * @Description:
 * @author:
 * @date:
 * @Version:v1.0
 */
public class TimeFunction implements Function{

    @Override
    public String execute(String[] args){
        String result = null;

        if(args.length == 0 || StringUtil.isEmpty(args[0])){
            //时间戳
            result = String.valueOf(DateTimeUtil.getTimeStamp());
        }else{
            //年月日时分秒
            result = DateTimeUtil.getDateTime();
        }
        return result;
    }

    @Override
    public String getReferenceKey() {
        return "Time";
    }

    public static void main(String[] args) {

    }

}
