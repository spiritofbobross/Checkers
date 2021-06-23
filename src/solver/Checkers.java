package solver;

import model.CheckersConfig;
import java.util.List;

public class Checkers {
    public static void main(String[] args) {
        CheckersConfig init = new CheckersConfig(null);
        System.out.println(init);
        Solver solver = new Solver();
        List<Configuration> path = solver.solve(init);
        System.out.println("Total configs: " + solver.getTotalConfig());
        System.out.println("Unique configs: " + solver.getUniqueConfig());
        if (path == null) {
            System.out.println("No solution");
        } else {
            for (int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ":");
                System.out.println(path.get(i).toString());
            }
        }
    }
}
