package test.transformers;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.test.*;
import com.mdcc.dto2ts.transformers.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;
import test.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FlagPropertyTransformerTest extends BaseUnitTestClass
{
    @InjectMocks
    private FlagPropertyTransformer transformer;

    @Test
    public void testShouldTransformCorrectly()
    {
        TsPropertyModel property = new TsPropertyModel(
            "flgproperty",
            TsType.Number,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertTrue(result.isPresent());

        TsPropertyModel resultProperty = result.get().getPropertyModel();

        assertEquals(property.getName(), resultProperty.getName());
        assertEquals(TsType.Boolean, resultProperty.getTsType());
    }

    @Test
    public void testShouldNotTransformIfNotStartingWithPrefix()
    {
        TsPropertyModel property = new TsPropertyModel(
            "property",
            TsType.Number,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());
    }
}
