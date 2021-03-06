package com.mdcc.dto2ts.core.transformers;

import org.springframework.beans.factory.annotation.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Qualifier
public @interface TransformBeforeDecorate
{
}
