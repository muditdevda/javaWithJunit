package com.qat.helper.files;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class FileReaderHelper {
    private static Logger logger = LoggerFactory.getLogger(FileReaderHelper.class);

    public String pDFRead(InputStream input) throws IOException, SAXException, TikaException {

        BodyContentHandler handler = new BodyContentHandler();
        ParseContext pcontext = new ParseContext();
        Metadata metadata = new Metadata();
        PDFParser parser = new PDFParser();
        PDFParserConfig pdfpc = new PDFParserConfig();
        pdfpc.setExtractInlineImages(true);
        parser.setPDFParserConfig(pdfpc);
        parser.parse(input, handler, metadata, pcontext);
        // getting the content of the document
        logger.info("Contents of the PDF :" + handler.toString());
        // getting metadata of the document
        return handler.toString();

    }

    public void officeRead(InputStream input) throws IOException, SAXException, TikaException {

        // detecting the file type
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();
        // OOXml parser
        OOXMLParser msofficeparser = new OOXMLParser();
        msofficeparser.parse(input, handler, metadata, pcontext);

    }

    public String readAnyFile(InputStream input) throws IOException, SAXException, TikaException {

        // detecting the file type
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();
        // OOXml parser
        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(input, handler, metadata, pcontext);
        return handler.toString();

    }

}
