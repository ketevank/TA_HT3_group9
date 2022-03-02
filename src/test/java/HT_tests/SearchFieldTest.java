package HT_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class SearchFieldTest {
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeTest
    public void setupTest() {
        WebDriverManager.chromedriver().browserVersion("97").setup();
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.amazon.com/");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.amazon.com/",
                "The current link is not as expected.");
    }

    @AfterTest
    public void cleanUpTest() {
        driver.close();
        driver.quit();

    }

    @Test
    public void incorrectSearchingTest(){
        WebElement searchBar = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"twotabsearchtextbox\"]")));
        searchBar.sendKeys("ghjk678tyuuy557767reettoouu455959595955");

        WebElement searchButton = getUntil(ExpectedConditions.elementToBeClickable(
                By.id("nav-search-submit-button")));
        searchButton.click();

        String noResultsMessage = driver.findElement(
                By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[3]/div[2]/div[1]/div/div/div/div[1]")
        ).getText();
        Assert.assertTrue(noResultsMessage.contains("No results for"));
    }

    @Test
    public void searchingItemsTest() {
        WebElement searchBar = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"twotabsearchtextbox\"]")));
        searchBar.sendKeys("laptop");

        WebElement searchButton = getUntil(ExpectedConditions.elementToBeClickable(
                By.id("nav-search-submit-button")));
        searchButton.click();

        String resultAmount = driver.findElement(
                By.xpath("//*[@id=\"search\"]/span/div/h1/div/div[1]/div/div")
        ).getText();
        Assert.assertTrue(resultAmount.contains("laptop"));
    }

    @Test
    public void foundItemsTest() {
        WebElement searchBar = getUntil(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"twotabsearchtextbox\"]")));
        searchBar.sendKeys("laptop");

        WebElement searchButton = getUntil(ExpectedConditions.elementToBeClickable(
                By.id("nav-search-submit-button")));
        searchButton.click();

        boolean laptop = false;
        while (!laptop) {
            int index = 3;
            String resultItem = driver.findElement(
                    By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[3]/div[2]/div["+index+"]")
            ).getText();
            boolean ifContains = resultItem.contains("Laptop");
            if (ifContains) {
                laptop = true;
            }
            index++;
        }
    }

    private WebElement getUntil(ExpectedCondition<WebElement> isTrue) {
        return webDriverWait.pollingEvery(Duration.ofMillis(10)).until(isTrue);
    }
}
