package com.mdcc.dto2ts.json.main;

import com.google.common.base.*;
import cyclops.control.*;
import cz.habarta.typescript.generator.emitter.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
@Slf4j
public class ModelEmitter
{
    @Autowired
    private JsonArguments arguments;
    @Autowired
    private Emitter emitter;

    public Try<Void, Throwable> writeModel(TsBeanModel bean)
    {
        String fileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, bean.getName().getSimpleName()) + ".model.ts";

        return Try.withResources(
            () -> new PrintWriter(new FileWriter(arguments.getOutputFolder() + "/" + fileName)),
            writer -> {
                TsModel model = new TsModel()
                    .withBeans(Collections.singletonList(bean));

                log.info("Writing file " + fileName);

                emitter.emit(
                    model,
                    writer,
                    null,
                    true,
                    true,
                    0
                );

                return null;
            }
        );
    }

}
