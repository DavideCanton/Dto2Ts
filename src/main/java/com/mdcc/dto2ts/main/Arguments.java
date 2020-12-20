package com.mdcc.dto2ts.main;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class Arguments {
    private String pattern;
    private String outputFolder;
    private String visitableName;
    private String visitablePath;
    private String visitorName;
    private String visitorPath;

}
