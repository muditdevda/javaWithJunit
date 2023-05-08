package com.dfs.helper.gmail;

import com.dfs.helper.properties.PropertiesHelper;
import com.dfs.pages.BasePage;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.common.io.BaseEncoding;
import io.qameta.allure.Step;
import org.awaitility.Awaitility;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmailChecker extends BasePage{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailChecker.class);
	public static final int NOTIFICATION_TIMEOUT_SECONDS = 350;
	/** Application name. */
	private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			"src/main/resources/gmail/credentials/");
	
	private static final java.io.File SECRETS_DIR = new java.io.File(
			"src/main/resources/gmail/secrets/");
	
	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String USER = "me";
	/** Global instance of the HTTP transport. */
	private final HttpTransport httpTransport;
	/** Global instance of the {@link FileDataStoreFactory}. */
	private final FileDataStoreFactory dataStoreFactory;
	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/gmail-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.MAIL_GOOGLE_COM);
	private final GoogleClientSecrets clientSecrets;
	private final String profile;

	private static final PropertiesHelper PROPERTIES_HELPER = new PropertiesHelper();

	public EmailChecker(String profile) throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(new java.io.File(DATA_STORE_DIR, profile));
		logger.info("using the {} profile", profile);
		File secretFile = new File (SECRETS_DIR,profile + "_secret.json");
		
		clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(secretFile));
		this.profile = profile;
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 */
	public Credential authorize() throws IOException {
		// Load client secrets.
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).setAccessType("offline").build();

		logger.info("The client secret is {}", clientSecrets.getDetails().getClientId());
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(clientSecrets.getDetails().getClientId());
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * 
	 * @return an authorized Gmail client service
	 */
	public Gmail getGmailService() throws IOException {
		Credential credential = authorize();
		logger.info("The google token is {}", credential.getAccessToken());
		return new Gmail.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Searchs for a notification with the desired text to exist on the inbox
	 * 
	 * @param text the string that the email has to contain
	 * 
	 * @return a Boolean, true if the email is found false if not
	 */
	// If no notifications... check these accounts
	// pmmultitrans@gmail.com / pmmultitrans12
	// clientmultitrans@gmail.com / clientmultitrans123
	// providermultitrans@gmail.com / providermultitrans123
	
	// the Gmail smtp server that we are using is:
	// mail server address: smtp.gmail.com
	// use tls: yes
	// mail server port: 587
	// mail server username: multitransemailer@gmail.com /multitransemailer2@gmail.com
	// mail server password: multitransemailer123
	// invoicing from: multitransemailer@gmail.com
	// notify from: multitransemailer@gmail.com
	// project From: multitransemailer@gmail.com

	
	private ListMessagesResponse waintUntilGetMessage(String text) throws IOException {
		Gmail service = getGmailService();
		ListMessagesResponse response;
		int retries = 0;
		do {
			response = service.users().messages().list(USER).setQ(text).execute();			
			++retries;
			logger.info("Getting messages, try:  {}", retries);
			logger.info("messages num: {}", response.size());
		} while (response.size()==0 && retries != 10);
		return response;
	}

	private boolean getUserNotification(String text) throws IOException {
		boolean found = false;
		Gmail service = getGmailService();
		ListMessagesResponse response = waintUntilGetMessage(text);
		List<Message> messages = new ArrayList<>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(USER).setQ(text).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}
		logger.info("The messages list size is: {}", messages.size());
		if (!messages.isEmpty()) {
			found = true;
			logger.info(":::Amount of messages found::: {}", messages.size());
		}


		
		return found;
	}


	public String getMessageFromNotification(String text) throws IOException {
		String htmlMessage = null;
		Gmail service = getGmailService();
		ListMessagesResponse response = waintUntilGetMessage(text);
		List<Message> messages = new ArrayList<>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(USER).setQ(text).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}
		if (!messages.isEmpty()) {
			logger.info(":::Amount of messages found::: {}", messages.size());
			for (Message m : messages) {
				Message readed = service.users().messages().get("me", m.getId()).setFormat("full").execute();
				List<MessagePart> mp = (readed.getPayload().getParts());
				htmlMessage = (StringUtils.newStringUtf8(BaseEncoding.base64Url().decode(mp.get(0).getBody().getData())));
			}
		}
		return htmlMessage;
	}

	public String getMessageBody(Message message) throws IOException {
		Gmail service = getGmailService();
		Message mes = service.users().messages().get(USER, message.getId()).set("format", "full").execute();
		byte[] emailBytes = BaseEncoding.base64Url().decode(mes.getPayload().getParts().toString());
		return new String(emailBytes, StandardCharsets.UTF_8);
	}

	@Step ("Wait until a new project notification is received")
	public boolean waitForNewProjectNotification(String projectNumber) {
		String searchString = "subject:" + PROPERTIES_HELPER.getNewProjectPmNotification() + " AND subject: "
				+ projectNumber;
		waitForNotification(searchString);
		return true;
	}

	public boolean waitForNewTaskToProviderNotification(String projectNumber) {
		String searchString = PROPERTIES_HELPER.getServiceProviderNotification() + " AND " + projectNumber;
		waitForNotification(searchString);
		return true;
	}

	public boolean waitForProjectDeliveredToCustomerNotification(String projectNumber) {
		String searchString = PROPERTIES_HELPER.getCustomerProjectDeliveryNotification() +   " AND subject: " +  projectNumber;

		logger.info("searching for {} ", searchString);
		waitForNotification(searchString);
		return true;
	}

	public boolean waitForProviderServiceSummaryNotification(String projectNumber) {
		String searchString = PROPERTIES_HELPER.getProviderServiceSummaryNotification() + " AND " + projectNumber;
		logger.info("searching for {} ", searchString);
		waitForNotification(searchString);
		return true;
	}
	public boolean waitForNotification(String searchString) {
		logger.info("Executing the following request against GMAIL API: {}", searchString);
		Awaitility.await().atMost(NOTIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
				.until(() -> getUserNotification(searchString));
		return true;
	}

	private List<Message> listMessagesMatchingQuery(String query) throws IOException {
		Gmail service = getGmailService();
		ListMessagesResponse response = waintUntilGetMessage(query);
		List<Message> messages = new ArrayList<>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(USER).setQ(query).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for (Message message : messages) {
			logger.info(message.toPrettyString());
		}
		return messages;
	}

	private InputStream getAttachmentsFromMessage(String messageId) throws IOException {
		InputStream inputStream = null;
		Gmail service = getGmailService();
		Message message = service.users().messages().get(USER, messageId).execute();
		List<MessagePart> parts = message.getPayload().getParts();
		for (MessagePart part : parts) {
			if (part.getFilename() != null && part.getFilename().length() > 0) {
				String filename = part.getFilename();
				logger.info("Attachment filename: {}", filename);
				String attId = part.getBody().getAttachmentId();
				MessagePartBody attachPart = service.users().messages().attachments().get(USER, messageId, attId)
						.execute();
				byte[] fileByteArray = BaseEncoding.base64Url().decode(attachPart.getData());
				inputStream = new ByteArrayInputStream(fileByteArray);
			}
		}
		return inputStream;
	}

	public InputStream getAttachmentStream(String message) throws IOException {
		waitForNotification(message);
		List<Message> messages = listMessagesMatchingQuery(message);
		logger.info("we have {}" , messages.size());
		return getAttachmentsFromMessage(messages.get(0).getId());
	}
	
	
	public int getMessagesSentToday() throws IOException {
		Gmail service = getGmailService();

		String yesterday = LocalDate.now().minusDays(1).toString();
		String query = "in:sent after:" + yesterday;
		logger.info("Looking for messages with query: \"{}\"", query);
		ListMessagesResponse response = service.users().messages().list(USER).setQ(query).execute();

		int numMessagesSent = 0;

		if (response.getMessages() != null) {
			numMessagesSent = response.getMessages().size();
		}
		logger.info("Num messages sent for {}: {}", profile, numMessagesSent);
		return numMessagesSent;
	}
	

}