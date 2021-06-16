package solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** @author Paige Machulsky */
public class Solver {
    private int totalconfig = 0;
    private int uniqueconfig = 0;

    public List<Configuration> solve(Configuration config) {
        totalconfig = 1;
        List<Configuration> queue = new LinkedList<>();
        queue.add(config);
        HashMap<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(config, null);
        boolean containsSolution = false;
        Configuration solution = null;
        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if (current.isSolution()) {
                containsSolution = true;
                solution = current;
                break;
            }
            for (Configuration neighbor : current.getNeighbors()) {
                if (!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
                totalconfig += 1;
            }
        }
        List<Configuration> path = new LinkedList<>();
        if (containsSolution) {
            while (!solution.equals(config)) {
                path.add(0, solution);
                solution = predecessors.get(solution);
            }
            path.add(0, config);
            uniqueconfig = predecessors.size();
            return path;
        }
        uniqueconfig = predecessors.size();
        return null;
    }

    public int getTotalConfig() {
        return totalconfig;
    }

    public int getUniqueConfig() {
        return uniqueconfig;
    }
}
