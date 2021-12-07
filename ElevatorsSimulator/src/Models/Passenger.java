package Models;

import Logger.CustomLogger;
import Logic.PassengerStrategy;

import java.awt.*;

public class Passenger {
    private int weight;
    private int sourceFloor;
    private int destinationFloor;
    private PassengerState state;
    private double x;
    private double y;
    private PassengerStrategy strategy;
    private String name;

    public String getName() { return name; }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;


    public PassengerStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PassengerStrategy strategy) {
        this.strategy = strategy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSourceFloor(int sourceFloor) {
        this.sourceFloor = sourceFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public void setState(PassengerState state) {
        this.state = state;
    }

    public int getWeight() {
        return weight;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public PassengerState getState() {
        return state;
    }

    public Passenger(int weight, int sourceFloor, int destinationFloor, PassengerState state) {
        this.weight = weight;
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.state = state;
        int randomNumber = (int) (Math.random() * 10);
        this.image = MainWindow.getInstance()
                .getPassengerImages().get(randomNumber);
        this.name = MainWindow.getInstance()
                .getPassengerNames().get(randomNumber);
    }

    public void Leave(Elevator elevator){
        Building building = MainWindow.getInstance().getBuilding();
        y = building.getFloors().get(destinationFloor).getY();
        Thread leavingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setX(elevator.getX());
                setY(elevator.getY());
                strategy.MoveOut(MainWindow.getInstance().getPassengerWidth() +
                        MainWindow.getInstance().getWorldWidth());
                state = PassengerState.Left;
                building.getLeavingList().remove(Passenger.this);
                CustomLogger.info("Passenger " + name + " left");
            }
        });
        leavingThread.start();
        try {
            leavingThread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Enter(Elevator elevator){
        CustomLogger.info("Passenger: " + name +" goes to elevator " + elevator.getID());
        strategy.Move(elevator.getX());
        state = PassengerState.Moving;
        CustomLogger.info("Passenger: " + name + " entered to elevator " + elevator.getID());
    }
}
