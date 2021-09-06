import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;

public class SubscribeInstagramAccount {
private String EMAIL;
private final String NICKNAME = getRandomStringValue(9);
private final String PASSWORD = getRandomStringValue(8);
private final String ACCOUNT_TO_FOLLOW = "TYPE NICKNAME";

    @Test
    public void shouldRegister(){
        WebDriver driver = new FirefoxDriver();
        WebDriverRunner.setWebDriver(driver);
        Configuration.timeout = 20_000;
        Configuration.pageLoadTimeout = 10_000;
        open("https://10minemail.com/en/");
        $("button.btn-rds:nth-child(1)").waitUntil(appear, 100).click();
        try {
            EMAIL = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        if (r != null) {
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_T);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_T);
        } else {
            throw new NullPointerException("Robot is not initialized");
        }

        switchTo().window(1);
        open("https://www.instagram.com/");
        $(byXpath("//a[@href='/accounts/emailsignup/']")).click();
        $(byName("emailOrPhone")).setValue(EMAIL);
        $(byName("fullName")).setValue(getRandomStringValue(5));
        $(byName("username")).setValue(NICKNAME);
        $(byName("password")).setValue(PASSWORD);
        $(byXpath("//button[text()='Регистрация']")).click();
        $(byXpath("//select[@title='Месяц:']")).selectOptionByValue(String.valueOf(getRandomMonth()));
        $(byXpath("//select[@title='День:']")).selectOptionByValue(String.valueOf(getRandomDay()));
        $(byXpath("//select[@title='Год:']")).selectOptionByValue(String.valueOf(getRandomYear()));
        $(byXpath("//button[text()='Далее']")).click();

        switchTo().window(0);

        $(".inbox-dataList > ul:nth-child(1) > li:nth-child(2) > div:nth-child(2) > span:nth-child(1) > a:nth-child(1)")
                .waitUntil(appear, 60000).scrollTo().click();
        String emailCode = $(".inbox-data-content-intro > table:nth-child(2) > tbody:nth-child(1) " +
                "> tr:nth-child(1) > td:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) " +
                "> tr:nth-child(4) > td:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) " +
                "> tr:nth-child(1) > td:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) " +
                "> tr:nth-child(2) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) " +
                "> tr:nth-child(2) > td:nth-child(2)").waitUntil(appear, 10000).getOwnText();

        switchTo().window(1);
        $(byName("email_confirmation_code")).setValue(emailCode);
        $(byXpath("//button[text()='Далее']")).click();
        $(byXpath("//button[text()='Не сейчас']")).click();
        writeDataToFile();
        $(".TqC_a").waitUntil(appear, 5000).click();
        $(".XTCLo").setValue(ACCOUNT_TO_FOLLOW);
        $(".JvDyy > a:nth-child(1) > div:nth-child(1)").click();
        $(".sqdOP").click();
    }

    private void writeDataToFile(){
        try(FileWriter writer = new FileWriter("account_result", true)){
            writer.write("\n");
            writer.write("----------------");
            writer.write("\n");
            writer.write("Email: " + EMAIL);
            writer.write("\n");
            writer.write("Nickname: " + NICKNAME);
            writer.write("\n");
            writer.write("Password: " + PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRandomStringValue(int n) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < 8; i++) {
            int index = (int)(letters.length() * Math.random());
            sb.append(letters.charAt(index));
        }
        return sb.toString();
    }

    private Integer getRandomMonth(){
        return 1 + (int) (Math.random() * 12);
    }

    private Integer getRandomDay(){
        return 1 + (int) (Math.random() * 30);
    }

    private Integer getRandomYear(){
        return 1980 + (int) (Math.random() * 2002);
    }
}
