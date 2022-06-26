package com.mdcc.dto2ts.json.main;

import cyclops.control.Try;
import cyclops.reactive.ReactiveSeq;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MainHandler
{
    @Autowired
    private JsonArguments arguments;
    @Autowired
    private ModelCrawler crawler;
    @Autowired
    private ModelEmitter emitter;

    public Try<Void, Throwable> generate()
    {
        return Try.<Swagger, Throwable>withCatch(() -> new SwaggerParser().read(arguments.getJson()))
            .mapOrCatch(swagger -> {
                if (swagger == null)
                {
                    String message = "Swagger file not found at " + arguments.getJson();
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }

                return crawler.generateModels(swagger);
            })
            .flatMapOrCatch(beans ->
                ReactiveSeq.fromIterable(beans.values())
                    .reduce(
                        Try.success(null),
                        (cur, bean) -> cur.flatMapOrCatch(__ -> emitter.writeModel(bean))
                    )
            );
    }


}
