package Models;

import Interfaces.BaseElevatorStrategy;
import Interfaces.ElevatorStrategy;
import Interfaces.IElevator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Elevator extends BaseElevator implements Runnable {
    private double x, y;
    private int maxWeight;
    private Floor currentFloor;
    private ElevatorStrategy strategy;
    private CopyOnWriteArrayList<Passenger> passengers;
    private ElevatorState state;
    private double doorWidth;
    private double constDoorWidth = 25.0;

    public double getDoorWidth() {
        return doorWidth;
    }

    public void setDoorWidth(double doorWidth) {
        this.doorWidth = doorWidth;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public CopyOnWriteArrayList<Passenger> getPassengers() {
        return passengers;
    }
    public void setPassengers(CopyOnWriteArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Elevator(int maxWeight, Floor initialFloor)
    {
        this.maxWeight = maxWeight;
        currentFloor = initialFloor;
        passengers = new CopyOnWriteArrayList<>();
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public ElevatorState getState() {
        return state;
    }

    @Override
    public void setState(ElevatorState state) {
        state = state;
    }

    public void Stop(Floor floor)
    {
        OpenDoors();
        floor.ElevatorArrived(this);
        CloseDoors();
    }

    public void OpenDoors()
    {
        double step = 0.0000001;
        while (doorWidth > 0){
            doorWidth -= step;
        }
        state = ElevatorState.Stopped;
    }

    public void CloseDoors()
    {
        double step = 0.0000001;
        while (doorWidth < constDoorWidth){
            doorWidth += step;
        }
        state = ElevatorState.Moving;
    }


    @Override
    public void setStrategy(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public ElevatorStrategy getStrategy() {
        return strategy;
    }

    public boolean canEnter(int weight){
        int passengersWeight = passengers
                .stream()
                .mapToInt(x -> x.getWeight())
                .sum();

        return  passengersWeight + weight <= maxWeight;
    }

    @Override
    public void run() {
        strategy.Move();
    }
}
