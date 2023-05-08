package com.dfs.helper.files;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfs.pages.BasePage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDialog extends BasePage {

	
	private FileDialog() {
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(FileDialog.class);


	public static void addFile(WebElement element, String filepath) {
		LOGGER.info("Uploading file {}", filepath);
		// Added sleep to make you see the difference.
		element.clear();
		element.sendKeys(filepath);
	}


	public static void addFile(WebDriver driver, String filepath) {
		LOGGER.info("Uploading file {}", filepath);
		WebElement elem = driver.findElement(By.xpath("//input[@type='file']"));
		elem.sendKeys(filepath);
	}

	
}
