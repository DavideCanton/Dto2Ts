package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.*;
import cyclops.companion.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import io.cucumber.datatable.*;
import io.cucumber.java8.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions extends SpringBootTestApplication implements En
{
    private final Map<String, File> outFiles = new HashMap<>();

    @Autowired
    private JsonArguments jsonArguments;
    @Autowired
    private MainLogic mainLogic;

    private SettableJsonArguments getSettableJsonArguments()
    {
        return (SettableJsonArguments) this.jsonArguments;
    }

    private String getFullJsonPath(String name)
    {
        return String.format("src/test/resources/com/mdcc/dto2ts/json/tests/e2e/definitions/%s.json", name);
    }

    private String getOutputFilePath(String name)
    {
        return String.format("target/generated-models/%s", name);
    }

    @SuppressWarnings("Convert2MethodRef")
    public StepDefinitions()
    {
        Before(() -> {
            this.outFiles.clear();
        });
        Given("the schema {string}", (String fileName) -> {
            val s = this.getSettableJsonArguments();
            val path = getFullJsonPath(fileName);
            s.setJson(path);
        });
        Given("the DTO {word}", (String name) -> {
            val s = this.getSettableJsonArguments();
            s.setRootModel(name);
        });
        When("^I invoke the program to translate it$", () -> {
            mainLogic.run();
        });
        Then("a file named {string} should have been created", (String name) -> {
            String path = getOutputFilePath(name);
            val outFile = new File(path);
            outFiles.put(name, outFile);
            assertTrue(outFile.exists());
        });
        And("imports in {string} should be:", (String name, DataTable t) -> {
            File outFile = this.outFiles.get(name);
            assertNotNull(outFile);

            val m = t.asMaps();

            List<String> lines = readLines(outFile);

            Streams.zipStream(
                lines.stream(),
                m.stream(),
                Tuple2::of
            ).forEach(tuple -> {
                String line = tuple._1();
                val map = tuple._2();

                val importedSymbols = map.get("importedSymbols");
                val from = map.get("from");

                assertEquals("import { " + importedSymbols + " } from '" + from + "';", line);
            });
        });
    }

    private List<String> readLines(File file)
    {
        return Try.withResources(
            () -> new BufferedReader(new FileReader(file)),
            reader -> {
                List<String> lines = new ArrayList<>();

                while (true)
                {
                    String line = reader.readLine();
                    if (line == null) break;
                    lines.add(line);
                }

                return lines;
            },
            IOException.class
        ).fold(
            l -> l,
            __ -> null
        );
    }
}
