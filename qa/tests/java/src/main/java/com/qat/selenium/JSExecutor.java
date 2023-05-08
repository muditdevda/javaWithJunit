package com.dfs.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(JSExecutor.class);

	private WebDriver getDriver() {
		return DriverManager.getDriver();
	}

	public String executeJS(String script, Object... args) {
		JavascriptExecutor je = (JavascriptExecutor) getDriver();
		return (String) je.executeScript(script, args);
	}

	public String executeJS(String script) {
		JavascriptExecutor je = (JavascriptExecutor) getDriver();
		return (String) je.executeScript(script);
	}

	public void scrollUp() {
		executeJS("window.scrollBy(0,-250)", "");
	}

	public void scrollDown() {
		executeJS("window.scrollBy(0,250)", "");
	}
	
	public String getFocusedText() {
		return executeJS("return window.getSelection().toString();");
	}

	public void highlightElement(WebElement element, int duration) {
		String originalStyle = element.getAttribute("style");
		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		jse.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				"border: 2px solid red; border-style: dashed;");

		if (duration > 0) {
			if (!waitException(duration * 1000))
				logger.error("InterruptedException when highlightElement {}", element);
			jse.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
		}
	}

	public void scrollToObject(WebElement element) {
		executeJS("arguments[0].scrollIntoView();", element);
	}

	private boolean waitException(int miliSecs) {
		try {
			// see https://bugs.chromium.org/p/chromedriver/issues/detail?id=2198
			Thread.sleep(miliSecs);
		} catch (InterruptedException e) {
			// Restore interrupted state...
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}
}
