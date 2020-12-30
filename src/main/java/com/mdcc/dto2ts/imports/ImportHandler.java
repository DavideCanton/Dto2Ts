package com.mdcc.dto2ts.imports;

import com.mdcc.dto2ts.utils.*;
import cyclops.data.tuple.*;
import cyclops.reactive.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;

@Component
public class ImportHandler
{
    private final Map<String, Map<String, Set<String>>> imports = new TreeMap<>();

    public void registerClassLibraryImport(String className, String symbol)
    {
        Map<String, Set<String>> classImports = ensureClassImportsExists(className);
        String pathLibraryDecorator = ImportNames.getLibraryImport(symbol);
        Set<String> set = ensureSymbolImportExists(classImports, pathLibraryDecorator);
        set.add(symbol);
    }

    public void registerOtherClassImport(String className, String symbol)
    {
        Map<String, Set<String>> classImports = ensureClassImportsExists(className);
        String pathArgument = "./" + Utils.getClassName(symbol);
        Set<String> set = ensureSymbolImportExists(classImports, pathArgument);
        set.add(symbol);
    }

    public void registerExternalImport(String className, String symbol, String pathArgument)
    {
        Map<String, Set<String>> classImports = ensureClassImportsExists(className);
        Set<String> set = ensureSymbolImportExists(classImports, pathArgument);
        set.add(symbol);
    }


    public ReactiveSeq<Tuple2<String, List<String>>> getImportsFor(String className)
    {
        return ReactiveSeq.fromStream(this.imports.getOrDefault(className, new HashMap<>())
            .entrySet()
            .stream()
            .map(e -> Tuple2.of(e.getKey(), new ArrayList<>(e.getValue()))));
    }

    private Map<String, Set<String>> ensureClassImportsExists(String className)
    {
        Predicate<String> isRelative = s -> s.charAt(0) == '.';

        return imports.computeIfAbsent(className, __ -> new TreeMap<>(Comparator.comparing(
            s -> s,
            (o1, o2) -> {
                val isRelative1 = isRelative.test(o1);
                val isRelative2 = isRelative.test(o2);

                if (isRelative1 == isRelative2)
                    return o1.compareTo(o2);
                else
                    return isRelative1 ? 1 : -1;
            }
        )));
    }

    private Set<String> ensureSymbolImportExists(Map<String, Set<String>> classImports, String pathLibraryDecorator)
    {
        return classImports.computeIfAbsent(pathLibraryDecorator, __ -> new TreeSet<>());
    }
}
