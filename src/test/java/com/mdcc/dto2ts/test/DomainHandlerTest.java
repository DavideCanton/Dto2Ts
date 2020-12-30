package com.mdcc.dto2ts.test;

import com.mdcc.dto2ts.core.*;
import com.mdcc.dto2ts.domains.*;
import lombok.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

import java.io.*;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
@RunWith(SpringRunner.class)
public class DomainHandlerTest
{
    @Autowired
    private DomainHandler domainHandler;

    @MockBean
    private Arguments arguments;

    @Before
    public void setup()
    {
        Mockito.when(arguments.getThreshold()).thenReturn(0.7);
    }

    @Test
    public void testRegisterUsedDomains()
    {
        this.domainHandler.registerUsedDomain("Domain1");
        this.domainHandler.registerUsedDomain("Domain2");
        this.domainHandler.registerUsedDomain("Domain1");

        val domains = this.domainHandler.getDomainsUsed();
        assertEquals(2, domains.size());
        assertTrue(domains.contains("Domain1"));
        assertTrue(domains.contains("Domain2"));
        assertFalse(domains.contains("Domain3"));
    }

    @Test
    public void testFindDomains()
    {
        val s = "miotipo=aaa\nmiodominio=bbb";
        val reader = new StringReader(s);
        assertTrue(this.domainHandler.loadPropertiesFrom(reader).isSuccess());
        assertEquals("miotipo", this.domainHandler.findDomain("MoiTipo").get());
        assertEquals("miodominio", this.domainHandler.findDomain("MioDminio").get());
    }
}
