//package com.mdcc.dto2ts.core.test.transformers;
//
//import com.mdcc.dto2ts.core.context.*;
//import com.mdcc.dto2ts.core.context.types.*;
//import com.mdcc.dto2ts.core.domains.*;
//import com.mdcc.dto2ts.core.imports.*;
//import com.mdcc.dto2ts.core.test.*;
//import com.mdcc.dto2ts.core.transformers.*;
//import org.junit.*;
//import org.junit.runner.*;
//import org.mockito.*;
//import org.mockito.junit.*;
//
//import java.util.*;
//
//import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DomainPropertyTransformerTest extends BaseUnitTestClass
//{
//    @Mock
//    private DomainHandler domainHandler;
//
//    @Mock
//    private ImportHandler importHandler;
//
//    @Mock
//    private IArguments arguments;
//
//    @InjectMocks
//    private DomainPropertyTransformer transformer;
//
//    @Before
//    public void setup()
//    {
//        when(arguments.getDomainPrefix()).thenReturn("cod");
//    }
//
//    @Test
//    public void testShouldTransformCorrectly()
//    {
//        String domain = "miotipo";
//        when(domainHandler.findDomain(domain)).thenReturn(Optional.of(domain));
//
//        PropertyModel property = PropertyModel.builder()
//            .name("cod" + domain)
//            .tsType(BasicType.string())
//            .build();
//
//        PropertyContext ctx = PropertyContext.builder()
//            .className("className")
//            .dataHolder(property)
//            .build();
//
//        Optional<PropertyContext> result = transformer.transformProperty(ctx);
//
//        assertTrue(result.isPresent());
//
//        PropertyModel resultProperty = result.get().getDataHolder();
//
//        assertEquals(property.getName(), resultProperty.getName());
//        assertEquals(
//            new GenericBasicType(I_LOCALIZABLE_PROPERTY, BasicType.string()),
//            resultProperty.getTsType()
//        );
//
//        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), I_LOCALIZABLE_PROPERTY);
//    }
//
//    @Test
//    public void testShouldNotTransformIfDomainNotFound()
//    {
//        when(domainHandler.findDomain("miotipo")).thenReturn(Optional.empty());
//
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
//        Optional<PropertyContext> result = transformer.transformProperty(ctx);
//
//        assertFalse(result.isPresent());
//
//        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
//        verify(domainHandler, never()).registerUsedDomain(anyString());
//    }
//
//    @Test
//    public void testShouldNotTransformIfNotString()
//    {
//        PropertyModel property = PropertyModel.builder()
//            .name("codmiotipo")
//            .tsType(BasicType.number())
//            .build();
//
//        PropertyContext ctx = PropertyContext.builder()
//            .className("className")
//            .dataHolder(property)
//            .build();
//
//        Optional<PropertyContext> result = transformer.transformProperty(ctx);
//
//        assertFalse(result.isPresent());
//
//        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
//        verify(domainHandler, never()).registerUsedDomain(anyString());
//    }
//
//    @Test
//    public void testShouldNotTransformIfNotStartingWithPrefix()
//    {
//        PropertyModel property = PropertyModel.builder()
//            .name("desmiotipo")
//            .tsType(BasicType.string())
//            .build();
//
//        PropertyContext ctx = PropertyContext.builder()
//            .className("className")
//            .dataHolder(property)
//            .build();
//
//        Optional<PropertyContext> result = transformer.transformProperty(ctx);
//
//        assertFalse(result.isPresent());
//
//        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
//        verify(domainHandler, never()).registerUsedDomain(anyString());
//    }
//}
