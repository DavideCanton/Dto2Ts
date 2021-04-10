package com.mdcc;

import io.cucumber.java8.*;

import static org.junit.Assert.*;

class IsItFriday
{
    static String isItFriday(String today)
    {
        return "Friday".equals(today) ? "TGIF" : "Nope";
    }
}

public class StepDefinitions implements En
{
    private String today;
    private String actualAnswer;

    public StepDefinitions()
    {
        Given("^today is \"([^\"]*)\"$", (String arg) -> {
            today = arg;
        });
        When("^I ask whether it's Friday yet$", () -> {
            actualAnswer = IsItFriday.isItFriday(today);
        });
        Then("^I should be told \"([^\"]*)\"$", (String expectedAnswer) -> {
            assertEquals(expectedAnswer, actualAnswer);
        });
    }
}
