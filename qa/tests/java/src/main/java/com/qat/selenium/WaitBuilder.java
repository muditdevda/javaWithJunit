package com.dfs.selenium;

public class WaitBuilder {

	private WaitBuilder() {
	}

	public static NoStaleWait buildNoStaleWait() {
		return new NoStaleWait(DriverManager.getDriver());
	}

}
