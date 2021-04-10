package com.mdcc.dto2ts.json.main;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MainRunner implements CommandLineRunner
{
    @Autowired
    private MainLogic mainLogic;

    @Override
    public void run(String... args)
    {
        this.mainLogic.run();
    }
}
