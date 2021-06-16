package model;

import solver.Configuration;
import java.util.Collection;

public class CheckersConfig implements Configuration {
    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }
}
