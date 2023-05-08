package com.dfs.helper.files;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.dfs.helper.properties.PropertiesHelper;
import com.dfs.helper.database.DBConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// We will store here common generic methods to be used for any test/helper 

// TODO: Merge with FileHelper and others
public class Common {
	private static Logger logger = LoggerFactory.getLogger(Common.class);
	private static String filePath = new PropertiesHelper().getProjectFile();
	
	public String getDateFromFormat(String format)  {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return (dateFormat.format(date));
	}

//	return date as string of YYYYMMDD, its used to identify project on src/webEditorProject.txt file
	public String getDateYYYYMMDD()  {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return (dateFormat.format(date));
	}
	
	public String getPropFormat(String property) {
		return "[" + getDateYYYYMMDD() + "_" + property + "]";
	}
	
	public String getPropFormatToSearch(String property) {
		return "[" + getDateYYYYMMDD() + "_" + property;
	}
	
	
	public String readPropertyFromFile(String property) throws IOException  {
		String propertyValue = null;
		if (fileExists(filePath)){
			Properties prop = new Properties();
			InputStream input = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);

			try {
				prop.load(inputStreamReader);
			} catch (Exception e){
				logger.error(e.toString());
			} finally {
				inputStreamReader.close();
			}
			propertyValue = prop.getProperty(getPropFormat(property));
		}
		return propertyValue;
	}
	
	
	public void writeProjectReqID(String property, String projectReq) throws IOException {
		writePropertyToFile(property, projectReq);
	}
	
	public void writePropertyToFile(String property, String value) throws IOException  {
		if (value!=null) {
			if (!fileExists(filePath))
				createFile(filePath);
			// load properties
			Properties prop = new Properties();
			InputStream input = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);

			try {
				prop.load(inputStreamReader);
				input.close();
				// remove project if already exists in any state
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					if (value.equals(prop.getProperty(key)))
						prop.remove(key);
				}
				// write the new project info.
				prop.setProperty(getPropFormat(property), value);

			} catch (Exception e){
				logger.error(e.toString());
			}
			finally {
				inputStreamReader.close();
			}

			OutputStream output = new FileOutputStream(filePath);
			try {
				prop.store(output, null);
			} catch (Exception e){
				logger.error(e.toString());
			}
			finally {
				output.close();
			}
		    logger.info("************ writePropertyToFile {} ************", prop);
		}
		else logger.info("************ writePropertyToFile - No Project to log ************");
	}
	
	public boolean fileExists(String file) {
		File myFile = new File(file);
		logger.info("************ fileExists {}: {} ************", file, myFile.exists() );
		return myFile.exists();
	}
	
	public void createFile(String file) throws IOException {
		List<String> lines = Arrays.asList("-");
		Files.write(Paths.get(file), lines, StandardCharsets.UTF_8,
		        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		logger.info("************ createFile {} ************", file);
	}
	

	

	public void burnProjectFromFile(String requestProjecyID) throws IOException {
		logger.info("************ remove Prjcet id: {} from file ************", requestProjecyID);
		if (fileExists(filePath)){
			Properties prop = new Properties();
			InputStream input = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);

			try {
				prop.load(inputStreamReader);
				Enumeration<?> e = prop.propertyNames();

				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					if (requestProjecyID.equals(prop.getProperty(key)))
						prop.remove(key);
				}
			}
			catch(Exception e){
				logger.error(e.toString());
			}
			finally{
				inputStreamReader.close();
				input.close();
			}
		}
	}
	
}
