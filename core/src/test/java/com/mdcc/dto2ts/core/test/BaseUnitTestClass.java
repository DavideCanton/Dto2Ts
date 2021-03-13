package com.mdcc.dto2ts.core.test;

import org.junit.*;
import org.mockito.*;

public abstract class BaseUnitTestClass
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
}
