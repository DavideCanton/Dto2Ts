package com.mdcc.dto2ts.json.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
