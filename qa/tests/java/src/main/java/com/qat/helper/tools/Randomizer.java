package com.dfs.helper.tools;

import java.util.Random;
import java.util.UUID;

public class Randomizer {
	private Random rand = new Random();

	public String randomProjectCode() {
		int n = rand.nextInt(999);
		return "CU" + Integer.toString(n);
	}

	public String randomInvoiceNo() {
		int n = rand.nextInt(99999);
		return "INV" + Integer.toString(n);
	}

	public String randomNumber() {
		int n = rand.nextInt(99999);
		return Integer.toString(n);
	}

	public String randomUserCredentials(String userType) {
		int n = rand.nextInt(999999);
		return userType + Integer.toString(n);
	}

	public String randomTitleProject() {
		return generateString() ;
	}

	public String generateString() {
		String uuid = UUID.randomUUID().toString();
		return "title_" + uuid;
	}

}
