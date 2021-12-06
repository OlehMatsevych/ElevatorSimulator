package Models;

import Interfaces.ElevatorStrategy;

public abstract class BaseElevator {
    public abstract double getY();
    public abstract void setY(double y);
    public abstract double getX();
    public abstract void setX(double x);
    public abstract ElevatorState getState();

    public abstract void setState(ElevatorState state);
    public abstract void OpenDoors();
    public abstract void CloseDoors();
    public abstract void setStrategy(ElevatorStrategy strategy);
    public abstract ElevatorStrategy getStrategy();
}
