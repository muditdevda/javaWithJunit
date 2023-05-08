package com.dfs.helper.tools;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

import static com.dfs.selenium.DriverManager.getDriver;

public class NotificationChecker {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NotificationChecker.class);

    private NotificationChecker(){}

    @Step("Getting Document tab error message and close the modal window")
    public static String getNotificationMessage(String xpath) {
        LOGGER.info("***** getNotificationMessage *****");
        StringBuilder notification = new StringBuilder();
        try {
            List<WebElement> notifications = new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofMillis(1)).until(
                            ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
            for (WebElement notify : notifications)
                notification.append(notify.getText());
        } catch (TimeoutException timeoutException){
            LOGGER.error("Error gettting the notification text");
            timeoutException.getStackTrace();
        }
        LOGGER.info("Notification found: {}", notification);
        return notification.toString();
    }

}
