package com.sales.docGenerator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDFontSetting;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

/**
 * Created by Rahul Roy on 12/17/2018.
 */

@Service
public class PDFCreatorService {


    public byte[] getPDF() throws IOException, DocumentException {

        //creating a temporary file
        String filename = new Date().getTime()+".pdf";
        File file = new File(filename);

        Document document = new Document(PageSize.A3);


        writePDF(document, file);

        //Conversion in byte array
        byte[] fileByte=Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        //deleting the temporary file
        file.delete();

        return fileByte;
    }


    private void writePDF(Document document, File file) throws FileNotFoundException, DocumentException {

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            writePages(document);
            document.close();

    }

    public void writePages(Document document) throws DocumentException {
       writeFirstPage(document);
       //write other pages
        writeSecondPage(document);
    }

    private void writeSecondPage(Document document) {

    }

    private void writeFirstPage(Document document) throws DocumentException {
    //write dates

        Font dateFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLUE);
        Font introFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font tableFont = FontFactory.getFont(FontFactory.COURIER, 8, BaseColor.BLACK);
        //Chunk chunk = new Chunk(data,  font);
        document.newPage();
        document.addAuthor("McDonald");

        // date
        Paragraph datePara = new Paragraph(Instant.now().toString(),dateFont);
        datePara.setAlignment(Element.ALIGN_LEFT);
        datePara.setPaddingTop(0.3f);
        document.add( datePara);

        //Introduction
        String firstPageConent = "SALES SUMMARY REPORT\nOPERATOR ID – 1000018765\nSALES CLOSE PERIOD – ";   // in real world a method will be called which will return first page content
        Paragraph introduction = new Paragraph(firstPageConent, introFont);
        introduction.setAlignment(Element.ALIGN_CENTER);
        introduction.setSpacingBefore(0.3f);
        com.itextpdf.text.Chunk intro2 = new Chunk("November, 2018", dateFont);
        introduction.add(intro2);
        document.add(introduction);


        //Table creation :- I am creating a 3X3 table in live env the dta should come from another method call
        String [][]firstPageData = getfirstPageData();
        //System.out.println(firstPageData[0][1]);
        int columnCount = firstPageData[0].length;
        int rowCount = firstPageData.length;
        PdfPTable table = new PdfPTable(columnCount);
        table.setSpacingBefore(20);
        table.setWidthPercentage(100);


        for( int j =0; j < rowCount; j++)
        for ( int i =0; i< columnCount; i++ ){
            PdfPCell cell = new PdfPCell();

            if(j%2 == 1) {  //for alternate highlight
                cell.setBackgroundColor(new BaseColor(0.0f, 0.0f, .1f, 0.1f));
                cell.setBorderColor(new BaseColor(0.0f, 0.0f, .05f, 0.0f));
            }else {
                cell.setBorder(PdfPCell.NO_BORDER);
            }
            String data = firstPageData[j][i];
            cell.addElement(new Paragraph((null == data?data: data.trim()), tableFont));
            table.addCell(cell);
        }

        document.add(table);



    }

    private String[][] getfirstPageData() {
        String [][] firstPageData = new String [6][12];
        String []header  = {"National #","	State Site ID","	Long Description","	Days Missing","	(A) Cash Sheet - All Net Sales","	(B) Other Sales or Other Receipts ","	(A+B) Total Reportable Sales","	Total Guest Count","	Break Fast","	Drive Thru","	Temp close periods","	Exceptions / Justification  Description"};
        firstPageData[0]= header;
        firstPageData[1]= new String[]{"02012","012-0001","LANSING-TORRENCE","4","300041.01","0","208395.99","99012","30012","0","12/1/2018, 12/2/2018,Remodel,12/5/2018","12/6/2018 Other Holiday","Missing Data / No Sales for 4 days due to water repair        Sales same as Last Month / yes"," because of 5 days repair and holiday"};
        firstPageData[2]= new String[]{"02078","012-0341","CENTRALIA-LINCLN","0","100041.01","0","221544.32","120012","20012","78012","",""};
        firstPageData[3]= new String[]{"05012","012-0564","RT 59","31","0","0","0","0","0","0","12/1/2018, 12/31/2018, Rebuild, Fire",""};
        firstPageData[4]= new String[]{"05441","012-0501","OGDEN AND 34TH","","0","701031.99","701031.99","200","10","190","",""};
        firstPageData[4]= new String[]{"",	"",	"",	"",	"Total",	"0",	"0",	"0",	"0",	"0",	"0",	"",};
        return firstPageData;

    }






/*
    public byte[] writePDFUsingPDFBox(String data) throws IOException {
        PDDocument document = new PDDocument();

        //writing pages
        writePages(document);

    //Saving in file and reading from there
        String tempFileName = Math.random() + ".pdf";
        File file = new File(tempFileName);
        document.save(file);
        byte[] fileByte = Files.readAllBytes(file.toPath());

        //deleting file
        file.delete();

        return fileByte;

    }

    private void writePages(PDDocument document) throws IOException {


        //Writing first page It has date and other topics

        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);


        //page hieght and width calculator
        float height = page.getMediaBox().getHeight();
        float width = page.getMediaBox().getWidth();

        //writing header                            //10 % of page height for header
        writeHeader(contentStream, height, height*0.9f);

        //writing body
        writeBody(contentStream, height*0.9f, height*0.1f);

        //writing footer                 //10 % of page height for footer
        writeFooter(contentStream, height*0.1f, 0);

        contentStream.close();
    }

    private void writeHeader(PDPageContentStream contentStream,  float heightBegin , float heightEnd) throws IOException {
        contentStream.setFont(PDType1Font.COURIER, 12);
        PDFont font = PDType1Font.COURIER;
        contentStream.setNonStrokingColor(20,20,200);

        //positioning the text
        contentStream.beginText();
        contentStream.newLineAtOffset(10, (heightBegin+heightEnd)/2);

        //writing in pdf
        contentStream.showText("Hi How are You");
        contentStream.close();



    }
    private void writeBody(PDPageContentStream contentStream, float hieghtBegin , float hieghtEnd){

    }
    private void writeFooter(PDPageContentStream contentStream, float hieghtBegin , float hieghtEnd){

    }
*/

}
