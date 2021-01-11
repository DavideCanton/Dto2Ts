package com.mdcc.dto2ts.core;

import cyclops.control.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@SuppressWarnings("unused")
@Component
@Getter
public class Arguments {
    @Value("${pattern}")
    private String pattern;
    @Value("${outputFolder:./target}")
    private String outputFolder;
    @Value("${createVisitor:false}")
    private boolean createVisitor;
    @Value("${visitableName:}")
    private String visitableName;
    @Value("${visitablePath:}")
    private String visitablePath;
    @Value("${visitorName:}")
    private String visitorName;
    @Value("${domainFile}")
    private String domainFile;
    @Value("${domainPrefix:cod}")
    private String domainPrefix;
    @Value("${uidPrefix:uid}")
    private String uidPrefix;
    @Value("${threshold:0.8}")
    private double threshold;

    public Either<List<String>, Boolean> isValid() {
        var valid = true;
        val messages = new ArrayList<String>();

        if (createVisitor) {
            val hasVisitable = Stream.of(this.visitableName, this.visitorName, this.visitablePath)
                    .allMatch(s -> s != null && !s.isEmpty());
            if (!hasVisitable) {
                valid = false;
                messages.add("If createVisitor is true, visitor parameters should be not empty");
            }
        }

        return valid ? Either.right(true) : Either.left(messages);
    }
}
