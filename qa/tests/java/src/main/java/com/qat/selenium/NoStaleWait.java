package com.qat.selenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.qat.helper.properties.PropertiesHelper;

public class NoStaleWait extends FluentWait<WebDriver> {
	private PropertiesHelper propertiesHelper = new PropertiesHelper();

	public NoStaleWait(WebDriver input) {
		super(input);
		this.withTimeout(Duration.ofSeconds(propertiesHelper.getWaitTimeout())).pollingEvery(Duration.ofSeconds(5))
				.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
	}

	public NoStaleWait(WebDriver input, Integer timeout) {
		super(input);
		this.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(2))
				.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
	}

	/*
	* The refreshed method is very helpful  when trying to access a search result that has been newly refreshed
	* */
	public WebElement waitUntilClickable(By by){

		return until(ExpectedConditions.refreshed(
				ExpectedConditions.elementToBeClickable(by)));

	}
	
	public WebElement waitUntilClickable(WebElement element) {
		return until(ExpectedConditions.refreshed(
				ExpectedConditions.elementToBeClickable(element)));
	}

	public WebElement waitUntilClickableNoRedrawn(By by){
		return until(ExpectedConditions.elementToBeClickable(by));
	}

	public WebElement waitUntilClickableNoRedrawn(WebElement element) {
		return until(ExpectedConditions.elementToBeClickable(element));

	}
}