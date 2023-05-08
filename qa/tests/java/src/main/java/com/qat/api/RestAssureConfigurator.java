package com.dfs.api;

import io.restassured.RestAssured;

public class RestAssureConfigurator {

	public RestAssureConfigurator() {
		setRelaxation();
	}

	private void setRelaxation() {
		RestAssured.useRelaxedHTTPSValidation();
	}

}
