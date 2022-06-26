package com.mdcc.dto2ts.core.context;

import com.mdcc.dto2ts.core.decorators.PropertyDecorator;
import com.mdcc.dto2ts.core.transformers.PropertyTransformer;
import com.mdcc.dto2ts.core.transformers.TransformAfterDecorate;
import com.mdcc.dto2ts.core.transformers.TransformBeforeDecorate;
import cyclops.data.tuple.Tuple2;
import cyclops.reactive.ReactiveSeq;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DefaultPropertyTransformationExecutor<P, C> implements PropertyTransformationExecutor<P, C>
{
    @Autowired
    @TransformAfterDecorate
    private List<PropertyTransformer> afterDecoratePropertyTransformers;

    @Autowired
    @TransformBeforeDecorate
    private List<PropertyTransformer> beforeDecoratePropertyTransformers;

    @Autowired
    private List<PropertyDecorator> propertyDecorators;

    protected abstract PropertyContext buildContext(P property, C context);

    protected abstract P unwrapContext(PropertyContext property);

    @Override
    public List<P> transformProperties(List<P> properties, C context)
    {
        return properties
            .stream()
            .map(p -> this.buildContext(p, context))
            .map(c -> applyWhileNull(beforeDecoratePropertyTransformers, c))
            .map(c -> applyWhileNull(propertyDecorators, c))
            .map(c -> applyWhileNull(afterDecoratePropertyTransformers, c))
            .map(this::unwrapContext)
            .collect(Collectors.toList());
    }


    private PropertyContext applyWhileNull(List<? extends ContextTransformer> list, PropertyContext startingValue)
    {
        return ReactiveSeq.fromList(list)
            .reduce(
                Tuple2.of(false, startingValue),
                (acc, t) -> Boolean.TRUE.equals(acc._1()) ? acc : (
                    t.transformContext(startingValue)
                        .map(c -> Tuple2.of(true, c))
                        .orElse(acc)
                )
            )
            ._2();
    }
}
