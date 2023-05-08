package com.qat.selenium;

import com.qat.helper.properties.PropertiesHelper;
import io.restassured.response.Response;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stqa.selenium.factory.WebDriverPool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class DriverManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
	private static final String DRIVER_PATH = "src/main/resources/driver/";
	private static final String CHROME_BROWSER_VERSION = "102.0";
	private static final String ZALENIUM_HOSTNAME = "chelenoid";
	private static final String ZALENIUM_SERVER = "http://" + ZALENIUM_HOSTNAME + ":4444/wd/hub";
	private static String proxyHost = "";
	private static int proxyPort = 0;

	private static WebDriver driver;
	private static boolean driverIsRemote = false;

	public static boolean isDriverIsRemote() {
		return driverIsRemote;
	}

	public DriverManager()  {//
		super();
	}

	public static String getProxyHost() {
		return proxyHost;
	}

	public static void setProxyHost(String proxyHost) {
		DriverManager.proxyHost = proxyHost;
	}

	public static int getProxyPort() {
		return proxyPort;
	}

	public static void setProxyPort(int proxyPort) {
		DriverManager.proxyPort = proxyPort;
	}


	public static WebDriver createDriver(String testname, int idleTimeout) {

		boolean isActive = driverIsActive();
		LOGGER.debug("is driver active? ---> {}", isActive);
		String driverName = new PropertiesHelper().getWebDriver();

		if (driver != null && isActive) {
			dismissDriver();
		}

		if (driverName.equals("zalenium")){
			driverIsRemote = true;
			driver = getChromeDriverZalenium(testname, idleTimeout);
		} else {
			driver = getChromeDriver();
		}

		LOGGER.info("Created driver {}", driver.toString());
		return driver;
	}

	public static WebDriver createDriver(String testname) {
		return createDriver(testname, 150);
	}



	public static WebDriver getDriver() {
		if (driver == null) {
			LOGGER.warn("Could not find a Driver! Did you forget creating the driver at the beginning of the test?");
			driver = createDriver("testXXXX");

		}
		return driver;
	}

	
	private static boolean driverIsActive() {
		if (driver == null)
			return false;
		SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
		LOGGER.info("Driver instance found {} ", sessionId);
		String session = sessionId.toString();
		LOGGER.debug("session: {}", session);
		return !session.isEmpty();
	}

	private static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		//options.setProxy(null);
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--start-maximized");
		//options.addArguments("--test-type");
		options.addArguments("--disable-extensions"); // to disable browser extension popup
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disk-cache-size=0");
		options.addArguments("--disable-gpu");
		/*This is necessary because in assignAProviderToATasksExceedingItsCapacity(), when checking the toast and
		popping up the alert, we capture the unhandledException but the default behaviour is that the navigator
		closes it. We need to work with the alert and with this capacity the aler is left open.*/
		options.setCapability("unhandledPromptBehavior", "ignore");



		Map<String, Object> preferences = new HashMap<>();
		preferences.put("download.default_directory", "/home/selenium/Downloads");
		preferences.put("download.prompt_for_download", false);
		preferences.put("profile.default_content_settings.popups", 0);
		preferences.put("download.directory_upgrade", true);
		preferences.put("safebrowsing.enabled", false);
		preferences.put("plugins.always_open_pdf_externally", true);
		options.setExperimentalOption("prefs", preferences);

		return options;

	}

	private static RemoteWebDriver instantiateRemoteDriver(URL remoteWebDriverUrl, ChromeOptions capabilities) {
		RemoteWebDriver theDriver = null;

		final int maxTries = 3;

		int numTry = 0;
		while (theDriver == null && numTry++ < maxTries) {

			try {
				LOGGER.info("Instantiating remote driver on {}", remoteWebDriverUrl);
				theDriver = new RemoteWebDriver(remoteWebDriverUrl, capabilities);
				theDriver.setFileDetector(new LocalFileDetector());
				//getProxyInfo(ZALENIUM_HOSTNAME, 4444, theDriver.getSessionId());

			} catch (Exception ex) {
				LOGGER.error("Could not instantiate remote driver! Will re-try after some time", ex);
				try {
					Thread.sleep(5L * 1000);
				} catch (InterruptedException e) {
					LOGGER.info("Interrupted!", e);
					Thread.currentThread().interrupt();

				}
			}
		}

		return theDriver;
	}

	private static WebDriver getChromeDriverZalenium(String testname, int idleTimeout) {
		LOGGER.info("Creating new zalenium driver");
		Map<String, Object> opt = new HashMap<>();
		opt.put("enableVNC",true);
		opt.put ("enableVideo", false);

		ChromeOptions chromeOptions=new ChromeOptions();

		chromeOptions.addArguments("--start-maximized");
		chromeOptions.setBrowserVersion(CHROME_BROWSER_VERSION);
		chromeOptions.setCapability(ChromeOptions.CAPABILITY, getChromeOptions());
		chromeOptions.setAcceptInsecureCerts(true);
		chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromeOptions.setPlatformName(Platform.LINUX.toString());
		chromeOptions.setCapability("name", testname);
		chromeOptions.setCapability("idleTimeout", idleTimeout);
		chromeOptions.setCapability("sessionTimeout", "5m");

		chromeOptions.setCapability("selenoid:options", opt);
		URL remoteWebDriverUrl = null;
		try {
			remoteWebDriverUrl = new URL(ZALENIUM_SERVER);
		} catch (MalformedURLException e) {
			LOGGER.error("Could not create url {}", ZALENIUM_SERVER, e);
		}

		return instantiateRemoteDriver(remoteWebDriverUrl, chromeOptions);

	}

	private static WebDriver getChromeDriver() {
		LOGGER.info("Creating new chrome driver");
		String fullDriverPath;
		if (System.getProperty("os.name").equalsIgnoreCase("linux"))
			fullDriverPath = DRIVER_PATH + "linux/chromedriver";
		else
			fullDriverPath = DRIVER_PATH + "win/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", fullDriverPath);
		return getDriverFromPool(getChromeOptions());
	}

	private static WebDriver getDriverFromPool(MutableCapabilities options) {
		WebDriver theDriver = null;
		try {
			theDriver = WebDriverPool.DEFAULT.getDriver(options);
		} catch (Exception ex) {
			// sometimes we receive weird exception when creating the driver
			LOGGER.error("Exception when instantiating webdriver for the first time");
			try {
				LOGGER.info("Sleeping, to see if we can re-instantiate driver...");
				Thread.sleep(5L * 1000L);
				// let's try again...
				theDriver = WebDriverPool.DEFAULT.getDriver(options);
				LOGGER.info("Driver created after second attemp. {}", theDriver);
			} catch (Exception e) {
				LOGGER.error("Exception when creating driver for second time... ", e);
				SeleniumGoodies.takeScreenshot("ex_creating_driver" + System.currentTimeMillis());
			}
		}
		return theDriver;
	}

	public static void dismissDriver() {
		if (driver != null) {
			LOGGER.info("Dismissing driver...{}", driver.toString());
			if (driverIsRemote) {
				LOGGER.info("Dismissing remote Driver {}", driver.toString());
				driver.quit();
			} else {
				LOGGER.info("Dismissing driver from the pull {}", driver.toString());
				WebDriverPool.DEFAULT.dismissDriver(driver);
			}
			driver = null;
		}
	}



	private static void getProxyInfo(String hostName, int port, SessionId session) {
		Response res = given().when()
				.get("http://" + hostName + ":" + port + "/grid/api/testsession?session=" + session);
		try {
			URL myURL = new URL(res.getBody().jsonPath().getString("proxyId"));
			setProxyHost(myURL.getHost());
			setProxyPort(myURL.getPort());
			LOGGER.info("Docker proxy at {}", myURL);
		} catch (MalformedURLException e) {
			LOGGER.error("Could not find info for proxy server on {}", ZALENIUM_HOSTNAME, e);
		}

	}





}