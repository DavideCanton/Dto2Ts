package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.context.*;
import org.springframework.beans.factory.annotation.*;

public class JsonArguments extends Arguments
{
    @Value("${json}")
    private String json;
}
