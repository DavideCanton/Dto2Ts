package com.mdcc.dto2ts.json.main;

import com.google.common.base.*;
import com.mdcc.dto2ts.core.utils.*;
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
    @Autowired
    private CodeWriteUtils codeWriteUtils;

    public Try<Void, Throwable> writeModel(TsBeanModel bean)
    {
        String fileName =
            bean.isClass() ?
                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, bean.getName().getSimpleName()) + ".model.ts" :
                arguments.getVisitorName() + ".visitor.ts";

        return Try.withResources(
            () -> new PrintWriter(new FileWriter(arguments.getOutputFolder() + "/" + fileName)),
            writer -> {
                TsModel model = new TsModel()
                    .withBeans(Collections.singletonList(bean));

                log.info("Writing file " + fileName);

                StringWriter sw = new StringWriter();

                String imports = codeWriteUtils.generateClassImport(bean.getName().getSimpleName());
                sw.write(imports);

                emitter.emit(
                    model,
                    sw,
                    null,
                    true,
                    true,
                    0
                );

                writer.println(codeWriteUtils.setDefaultForArrays(sw.toString()));

                return null;
            }
        );
    }

}
