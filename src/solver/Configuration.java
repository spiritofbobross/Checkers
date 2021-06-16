package solver;

import java.util.Collection;

public interface Configuration {
    boolean isSolution();

    Collection<Configuration> getNeighbors();
}
