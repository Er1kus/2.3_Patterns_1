package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryChangeDateTest {

    @BeforeEach
    void setUpAll() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldReplanByHappyPath() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $(withText("Я соглашаюсь с условиями обработки и")).click();
        $x("//*[text()=\"Запланировать\"]").click();
        $("[data-test-id='success-notification']")
                .shouldBe(text("Успешно! Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $x("//*[text()=\"Запланировать\"]").click();
        $("[data-test-id='replan-notification']")
                .shouldBe(text("У вас уже запланирована встреча на другую дату. Перепланировать? "), Duration.ofSeconds(15));
        $x("//*[text()=\"Перепланировать\"]").click();
        $("[data-test-id='success-notification']")
                .shouldBe(text("Встреча успешно запланирована на  " + secondMeetingDate), Duration.ofSeconds(15));
    }
}

