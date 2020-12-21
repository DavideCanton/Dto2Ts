package com.mdcc.dto2ts.domains;

import cyclops.control.*;
import cyclops.data.tuple.*;
import info.debatty.java.stringsimilarity.*;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@Getter
public class DomainHandler {
    private List<String> domains = new ArrayList<>();
    private final NormalizedLevenshtein algorithm = new NormalizedLevenshtein();
    private double threshold;
    private Set<String> domainsUsed = new TreeSet<>();

    public DomainHandler(double threshold) {
        this.threshold = threshold;
    }

    public Try<Void, IOException> loadPropertiesFrom(String path) {
        return Try.withResources(
            () -> new FileReader(path),
            r -> {
                val p = new Properties();
                p.load(r);
                domains = p
                    .keySet()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                return null;
            },
            IOException.class
        );
    }

    public Optional<String> isDomain(String propertyName) {
        Comparator<Tuple2<String, Double>> c = Comparator.comparingDouble(Tuple2::_2);
        String lowercaseProperty = propertyName.toLowerCase(Locale.ROOT);

        return this.domains.stream()
            .map(w -> Tuple2.of(w, algorithm.similarity(w.toLowerCase(Locale.ROOT), lowercaseProperty)))
            .max(c)
            .filter(t -> t._2() >= threshold)
            .map(Tuple2::_1);
    }

    public void registerUsedDomain(String domain) {
        this.domainsUsed.add(domain);
    }
}