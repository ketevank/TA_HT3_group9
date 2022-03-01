package HT_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class DeliverToTest {

    private WebDriver driver;

    @BeforeTest
    public void setupTest() {
        WebDriverManager.chromedriver().browserVersion("97").setup();
        driver = new ChromeDriver();
    }

    @AfterTest
    public void cleasnUpTest() {
        driver.close();
        driver.quit();
    }

    @Test
    public void setDeliveryAreaTest() {
        driver.get("https://www.amazon.com/");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.amazon.com/",
                "The current link is not as expected.");

        WebElement deliverToIcon = driver.findElement(By.xpath("//*[@id=\"nav-global-location-slot\"]"));
        deliverToIcon.click();

        WebElement zipCodeField = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"GLUXZipUpdateInput\"]")));
        zipCodeField.sendKeys("19901");
        WebElement applyButton = driver.findElement(By.xpath("//*[@id=\"GLUXZipUpdate\"]/span/input"));
        applyButton.click();

        try {
            WebElement changeButton = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nav-main\"]/div[1]/div/div/div[3]/span[2]/span/input")));
            changeButton.click();
        } catch (RuntimeException e) {

        }

        WebElement continueButton = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*/div[@id=\"a-popover-1\"]//*[@class=\"a-popover-footer\"]//*[@data-action=\"GLUXConfirmAction\"]")));
        continueButton.click();

        WebElement deliverArea = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"glow-ingress-line2\" and contains(text(),\"Dover\")]")));
        Assert.assertEquals(deliverArea.getText().trim(), "Dover 19901\u200C");
    }
}
