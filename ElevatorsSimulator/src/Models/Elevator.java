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
    private int maximumWeight;
    private Floor actualFloor;
    private ElevatorStrategy strategy;
    private CopyOnWriteArrayList<Passenger> passengers;
    private ElevatorState state;
    private double exitDoorWidth;
    private double constDoorWidth = 25.0;

    @Override
    public void run() {
        strategy.Move();
    }

    public Elevator(int maxWeight, Floor initialFloor) {
        this.maximumWeight = maxWeight;
        actualFloor = initialFloor;
        passengers = new CopyOnWriteArrayList<>();
    }

    public Elevator(double x, double y) { this.x = x; this.y = y; }

    public Elevator(double x, double y, int maxWeight, Floor currentFloor,
                    ElevatorStrategy strategy, CopyOnWriteArrayList<Passenger> passengers,
                    ElevatorState state, double doorWidth, double constDoorWidth) {
        this.x = x;
        this.y = y;
        this.maximumWeight = maxWeight;
        this.actualFloor = currentFloor;
        this.strategy = strategy;
        this.passengers = passengers;
        this.state = state;
        this.exitDoorWidth = doorWidth;
        this.constDoorWidth = constDoorWidth;
    }

    public int getMaxWeight() {
        return maximumWeight;
    }

    public double getConstDoorWidth() {
        return constDoorWidth;
    }

    public double getDoorWidth() {
        return exitDoorWidth;
    }

    public Floor getCurrentFloor() {
        return actualFloor;
    }

    public CopyOnWriteArrayList<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public ElevatorStrategy getStrategy() {
        return strategy;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public ElevatorState getState() {
        return state;
    }

    public int getMaximumWeight() {
        return maximumWeight;
    }

    public Floor getActualFloor() {
        return actualFloor;
    }

    public double getExitDoorWidth() {
        return exitDoorWidth;
    }

    public void setExitDoorWidth(double exitDoorWidth) {
        this.exitDoorWidth = exitDoorWidth;
    }

    public void setActualFloor(Floor actualFloor) {
        this.actualFloor = actualFloor;
    }

    public void setMaximumWeight(int maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maximumWeight = maxWeight;
    }

    public void setConstDoorWidth(double constDoorWidth) {
        this.constDoorWidth = constDoorWidth;
    }

    public void setDoorWidth(double doorWidth) {
        this.exitDoorWidth = doorWidth;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.actualFloor = currentFloor;
    }

    public void setPassengers(CopyOnWriteArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setState(ElevatorState state) {
        state = state;
    }

    @Override
    public void setStrategy(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }

    public void Stop(Floor floor) {
        OpenDoors();
        floor.ElevatorArrived(this);
        CloseDoors();
    }

    public void OpenDoors() {
        double step = 0.0000001;
        while (exitDoorWidth > 0) {
            exitDoorWidth -= step;
        }
        state = ElevatorState.Stopped;
    }

    public void CloseDoors() {
        double step = 0.0000001;
        while (exitDoorWidth < constDoorWidth) {
            exitDoorWidth += step;
        }
        state = ElevatorState.Moving;
    }

    public boolean canEnter(int weight) {
        int passengersWeight = passengers
                .stream()
                .mapToInt(x -> x.getWeight())
                .sum();

        return passengersWeight + weight <= maximumWeight;
    }

}
