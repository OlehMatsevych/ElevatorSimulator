package Logic;

import Models.Building;
import Models.Passenger;
import Models.PassengerState;
import Models.WorldInformation;

public class PassengerStrategy {
    private Passenger passenger;

    public PassengerStrategy(Passenger passenger) {
        this.passenger = passenger;
    }

    public void Move(double dest){
        System.out.println("Passenger started going");
        double step = 0.0000005;
        while (Math.abs(passenger.getX() - dest) > step){
                if(passenger.getX() > dest)
                    passenger.setX(passenger.getX() - step);
                else
                    passenger.setX(passenger.getX() + step);
            }

        passenger.setState(PassengerState.Waiting);
        System.out.println("Passenger stopped");
    }

    public void MoveOut(double dest){
        Building building = WorldInformation.getInstance().getBuilding();

        System.out.println("Passenger started going");
        double step = 0.0000005;
        while (Math.abs(passenger.getX() - dest) > step){
            if(passenger.getX() > dest)
                passenger.setX(passenger.getX() - step);
            else
                passenger.setX(passenger.getX() + step);
        }
    }
}
