package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @InjectMocks
    private HashService hashService;

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
        Assertions.assertTrue(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg"))).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void addNote() {
//		doMockSignUp("Large File","Test","LFT","123");
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        addNoteData();
        Assertions.assertTrue(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteErrorSuccess"))).getText().contains("Add Note Successfully"));
    }

    @Test
    public void editNote() {
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        addNoteData();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditNote2"))).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).clear();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Note Title Edit");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).clear();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).sendKeys("Test Note Description Edit");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSubmit"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        WebElement tableElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        List<WebElement> listRow = tableElement.findElements(By.tagName("tr"));
        WebElement firstDataRow = listRow.get(2);
        WebElement notTitleElement = firstDataRow.findElement(By.tagName("th"));
        WebElement noteDescriptionElement = firstDataRow.findElements(By.tagName("td")).get(1);
        Assertions.assertEquals("Test Note Title Edit", notTitleElement.getText());
        Assertions.assertEquals("Test Note Description Edit", noteDescriptionElement.getText());
    }

    @Test
    public void deleteNote() {
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote2"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        WebElement tableElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        List<WebElement> listRow = tableElement.findElements(By.tagName("tr"));
        Assertions.assertEquals(2, listRow.size());
    }

    private void addNoteData() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddNote"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Note Title");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).sendKeys("Test Note Description");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSubmit"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
    }

    @Test
    public void addCredential() {
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        addCredentialData();
        Assertions.assertTrue(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialErrorSuccess"))).getText().contains("Add Credential Successfully"));
    }

    @Test
    public void editCredential() {
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        addCredentialData();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditCredential2"))).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).clear();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys("http://localhost:8081/login");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).clear();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("usernametestedit");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).clear();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("passwordtestedit");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSubmit"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
        WebElement tableElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        List<WebElement> listRow = tableElement.findElements(By.tagName("tr"));
        WebElement firstDataRow = listRow.get(2);
        WebElement credentialUrlElement = firstDataRow.findElement(By.tagName("th"));
        WebElement credentialUserNameElement = firstDataRow.findElements(By.tagName("td")).get(1);

        Assertions.assertEquals("http://localhost:8081/login", credentialUrlElement.getText());
        Assertions.assertEquals("usernametestedit", credentialUserNameElement.getText());
    }

    @Test
    public void deleteCredential() {
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
//        addCredentialData();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredential2"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
        WebElement tableElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        List<WebElement> listRow = tableElement.findElements(By.tagName("tr"));
        Assertions.assertEquals(2, listRow.size());
    }

    private void addCredentialData() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddCredential"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys("http://localhost:8080/login");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("usernametest");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("passwordtest");
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSubmit"))).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
    }

}
