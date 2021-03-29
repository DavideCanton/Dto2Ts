package com.mdcc.dto2ts.core.test.utils;

import com.mdcc.dto2ts.core.utils.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UtilsTest
{
    @ParameterizedTest
    @CsvSource({
        "Nome1,nome1.model",
        "NomeNome,nome-nome.model",
        "NOMENome,n-o-m-e-nome.model"
    })
    public void testGetClassName(String name, String expected)
    {
        assertThat(Utils.getClassName(name), is(expected));
    }
}
