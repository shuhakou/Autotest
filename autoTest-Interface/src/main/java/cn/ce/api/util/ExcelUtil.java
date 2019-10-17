package cn.ce.api.util;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @ClassName:  ExcelUtil
 * @Description: java代码操作excel相关方法
 * @author:
 * @date:
 * @Copyright:
 */
public class ExcelUtil {

    /**
     * 初始化filePath,代表excel文件路径
     */
    private static String filePath = null;
    private static Map<String,String> excelConfig = new ParseXmlUtil().getExcelConfig();
    private final static String EXCEL_TYPE = excelConfig.get("excelType");

    /**
     * @Title:  ExcelUtil
     * @Description: 构造方法对类的filePath属性初始化
     * @param:  @param filePath
     * @throws
     */
    public ExcelUtil(String filePath){
        ExcelUtil.filePath = filePath;
    }

    /**
     * @Title: getWb
     * @Description: 得到Workbook对象
     * @param: @return
     * @return: Workbook
     * @throws
     */
    public static Workbook getWb(){
        Workbook wb = null;
        try{
            InputStream input = new FileInputStream(filePath);
            if(filePath.endsWith(EXCEL_TYPE)&&EXCEL_TYPE.equals("xlsx")){
                wb = new XSSFWorkbook(input);
            }else{
                wb = new HSSFWorkbook(input);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * @Title: getSheet
     * @Description: 得到Sheet对象
     * @param: @param index
     * @param: @return
     * @return: Sheet
     * @throws
     */
    private static Sheet getSheet(int index){
        Sheet sheet = null;
        if(getWb()!=null){
            //得到指定下标的Sheet
            sheet = getWb().getSheetAt(index);
        }
        return sheet;
    }

    /**
     * @Title: getCellValueFromCellType
     * @Description: 根据CellType得到不同value
     * @param: @param cell
     * @param: @return
     * @return: Object
     * @throws
     */
    public static Object fromCellTypeGetCellValue(Cell cell){
        Object value = null;

        try{
            //根据CellType得到不同value
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                value = cell.getNumericCellValue();
            }else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                value = cell.getStringCellValue();
            }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                value = cell.getCellFormula();
            }else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
                value = "";
            }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
                value = cell.getBooleanCellValue();
            }else if(cell.getCellType() == Cell.CELL_TYPE_ERROR){
                value = cell.getErrorCellValue();
            }else{
                value = cell.getDateCellValue();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return value;
    }

    /**
     * @Title: getCellValue
     * @Description: 得到单元格value
     * @param: @return
     * @return: Object
     * @throws
     */
    public Object getCellValue(int rowNum,int cellNum){
        Object value = null;

        if(getSheet(0)!=null){
            Row row = getSheet(0).getRow(rowNum);
            Cell cell = row.getCell(cellNum);
            value = fromCellTypeGetCellValue(cell);
        }
        return value;
    }

    /**
     * @Title: isBlankRow
     * @Description: 判断是否为空行
     * @param: @return
     * @return: boolean
     * @throws
     */
    public boolean isBlankRow(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i <row.getLastCellNum() ; i++) {
            Cell cell = row.getCell(i);
            if (cell!=null&&cell.getCellType()!= Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Title: getArrayCellValue
     * @Description: 把excel内用例存储到Object[][]中
     * @param: @param sheetIndex
     * @param: @return
     * @return: Object[][]
     * @throws
     */
    public  Object[][] getArrayCellValue(int sheetIndex){
        //初始化返回值
        Object[][] testCaseData = null;
        //用例总行数的下标(包含空行)
        int totalRowIndex = getSheet(sheetIndex).getLastRowNum();
        int blankRow=0;
        Integer excelColumns = Integer.valueOf(excelConfig.get("excelColumns"));
        testCaseData  = new Object[totalRowIndex][excelColumns];

        for(int rowIndex = 1,resultRowIndex=1; rowIndex <= totalRowIndex; rowIndex++,resultRowIndex++){
            //通过sheet指定到rowIndex那行
            Row row = getSheet(sheetIndex).getRow(rowIndex);
            //如果是空行，则跳过此行，读取下一行数据，但是空行的数据不存入返回结果：Object[][]
            if(isBlankRow(row)){
                resultRowIndex--;
                blankRow++;
                continue;
            }

            //指定到行后,遍历每列值
            for(int cellIndex = 0;cellIndex < row.getLastCellNum();cellIndex++){
                Cell cell = row.getCell(cellIndex);
                if(cell == null){
                    testCaseData[resultRowIndex -1][cellIndex]="";
                }else{
                    testCaseData[resultRowIndex -1][cellIndex] = fromCellTypeGetCellValue(cell);
                }
            }
        }
        //返回没有空行的结果
        Object[][] resultData = new Object[totalRowIndex-blankRow][excelColumns];
        System.arraycopy(testCaseData,0,resultData,0,resultData.length);
        return resultData;
    }

    public static void main(String[] args) {
//		String filePath = "D:\\autotest\\app\\form\\app_testcase.xlsx";
//		ExcelUtil excelUtil = new ExcelUtil(filePath);
//
//		//单个单元格取值
//		//System.out.println(excelUtil.getCellValue(2,3).toString());
//
//		//二维数组取值
//		Object[][] tmp = excelUtil.getArrayCellValue(0);
//		System.out.println(tmp[2][5]);
    }


}