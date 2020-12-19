package com.mdcc.dto2ts.main;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class Arguments {
    private String pattern;
    private String outputFolder;
}
