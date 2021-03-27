package com.mdcc.dto2ts.json.main;

import java.util.*;

public class DependencyGraph
{
    private final Map<String, Set<String>> dependencies = new HashMap<>();

    public void registerClass(String name)
    {
        if (!this.dependencies.containsKey(name))
            this.dependencies.put(name, new HashSet<>());
    }

    public Set<String> getDependencies(String name)
    {
        return Collections.unmodifiableSet(this.dependencies.get(name));
    }

    public void registerDependency(String from, String to)
    {
        this.registerClass(from);
        this.registerClass(to);
        this.dependencies.get(from).add(to);
    }

    public List<String> reversedTopologicalOrder()
    {
        ArrayList<String> order = new ArrayList<>(this.dependencies.size());
        Set<String> permanentMarks = new HashSet<>();
        Set<String> temporaryMarks = new HashSet<>();

        while (permanentMarks.size() < dependencies.size())
        {
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            String n = dependencies.keySet().stream().filter(x -> !permanentMarks.contains(x)).findFirst().get();
            visit(n, order, permanentMarks, temporaryMarks);
        }

        return order;
    }

    private void visit(String n, ArrayList<String> order, Set<String> permanentMarks, Set<String> temporaryMarks)
    {
        if (permanentMarks.contains(n)) return;
        if (temporaryMarks.contains(n)) throw new RuntimeException("Not a DAG");

        temporaryMarks.add(n);

        for (String m : dependencies.get(n))
            visit(m, order, permanentMarks, temporaryMarks);

        temporaryMarks.remove(n);
        permanentMarks.add(n);
        order.add(n);
    }
}
