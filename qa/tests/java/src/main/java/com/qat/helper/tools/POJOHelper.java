package com.qat.helper.tools;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

public class POJOHelper {

    // To prevent instantiation
    private POJOHelper(){}
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(POJOHelper.class);

    public static <T> T convertXMLFileToObject(String filePath, T objectType){
        LOGGER.info("Converting XML file to Object");
        T object = null;
        File file = new File(filePath);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(objectType.getClass());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            object = (T) unmarshaller.unmarshal(file);
            LOGGER.info("XML converted to {}", objectType.getClass());
        }
        catch (JAXBException e) {
            LOGGER.info("File {} is not a xml! Cannot be converted", file.getName());
        }
        return object;
    }

    public static <T> T convertJSONToObject(String content, T objectType){
        LOGGER.info("Converting JSON file to Object");
        LOGGER.info(content);
        Gson gson = new Gson();
        return (T) gson.fromJson(content, objectType.getClass());
    }

    public static <T> T convertJSONFileToObject(String filePath, T objectType){
        LOGGER.info("Converting JSON file to Object");

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOGGER.info("ERROR {}", e.getMessage());
        }
        Gson gson = new Gson();
        return (T) gson.fromJson(Objects.requireNonNull(bufferedReader), objectType.getClass());
    }
}
