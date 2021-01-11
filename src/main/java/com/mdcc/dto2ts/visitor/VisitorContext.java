package com.mdcc.dto2ts.visitor;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class VisitorContext {

    private final Set<String> visitedClasses = new HashSet<>();

    public void addClass(String className) {
        visitedClasses.add(className);
    }

    public Set<String> getVisitedClasses() {
        return Collections.unmodifiableSet(visitedClasses);
    }


}
