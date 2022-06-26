package com.mdcc.dto2ts.core.test.utils;

import com.mdcc.dto2ts.core.utils.Utils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
