package com.mdcc.dto2ts.core.context;

import cyclops.control.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.*;

@Getter
@Setter(AccessLevel.PROTECTED)
public class Arguments
{
    @Value("${suffixToRemove:Model$}")
    private String suffixToRemove;
    @Value("${prefixToRemove:^(prs|api|srv|opr|agt).}")
    private String prefixToRemove;
    @Value("${outputFolder:./target/generated-models}")
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
    @Value("${domainFilePrefix:}")
    private String domainFilePrefix;
    @Value("${uidPrefix:uid}")
    private String uidPrefix;
    @Value("${threshold:0.8}")
    private double threshold;

    public Either<List<String>, Boolean> isValid()
    {
        var valid = true;
        val messages = new ArrayList<String>();

        if (createVisitor)
        {
            val hasVisitable = Stream.of(this.visitableName, this.visitorName, this.visitablePath)
                .allMatch(s -> s != null && !s.isEmpty());
            if (!hasVisitable)
            {
                valid = false;
                messages.add("If createVisitor is true, visitor parameters should be not empty");
            }
        }

        return validationResultToEither(valid, messages);
    }

    @NotNull
    protected Either<List<String>, Boolean> validationResultToEither(boolean valid, List<String> messages)
    {
        return valid ? Either.right(true) : Either.left(messages);
    }
}
