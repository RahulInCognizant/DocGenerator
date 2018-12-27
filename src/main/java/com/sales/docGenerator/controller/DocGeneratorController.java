package com.sales.docGenerator.controller;

import com.sales.docGenerator.service.PDFCreatorService;
import com.sales.docGenerator.service.SpreadSheetCreator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Rahul Roy on 12/17/2018.
 */

@RestController
public class DocGeneratorController {


    @Autowired
    PDFCreatorService pdfService;

    @Autowired
    SpreadSheetCreator spreadSheetService;

    @GetMapping(value = "/pdf")
    public ResponseEntity<byte[]> getPDF() throws IOException, DocumentException, com.itextpdf.text.DocumentException {
        //return pdfService.getPDF();
        //byte[] content =   pdfService.writePDFUsingPDFBox("Hi Dear, How are you Hi Dear, How are you Hi Dear, How are you Hi Dear, How are you Hi Dear, How are you Hi Dear, How are you Hi Dear, How are you vvv Hi Dear, How are youvHi Dear, How are you");

        byte[] content =   pdfService.getPDF();


        System.out.println(content);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("Application/PDF"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "PDF.pdf" + "\"")
                .body(content);


    }

    @GetMapping(value = "/spreadsheet")
    public ResponseEntity<byte[]> getSpreadSheet() throws IOException {

        //return pdfService.getPDF();
       byte[] content = spreadSheetService.writeSpreadSheet("Hi Dear, How are you Hi Dear");



        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "spreadsheet.xls" + "\"")
                .body(content);

    }

}
