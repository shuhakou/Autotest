package cn.ce.api.util;

public class Test2 {

    public static void main(String[] args) {
        ExcelUtil test = new ExcelUtil("D:\\autotest\\app\\form\\app_testcase.xlsx");
        //Object object = test.getCellValue(0, 1, 5);
        //System.out.println(object.toString());

        Object[][] object2 = test.getArrayCellValue(0);
        System.out.println(object2[0][3]);
        System.out.println(object2[1][8]);
    }

}
