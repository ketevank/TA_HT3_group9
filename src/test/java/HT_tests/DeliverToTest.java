package HT_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class DeliverToTest {

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeTest
    public void setupTest() {
        WebDriverManager.chromedriver().browserVersion("97").setup();
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterTest
    public void cleanUpTest() {
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

        WebElement zipCodeField = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"GLUXZipUpdateInput\"]")));
        zipCodeField.sendKeys("19901");

        WebElement applyButton = driver.findElement(
                By.xpath("//*[@id=\"GLUXZipUpdate\"]/span/input"));
        applyButton.click();

        try {
            WebElement changeButton = getUntil(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"nav-main\"]/div[1]/div/div/div[3]/span[2]/span/input")));
            changeButton.click();
        } catch (RuntimeException e) {

        }

        WebElement continueButton = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*/div[@id=\"a-popover-1\"]//*[@class=\"a-popover-footer\"]//*[@data-action=\"GLUXConfirmAction\"]")));
        continueButton.click();

        WebElement deliverArea = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"glow-ingress-line2\" and contains(text(),\"Dover\")]")));
        Assert.assertEquals(deliverArea.getText().trim(), "Dover 19901\u200C");
    }

    @Test
    public void checkDeliveryToPolandTest() {
        driver.get("https://www.amazon.com/");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.amazon.com/",
                "The current link is not as expected.");

        WebElement deliverToIcon = driver.findElement(By.xpath("//*[@id=\"nav-global-location-slot\"]"));
        deliverToIcon.click();

        WebElement outsideUsList = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"GLUXCountryListDropdown\"]/span")));
        outsideUsList.click();

        WebElement polandListElement = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"a-popover-2\"]/div/div/ul/li/a[contains(text(), \"Poland\")]")));
        Assert.assertEquals(polandListElement.getText().trim(), "Poland");
    }

    @Test
    public void itemDeliverToSettedAreaTest() {
        driver.get("https://www.amazon.com/");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.amazon.com/",
                "The current link is not as expected.");

        WebElement deliverToIcon = driver.findElement(By.xpath("//*[@id=\"nav-global-location-slot\"]"));
        deliverToIcon.click();

        WebElement outsideUsList = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"GLUXCountryListDropdown\"]/span")));
        outsideUsList.click();

        WebElement countryChoice = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"a-popover-2\"]/div/div/ul/li/a[contains(text(), \"Italy\")]")
        ));
        countryChoice.click();

        WebElement doneButton = driver.findElement(
                By.xpath("//div[@class=\"a-popover-wrapper\"]//span[@data-action=\"a-popover-close\"]"));
        doneButton.click();

        while (!elementExists(By.xpath("//*[@data-component-type=\"s-search-results\"]"))) {
            WebElement categoryOfItems = getUntil(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[@aria-label=\"Headsets\"]")
            ));
            categoryOfItems.click();
        }

        WebElement chosenItem = getUntil(ExpectedConditions.elementToBeClickable(
                By.xpath("//h2[@class=\"a-size-mini a-spacing-none a-color-base s-line-clamp-2\"]/a/span[contains(text(), \"Targeal\")]")));
        chosenItem.click();

        String actualString = driver.findElement(
                By.xpath("//div[@class=\"a-box-inner\"]/div/div/span[@class=\"a-size-base a-color-secondary\"]")).getText();
        Assert.assertTrue(actualString.contains("Italy"));
    }

    private WebElement getUntil(ExpectedCondition<WebElement> isTrue) {

        return webDriverWait.pollingEvery(Duration.ofMillis(10)).until(isTrue);
    }

    private boolean elementExists(By xpath) {
        boolean exists = false;
                try {
                    WebElement element = driver.findElement(xpath);
                    exists = element.isDisplayed();
                } catch (RuntimeException e){

                }
        return exists;
    }
}
