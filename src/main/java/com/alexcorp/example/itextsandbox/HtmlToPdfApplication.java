package com.alexcorp.example.itextsandbox;

import com.alexcorp.example.itextsandbox.domain.ATest;
import com.alexcorp.example.itextsandbox.domain.CTest;
import com.alexcorp.example.itextsandbox.service.PdfService;
import com.itextpdf.text.DocumentException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HtmlToPdfApplication {

    public static void main(String[] args) throws IllegalAccessException, IOException, SAXException,
            ParserConfigurationException, TransformerException, DocumentException {
        ATest test = new CTest(); // entity

        PdfService pdfService = new PdfService();
        // pdf
        byte[] pdf = pdfService.createPdf(test);

        new FileOutputStream("sss.pdf").write(pdf);
    }

}
