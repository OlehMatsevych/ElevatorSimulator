package Logic;

import Models.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public abstract class BaseStrategy {
    protected Elevator elevator;
    protected BlockingQueue<Passenger> floorQueue;

    public BaseStrategy(Elevator elevator, BlockingQueue<Passenger> floorQueue){
        this.elevator = elevator;
        this.floorQueue = floorQueue;
    }

    public BlockingQueue<Passenger> getFloorQueue() {
        return floorQueue;
    }

    public void setFloorQueue(BlockingQueue<Passenger> floorQueue) {
        this.floorQueue = floorQueue;
    }
}
