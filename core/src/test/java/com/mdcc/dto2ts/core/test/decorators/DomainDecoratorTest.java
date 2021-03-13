//package com.mdcc.dto2ts.core.test.decorators;
//
//import com.mdcc.dto2ts.core.context.*;
//import com.mdcc.dto2ts.core.decorators.*;
//import com.mdcc.dto2ts.core.domains.*;
//import com.mdcc.dto2ts.core.imports.*;
//import com.mdcc.dto2ts.core.test.*;
//import org.junit.*;
//import org.junit.runner.*;
//import org.mockito.*;
//import org.mockito.junit.*;
//
//import java.util.*;
//
//import static com.mdcc.dto2ts.core.context.ContextConstants.DOMAIN_KEY;
//import static com.mdcc.dto2ts.core.imports.ImportNames.*;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DomainDecoratorTest extends BaseUnitTestClass
//{
//    @Mock
//    private DomainHandler domainHandler;
//
//    @Mock
//    private ImportHandler importHandler;
//
//    @InjectMocks
//    private DomainDecorator domainDecorator;
//
//    @Test
//    public void testShouldTransformCorrectly()
//    {
//        String domain = "miotipo";
//
//        PropertyModel property = PropertyModel.builder()
//            .tsType(new GenericBasicType(I_LOCALIZABLE_PROPERTY, BasicType.string()))
//            .name("cod" + domain)
//            .build();
//
//        PropertyContext ctx = PropertyContext.builder()
//            .className("className")
//            .dataHolder(property)
//            .build()
//            .withExtendedProperty(DOMAIN_KEY, domain);
//
//        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);
//
//        assertTrue(result.isPresent());
//
//        List<DecoratorModel> decorators = result.get().getDecorators();
//        assertEquals(1, decorators.size());
//
//        DecoratorModel d = decorators.get(0);
//        assertEquals(1, d.getParameters().size());
//        assertEquals(JSON_LOCALIZABLE_PROPERTY, d.getIdentifier());
//
//        String expr = d.getParameters().get(0);
//        assertEquals(DOMAINS + "." + domain, expr);
//
//        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), JSON_LOCALIZABLE_PROPERTY);
//        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), DOMAINS);
//        verify(domainHandler, times(1)).registerUsedDomain(domain);
//    }
//
//    @Test
//    public void testShouldNotTransformIfNotLocalizable()
//    {
//        PropertyModel property = PropertyModel.builder()
//            .name("codmiotipo")
//            .tsType(BasicType.string())
//            .build();
//
//        PropertyContext ctx = PropertyContext.builder()
//            .className("className")
//            .dataHolder(property)
//            .build();
//
//        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);
//
//        assertFalse(result.isPresent());
//
//        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
//        verify(domainHandler, never()).registerUsedDomain(anyString());
//    }
//}
