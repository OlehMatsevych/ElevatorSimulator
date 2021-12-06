package Interfaces;

import Models.Passenger;

import java.util.concurrent.BlockingQueue;

public interface ElevatorStrategy {
    void Move();
    BlockingQueue<Passenger> getFloorQueue();
    void setFloorQueue(BlockingQueue<Passenger> floorQueue);
}
