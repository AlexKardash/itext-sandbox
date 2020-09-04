package com.alexcorp.example.itextsandbox.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

public class TemplateService {

    public String getTemplateName(Object object) {
        return object.getClass().getSimpleName().toLowerCase().concat(".html");
    }

    public String getStyle(Object object) {
        return object.getClass().getSimpleName().toLowerCase().concat(".css");
    }

    public Document getDocument(String templateName) throws ParserConfigurationException, IOException, SAXException {
        if (templateName != null) {
            InputStream resource = TemplateService.class.getClassLoader().getResourceAsStream(templateName);
            if (resource == null) {
                throw new RuntimeException("Template not found");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(resource);
        }
        throw new RuntimeException("Could not parse the template");
    }

    private Element lookup(Element element, String tag, String id) {
        if (element.getTagName().equals(tag) && element.getAttribute("id").equals(id)) {
            return element;
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element current = (Element) childNodes.item(i);

                if (current.getTagName().equals(tag) && current.getAttribute("id").equals(id)) {
                    return current;
                }

                Element lookup = lookup(current, tag, id);

                if (lookup != null) {
                    return lookup;
                }
            }
        }
        return null;
    }

    private Element addChildElement(Document document, Element element, String tag) {
        Element childElement = document.createElement(tag);
        return (Element) element.appendChild(childElement);
    }

    public String asString(Document document) throws TransformerException {
        StringWriter stringWriter = new StringWriter();
        transform(document, new StreamResult(stringWriter));
        return stringWriter.getBuffer().toString().replaceAll("[\n\r]", "");
    }

    public byte[] asBytes(Document document) throws TransformerException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transform(document, new StreamResult(outputStream));
        return outputStream.toByteArray();
    }

    private void transform(Document document, StreamResult streamResult) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.transform(new DOMSource(document), streamResult);
    }

    public void fillData(Document document, Map<String, String> data) {
        data.forEach((k, v) -> {
            Element divElement = lookup(document.getDocumentElement(), "div", k);
            if (divElement != null) {
                addChildElement(document, divElement, "p").setTextContent(v);
            }
        });
    }
}
