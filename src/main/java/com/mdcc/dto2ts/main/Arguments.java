package com.mdcc.dto2ts.main;

import com.beust.jcommander.*;
import lombok.*;

@Data
public class Arguments {
    @Parameter(required = true, names = {"-p", "--pattern"})
    private String pattern;
    @Parameter(names = {"-o", "--output-folder"})
    private String outputFolder = "./target";
    @Parameter(names = {"--visitable-name"})
    private String visitableName;
    @Parameter(names = {"--visitable-path"})
    private String visitablePath;
    @Parameter(names = {"--visitor-name"})
    private String visitorName;
    @Parameter(names = {"--visitor-path"})
    private String visitorPath;
    @Parameter(names = "--help", help = true)
    private boolean help;
}
