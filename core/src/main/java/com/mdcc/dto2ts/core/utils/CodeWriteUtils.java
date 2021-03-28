package com.mdcc.dto2ts.core.utils;

import com.mdcc.dto2ts.core.imports.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class CodeWriteUtils
{
    @Autowired
    private ImportHandler importHandler;

    public String generateClassImport(String className)
    {
        return importHandler
            .getImportsFor(className)
            .reduce(
                new StringBuilder(),
                (sb, tuple) -> sb.append(String.format(
                    "import { %s } from '%s';%n",
                    String.join(", ", tuple._2()),
                    tuple._1()
                ))
            )
            .toString();
    }

    public String setDefaultForArrays(String code)
    {
        return code.replace("[];", "[] = [];");
    }
}
