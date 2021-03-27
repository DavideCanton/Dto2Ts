package com.mdcc.dto2ts.json.main;

import cyclops.control.*;
import cyclops.reactive.*;
import io.swagger.models.*;
import io.swagger.parser.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

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
