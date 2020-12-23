package com.mdcc.dto2ts.test;

import com.mdcc.dto2ts.domains.*;
import lombok.*;
import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

public class DomainHandlerTest
{
    private DomainHandler domainHandler;

    @Before
    public void setup()
    {
        this.domainHandler = new DomainHandler(0.7);
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
