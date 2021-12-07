package Logic;

import Logger.CustomLogger;
import Models.Building;
import Models.Passenger;
import Models.PassengerState;
import Models.MainWindow;

public class PassengerBrain {
    private final Passenger passenger;

    public PassengerBrain(Passenger passenger) {
        this.passenger = passenger;
    }

    public void Move(double dest) {
        CustomLogger.info("Passenger " + passenger.getName() + " started going");
        final Object mutex = new Object();
        double step = (Math.random() + 0.1) / 1000000;
        while (Math.abs(passenger.getX() - dest) > step){
                if(passenger.getX() > dest)
                    passenger.setX(passenger.getX() - step);
                else
                    passenger.setX(passenger.getX() + step);
            }

        CustomLogger.warn(passenger.getName() + " is coming!!!");
        passenger.setState(PassengerState.Waiting);
        CustomLogger.info("Passenger " + passenger.getName() + " stopped");
    }

    public void MoveOut(double dest){
        Building building = MainWindow.getInstance().getBuilding();

        CustomLogger.info("Passenger " + passenger.getName() + " started going");
        double step = (Math.random() + 0.1) / 1000000;
        while (Math.abs(passenger.getX() - dest) > step){
            if(passenger.getX() > dest)
                passenger.setX(passenger.getX() - step);
            else
                passenger.setX(passenger.getX() + step);
        }
    }
}
