package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.java.common.CodeDecorationUtils;
import cz.habarta.typescript.generator.emitter.TsBeanModel;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class ModelCrawler
{
    @Autowired
    private JsonArguments arguments;
    @Autowired
    private ModelGenerator generator;
    @Autowired
    private CodeDecorationUtils codeDecorationUtils;

    public Map<String, TsBeanModel> generateModels(Swagger swagger)
    {
        val definitions = swagger.getDefinitions();

        Map<String, TsBeanModel> generatedBeans = new HashMap<>();
        Set<String> modelsToVisit = new HashSet<>();

        String rootModel = arguments.getRootModel();
        if (rootModel == null || "".equals(rootModel))
        {
            log.info("RootModel argument empty, defaulting to all models");
            modelsToVisit.addAll(definitions.keySet());
        }
        else
        {
            log.info("RootModel argument set to " + rootModel);
            modelsToVisit.add(rootModel);
        }

        visitAndCreateModels(modelsToVisit, generatedBeans, definitions);

        if (arguments.isCreateVisitor())
        {
            val visitorBean = codeDecorationUtils.createVisitorBean();
            generatedBeans.put(visitorBean.getName().getSimpleName(), visitorBean);
        }

        return generatedBeans;
    }

    private void visitAndCreateModels(
        Set<String> modelsToVisit,
        Map<String, TsBeanModel> generatedBeans,
        Map<String, Model> definitions
    )
    {
        while (!modelsToVisit.isEmpty())
        {
            String modelToVisit = modelsToVisit
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Empty set"));

            modelsToVisit.remove(modelToVisit);

            // skip already generated
            if (generatedBeans.containsKey(modelToVisit)) continue;

            log.debug("Visiting model " + modelToVisit);

            Model model = definitions.get(modelToVisit);
            if (model == null) continue;

            log.debug("Found definition for " + modelToVisit);

            generatedBeans.put(modelToVisit, generator.createBean(model));

            for (val property : model.getProperties().values())
            {
                String propertyType = extractType(property);

                // avoid self-loops and translate only model types
                if (!propertyType.equals(modelToVisit) && definitions.containsKey(propertyType))
                {
                    log.debug("Found property of type " + propertyType + ", adding to visit models");
                    modelsToVisit.add(propertyType);
                }
            }
        }
    }

    private String extractType(Property p)
    {
        if (p instanceof ArrayProperty)
            return extractType(((ArrayProperty) p).getItems());
        else if (p instanceof RefProperty)
            return ((RefProperty) p).getSimpleRef();
        else
            return p.getType();
    }
}
