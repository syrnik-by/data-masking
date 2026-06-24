package ru.psb.masking.core.planning;

import ru.psb.masking.core.schema.ForeignKeyModel;
import ru.psb.masking.core.schema.TableRef;

import java.util.*;

public class TopologicalSorter {

    public List<TableRef> sort(Set<TableRef> tables, List<ForeignKeyModel> foreignKeys) {
        Map<TableRef, Set<TableRef>> deps = new LinkedHashMap<>();
        for (TableRef t : tables) deps.put(t, new LinkedHashSet<>());
        for (ForeignKeyModel fk : foreignKeys) {
            TableRef from = fk.getFkTable();
            TableRef to = fk.getPkTable();
            if (deps.containsKey(from)) deps.get(from).add(to);
        }
        List<TableRef> sorted = new ArrayList<>();
        Set<TableRef> visited = new LinkedHashSet<>();
        Set<TableRef> inStack = new LinkedHashSet<>();
        for (TableRef t : tables) visit(t, deps, visited, inStack, sorted);
        return sorted;
    }

    private void visit(TableRef node, Map<TableRef, Set<TableRef>> deps,
                       Set<TableRef> visited, Set<TableRef> inStack, List<TableRef> sorted) {
        if (visited.contains(node)) return;
        if (inStack.contains(node)) throw new IllegalStateException("Circular FK dependency at " + node);
        inStack.add(node);
        for (TableRef dep : deps.getOrDefault(node, Set.of())) visit(dep, deps, visited, inStack, sorted);
        inStack.remove(node);
        visited.add(node);
        sorted.add(node);
    }
}
