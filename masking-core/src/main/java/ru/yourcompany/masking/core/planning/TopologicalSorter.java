package ru.yourcompany.masking.core.planning;

import ru.yourcompany.masking.core.schema.TableModel;

import java.util.*;

/**
 * Topological sort for tables based on FK dependencies.
 * Tables with no dependencies come first.
 */
public class TopologicalSorter {

    public List<String> sort(List<TableModel> tables) {
        Map<String, Set<String>> deps = new HashMap<>();
        for (TableModel t : tables) {
            Set<String> refs = new HashSet<>();
            t.foreignKeys().forEach(fk -> refs.add(fk.referencedTable()));
            deps.put(t.name(), refs);
        }

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> names = new HashSet<>(deps.keySet());

        while (!deps.isEmpty()) {
            List<String> ready = deps.entrySet().stream()
                    .filter(e -> names.containsAll(deps.get(e.getKey()))
                            && e.getValue().stream().allMatch(visited::contains))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .toList();

            if (ready.isEmpty()) {
                // Cyclic deps — add remaining as-is
                result.addAll(deps.keySet().stream().sorted().toList());
                break;
            }

            result.addAll(ready);
            ready.forEach(t -> { visited.add(t); deps.remove(t); });
        }

        return result;
    }
}
