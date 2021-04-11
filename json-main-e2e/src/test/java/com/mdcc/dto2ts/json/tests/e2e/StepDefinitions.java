package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.*;
import io.cucumber.java8.*;
import lombok.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("CodeBlock2Expr")
public class StepDefinitions extends SpringBootTestApplication implements En
{
    private File outputDefinition;
    private List<String> fileNames;
    private File generatedDirectory;

    @Autowired
    private JsonArguments jsonArguments;
    @Autowired
    private MainLogic mainLogic;


    public StepDefinitions()
    {
        Before(() -> {
            this.generatedDirectory = new File(jsonArguments.getOutputFolder());
            FileUtils.deleteDirectory(this.generatedDirectory);
        });
        Given("the test case {string}", (String testCase) -> {
            val settableJsonArguments = this.getSettableJsonArguments();

            val path = getFullJsonPath(testCase);
            settableJsonArguments.setJson(path);

            this.outputDefinition = this.getOutputDefinition(testCase);
        });
        Given("the DTO {word}", (String name) -> {
            val settableJsonArguments = this.getSettableJsonArguments();
            settableJsonArguments.setRootModel(name);
        });
        When("^I invoke the program to translate it$", () -> {
            mainLogic.run();
        });
        Then("files generated should be the ones in output folder", () -> {
            Set<String> generatedFiles = getGeneratedFiles();
            Set<String> expectedFiles = getOutputFiles();

            assertEquals(generatedFiles, expectedFiles);
        });
        And("content of each file should match", () -> {
            File[] generatedFiles = Objects.requireNonNull(this.generatedDirectory.listFiles());

            for (File generated : generatedFiles)
            {
                String generatedContent = FileUtils.readFileToString(generated);

                File expected = new File(this.outputDefinition, generated.getName());
                String expectedContent = FileUtils.readFileToString(expected);

                assertEquals(expectedContent, generatedContent);
            }
        });

    }


    private SettableJsonArguments getSettableJsonArguments()
    {
        return (SettableJsonArguments) this.jsonArguments;
    }

    private String getFullJsonPath(String testCase)
    {
        return String.format("src/test/resources/com/mdcc/dto2ts/json/tests/e2e/definitions/%s/input/swagger.json", testCase);
    }

    private File getOutputDefinition(String testCase)
    {
        return new File(String.format("src/test/resources/com/mdcc/dto2ts/json/tests/e2e/definitions/%s/output", testCase));
    }

    private Set<String> getOutputFiles()
    {
        return getFilesFromDirectory(this.outputDefinition);
    }

    private Set<String> getGeneratedFiles()
    {
        return getFilesFromDirectory(this.generatedDirectory);
    }

    private Set<String> getFilesFromDirectory(File directory)
    {
        if (!directory.isDirectory()) throw new IllegalArgumentException("directory");

        File[] array = directory.listFiles();
        assert array != null;

        return Arrays.stream(array)
            .map(File::getName)
            .collect(Collectors.toSet());
    }

}
