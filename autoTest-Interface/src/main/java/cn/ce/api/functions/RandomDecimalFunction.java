package cn.ce.api.functions;
import cn.ce.api.util.StringUtil;

public class RandomDecimalFunction implements Function{

    @Override
    public String execute(String[] args){
        //初始化返回值
        String result = null;

        if(args.length == 0 || StringUtil.isEmpty(args[0])){
            double d = Math.random();
            result = String.valueOf(d);
        }
        return result;
    }

    @Override
    public String getReferenceKey() {
        //excel函数表达式写法为${__Random()}
        return "Random";
    }

    public static void main(String[] args){

    }

}