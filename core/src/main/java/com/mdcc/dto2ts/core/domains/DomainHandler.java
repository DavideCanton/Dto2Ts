package com.mdcc.dto2ts.core.domains;

import com.mdcc.dto2ts.core.context.Arguments;
import com.oath.cyclops.util.ExceptionSoftener;
import cyclops.control.Try;
import cyclops.data.tuple.Tuple2;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.interfaces.StringSimilarity;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
