package com.mdcc.dto2ts.java.common;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.utils.*;
import cyclops.reactive.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;
import java.util.regex.*;

@Component
public class ClassRenamer implements SymbolTable.CustomTypeNamingFunction
{
    private final List<Pattern> patterns = new ArrayList<>(10);

    @Autowired
    private Arguments arguments;

    @PostConstruct()
    public void init()
    {
        patterns.add(Pattern.compile(arguments.getSuffixToRemove(), Pattern.CASE_INSENSITIVE));
        patterns.add(Pattern.compile(arguments.getPrefixToRemove(), Pattern.CASE_INSENSITIVE));
    }

    @Override
    public String getName(String className, String classSimpleName)
    {
        return ReactiveSeq.fromList(patterns).reduce(classSimpleName, (s, p) -> p.matcher(s).replaceAll(""));
    }

    public String getName(TsBeanModel bean)
    {
        return getName(
            bean.getOrigin().getCanonicalName(),
            Utils.getClassNameFromTsQualifiedName(bean.getName().getSimpleName())
        );
    }
}
