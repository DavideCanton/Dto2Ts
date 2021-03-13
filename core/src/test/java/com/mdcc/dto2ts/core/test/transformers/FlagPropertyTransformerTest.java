//package com.mdcc.dto2ts.core.test.transformers;
//
//import com.mdcc.dto2ts.core.context.*;
//import com.mdcc.dto2ts.core.context.types.*;
//import com.mdcc.dto2ts.core.test.*;
//import com.mdcc.dto2ts.core.transformers.*;
//import org.junit.*;
//import org.junit.runner.*;
//import org.mockito.*;
//import org.mockito.junit.*;
//
//import java.util.*;
//
//import static org.junit.Assert.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class FlagPropertyTransformerTest extends BaseUnitTestClass
//{
//    @InjectMocks
//    private FlagPropertyTransformer transformer;
//
//    @Test
//    public void testShouldTransformCorrectly()
//    {
//        PropertyModel property = PropertyModel.builder()
//            .name("flgproperty")
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
//        assertTrue(result.isPresent());
//
//        PropertyModel resultProperty = result.get().getDataHolder();
//
//        assertEquals(property.getName(), resultProperty.getName());
//        assertEquals(BasicType.bool(), resultProperty.getTsType());
//    }
//
//    @Test
//    public void testShouldNotTransformIfNotStartingWithPrefix()
//    {
//        PropertyModel property = PropertyModel.builder()
//            .name("property")
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
//    }
//}
