package test.domains;

import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.test.*;
import lombok.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;
import test.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(MockitoJUnitRunner.class)
public class DomainHandlerTest extends BaseUnitTestClass
{
    @Mock
    private Arguments arguments;

    @InjectMocks
    private DomainHandler domainHandler;

    @SneakyThrows
    @Before
    public void setup()
    {
        when(arguments.getDomainFile()).thenReturn("src/test/resources/test-domains.properties");
        when(arguments.getThreshold()).thenReturn(0.7);
        domainHandler.init();
    }

    @Test
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
