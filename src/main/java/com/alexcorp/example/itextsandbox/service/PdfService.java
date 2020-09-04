package com.alexcorp.example.itextsandbox.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PdfService {

    public byte[] createPdf(Object object) throws IllegalAccessException, IOException, SAXException,
            ParserConfigurationException, DocumentException, TransformerException {
        Map<String, String> data = new DataMapperService().mapFields(object, false);

        TemplateService templateService = new TemplateService();
        Document document = templateService.getDocument(templateService.getTemplateName(object));

        templateService.fillData(document, data);

        System.out.println(templateService.asString(document));

        com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(pdfDocument, out);

        pdfDocument.open();
        InputStream style = this.getClass().getClassLoader().getResourceAsStream(templateService.getStyle(object));
        XMLWorkerHelper.getInstance().parseXHtml(writer, pdfDocument, new ByteArrayInputStream(templateService.asBytes(document)),
                style);
        pdfDocument.close();

        return out.toByteArray();
    }
}
