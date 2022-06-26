package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.Main;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;

@CucumberContextConfiguration
@SpringBootTest(classes = Main.class)
@EnableAutoConfiguration
@ComponentScan(
    basePackages = "com.mdcc.dto2ts"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SpringBootTestApplication
{
}
