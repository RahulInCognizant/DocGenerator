package com.sales.docGenerator.service;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Rahul Roy on 12/18/2018.
 */
@Service
public class SpreadSheetCreator {

    public byte[] writeSpreadSheet(String data) throws IOException {

        HSSFWorkbook book = HSSFWorkbookFactory.createWorkbook();
        HSSFSheet sheet = book.createSheet();
        HSSFRow row = sheet.createRow(50);
        String [] dataSplit = data.split(" ");
        for(int i =0; i< dataSplit.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(dataSplit[i]);
        }

        String tempFileName = Math.random() + ".xls";
        File file = new File(tempFileName);

        book.write(file);
        byte[] fileByte = Files.readAllBytes(file.toPath());

        //deleting file
        file.delete();

        return  fileByte;

    }
}
