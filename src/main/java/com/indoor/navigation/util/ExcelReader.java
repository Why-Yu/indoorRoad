package com.indoor.navigation.util;

import com.indoor.navigation.entity.database.ChangeVertex;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author YH
 * @Description
 * @Date 2021/7/14 21:08
 */
public class ExcelReader {
    public static ArrayList<ChangeVertex> read(String filePath, String buildId) throws IOException {
        //创建Excel，读取文件内容
        ArrayList<ChangeVertex> CVList= new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        //两种方式读取工作表
        // Sheet sheet=workbook.getSheet("Sheet0");
        Sheet sheet = workbook.getSheetAt(0);
        //获取sheet中最后一行行号
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            ChangeVertex CV = new ChangeVertex();
            Row row = sheet.getRow(i);
            //获取当前行最后单元格列号
            int lastCellNum = 3;
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if ( cell == null) {
                    CV.setUpGlobalIndex(null);
                } else {
                    if (j == 0 ) {
                        CV.setGlobalIndex(buildId + "-" + cell.getStringCellValue());
                    } else if ( j == 1 ) {
                        CV.setChangeType(ChangeType.valueOf(cell.getStringCellValue()));
                    } else {
                        CV.setUpGlobalIndex(buildId + "-" + cell.getStringCellValue());
                    }
                }
            }
            CV.setBuildId(buildId);
            CVList.add(CV);
        }
        return CVList;
    }

    public static void main(String[] args) {
        try {
            System.out.println(read("E:/TopoData/大洋百货/大洋百货.xlsx", "1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
