package com.mdcc.dto2ts.core.test.domains;

import com.mdcc.dto2ts.core.context.Arguments;
import com.mdcc.dto2ts.core.domains.DomainHandler;
import com.mdcc.dto2ts.core.test.BaseUnitTestClass;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
public class DomainHandlerTest extends BaseUnitTestClass
{
    @Mock
    private Arguments arguments;

    @InjectMocks
    private DomainHandler domainHandler;

    @SneakyThrows
    @BeforeEach
    public void setup()
    {
        lenient().when(arguments.getDomainFile()).thenReturn("src/test/resources/test-domains.properties");
        lenient().when(arguments.getDomainFilePrefix()).thenReturn("a.");
        lenient().when(arguments.getThreshold()).thenReturn(0.7);
        domainHandler.init();
    }

    @Test
    @BeforeEach
    public void testRegisterUsedDomains()
    {
        this.domainHandler.registerUsedDomain("Domain1");
        this.domainHandler.registerUsedDomain("Domain2");
        this.domainHandler.registerUsedDomain("Domain1");

        Set<String> domains = this.domainHandler.getDomainsUsed();
        assertEquals(2, domains.size());
        assertTrue(domains.contains("Domain1"));
        assertTrue(domains.contains("Domain2"));
        assertFalse(domains.contains("Domain3"));
    }

    @Test
    public void testFindDomains()
    {
        assertEquals("miotipo", this.domainHandler.findDomain("MoiTipo").get());
        assertEquals("miodominio", this.domainHandler.findDomain("MioDminio").get());
    }
}
