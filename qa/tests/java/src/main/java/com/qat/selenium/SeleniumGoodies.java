package com.dfs.selenium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumGoodies {
	private static Logger logger = LoggerFactory.getLogger(SeleniumGoodies.class);

	public static void takeScreenshot(String name) {
		Path imagesPath = Paths.get("target/images");
		if (!imagesPath.toFile().exists()) {
			imagesPath.toFile().mkdirs();
		}

		File scrFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
		File outFile = new File(imagesPath.toFile(), name + ".jpg");
		try {
			FileUtils.copyFile(scrFile, outFile);
		} catch (IOException e) {
			logger.error("Error taking screenshot {}", outFile.getAbsolutePath(), e);
		}
	}

}