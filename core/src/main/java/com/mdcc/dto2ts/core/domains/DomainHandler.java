package com.mdcc.dto2ts.core.domains;

import com.mdcc.dto2ts.core.context.*;
import com.oath.cyclops.util.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import info.debatty.java.stringsimilarity.*;
import info.debatty.java.stringsimilarity.interfaces.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

@Component
public class DomainHandler
{
    private final StringSimilarity algorithm = new NormalizedLevenshtein();
    @Getter
    private final Set<String> domainsUsed = new TreeSet<>();
    private List<String> domains = new ArrayList<>();

    @Autowired
    private Arguments arguments;

    @PostConstruct
    public void init() throws IOException
    {
        try (FileReader fr = new FileReader(this.arguments.getDomainFile()))
        {
            this.loadPropertiesFrom(fr).onFail(ExceptionSoftener::throwSoftenedException);
        }
    }

    public Try<Void, IOException> loadPropertiesFrom(Reader reader)
    {
        return Try.runWithCatch(
            () -> {
                val p = new Properties();
                p.load(reader);
                domains = p
                    .keySet()
                    .stream()
                    .map(Object::toString)
                    .map(s -> {
                        String domainFilePrefix = arguments.getDomainFilePrefix();
                        if (!domainFilePrefix.endsWith("."))
                            domainFilePrefix += ".";
                        return s.replaceAll(String.format("^%s", domainFilePrefix), "");
                    })
                    .collect(Collectors.toList());
            },
            IOException.class
        );
    }

    public Optional<String> findDomain(String propertyName)
    {
        Comparator<Tuple2<String, Double>> c = Comparator.comparingDouble(Tuple2::_2);
        String lowercaseProperty = propertyName.toLowerCase(Locale.ROOT);

        return this.domains.stream()
            .map(w -> Tuple2.of(w, algorithm.similarity(w.toLowerCase(Locale.ROOT), lowercaseProperty)))
            .max(c)
            .filter(t -> t._2() >= this.arguments.getThreshold())
            .map(Tuple2::_1);
    }

    public void registerUsedDomain(String domain)
    {
        this.domainsUsed.add(domain);
    }
}
