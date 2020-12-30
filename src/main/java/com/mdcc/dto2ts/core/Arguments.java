package com.mdcc.dto2ts.core;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;

@SuppressWarnings("unused")
@Component
@Getter
public class Arguments {
    @Value("${pattern}")
    private String pattern;
    @Value("${outputFolder:./target}")
    private String outputFolder;
    @Value("${visitableName}")
    private String visitableName;
    @Value("${visitablePath}")
    private String visitablePath;
    @Value("${visitorName}")
    private String visitorName;
    @Value("${visitorPath}")
    private String visitorPath;
    @Value("${domainFile}")
    private String domainFile;
    @Value("${domainPrefix:cod}")
    private String domainPrefix;
    @Value("${uidPrefix:uid}")
    private String uidPrefix;
    @Value("${threshold:0.8}")
    private double threshold;
}
