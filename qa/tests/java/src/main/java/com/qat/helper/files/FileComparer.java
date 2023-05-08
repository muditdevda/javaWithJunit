package com.dfs.helper.files;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class FileComparer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileComparer.class);

    public double translationUnitDifferencesPercentage(String filePath, String filepath2) {

        List<Boolean> errors = new ArrayList<>();
        double percentageDiff = 0;
        try {
            LOGGER.info("Comparing  {} with  {}", filePath, filepath2);
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc1 = dBuilder.parse(fXmlFile);
            doc1.getDocumentElement().normalize();
            File fXmlFile2 = new File(filepath2);

            DocumentBuilder dBuilder2 = dbFactory.newDocumentBuilder();
            Document doc2 = dBuilder2.parse(fXmlFile2);
            doc2.getDocumentElement().normalize();
            NodeList nList = doc1.getElementsByTagName("trans-unit");
            NodeList nList2 = doc2.getElementsByTagName("trans-unit");
            LOGGER.info("First we will check the amount of the LIst");
            LOGGER.info("{} - {}", nList.getLength(), nList2.getLength());

            double size = nList.getLength();
            LOGGER.info("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Node nNode2 = nList2.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Element eElement2 = (Element) nNode2;

                    if (!eElement.getElementsByTagName("target").item(0).getTextContent().equalsIgnoreCase(eElement2.getElementsByTagName("source").item(0).getTextContent())) {
                        LOGGER.info("Found a difference on the error list on the position :");
                        LOGGER.info(eElement.getElementsByTagName("target").item(0).getTextContent());
                        LOGGER.info(eElement2.getElementsByTagName("source").item(0).getTextContent());
                        errors.add(true);
                    }
                }
            }
            percentageDiff = (errors.size()) * 100 / size;

        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
      return percentageDiff;
    }

    public double skeletonDistance(String filePath, String filepath2) throws ParserConfigurationException, IOException, SAXException {

        double distance = 100;

        LOGGER.info("Comparing {} with {}", filePath, filepath2);
        File fXmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc1 = dBuilder.parse(fXmlFile);
        doc1.getDocumentElement().normalize();

        File fXmlFile2 = new File(filepath2);
        DocumentBuilder dBuilder2 = dbFactory.newDocumentBuilder();
        Document doc2 = dBuilder2.parse(fXmlFile2);
        doc2.getDocumentElement().normalize();

        NodeList nList = doc1.getElementsByTagName("header");
        NodeList nList2 = doc2.getElementsByTagName("header");

        LOGGER.info("First we will check the amount of the List");
        LOGGER.info("{} - {}", nList.getLength(), nList2.getLength());

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Node nNode2 = nList2.item(temp);

            Element eElement = (Element) nNode;
            Element eElement2 = (Element) nNode2;

            distance = comparer3(eElement.getElementsByTagName("mc:files").item(0).getTextContent(), eElement2.getElementsByTagName("mc:files").item(0).getTextContent());

        }
        return distance;
    }

    public double comparer3(String string1, String string2) {
        JaroWinklerSimilarity distance = new JaroWinklerSimilarity();
        return distance.apply(string1, string2);
    }


}

