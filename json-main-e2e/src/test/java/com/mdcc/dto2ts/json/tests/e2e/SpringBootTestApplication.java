package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.*;
import io.cucumber.spring.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.test.annotation.*;

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
