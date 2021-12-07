package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Floor {
    private CopyOnWriteArrayList<Passenger> passengerList;
    private double elevatorPoints;
    //public Building buildingReference;
    private double storey_Height;
    private double yCoordinate;
    private static Object elevatorLocker = new Object();

    public Floor(CopyOnWriteArrayList<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public Floor(double elevatorPoints) {
        this.elevatorPoints = elevatorPoints;
    }

    public Floor() {
        passengerList = new CopyOnWriteArrayList<>();
    }

    public double getFloorHeight() {
        return storey_Height;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public static Object getElevatorLocker() {
        return elevatorLocker;
    }

    public void setFloorHeight(double floorHeight) {
        this.storey_Height = floorHeight;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public static void setElevatorLocker(Object elevatorLocker) {
        Floor.elevatorLocker = elevatorLocker;
    }

    public CopyOnWriteArrayList<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(CopyOnWriteArrayList<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public double getElevatorPoints() {
        return elevatorPoints;
    }

    public void setElevatorPoints(double elevatorPoints) {
        this.elevatorPoints = elevatorPoints;
    }

    public void setHeight(double height) {
        storey_Height = height;
    }

    public double getHeight() {
        return storey_Height;
    }

    public double getY() {
        return yCoordinate;
    }

    public void setY(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getNextPassengerPosition() {
        double pos;
        MainWindow wi = MainWindow.getInstance();
        double leftOffset = wi.get_xMargin() + wi.getElevatorWidth();
        leftOffset *= wi.getElevatorsNum();
        leftOffset += wi.getElevatorWidth() + wi.getPassengerMargin();

        double passengersCount = passengerList.stream()
                .filter(x -> x.getState() == PassengerState.Waiting ||
                        x.getState() == PassengerState.Spawned).count() - 1;
        double passengerOffset = passengersCount * (wi.getPassengerWidth() + wi.getPassengerMargin());
        pos = leftOffset + passengerOffset;

        return pos;
    }

    public void ElevatorSourceFloorArrivedIgnoreStrategy(Elevator elevator,
                                                         Passenger passengerToMove) {
        Building building = MainWindow.getInstance().getBuilding();
        int floorIndex = MainWindow.getInstance().getBuilding().getFloors().indexOf(this);
        for (int i = 0; i < passengerList.size(); ++i) {
            Passenger passenger = passengerList.get(i);
            if (passengerToMove.getDestinationFloor() == passenger.getDestinationFloor()) {
                passengerList.remove(passenger);
                elevator.getPassengers().add(passenger);
                building.getLeavingList().add(passenger);
                --i;
            }
        }
    }

    public void ElevatorDestinationFloorArrivedIgnoreStrategy(Elevator elevator) {
        int floorIndex = MainWindow.getInstance().getBuilding().getFloors().indexOf(this);
        for (int i = 0; i < elevator.getPassengers().size(); ++i) {
            Passenger passenger = elevator.getPassengers().get(i);
            passenger.setState(PassengerState.Leaving);
            passengerList.add(passenger);
            elevator.getPassengers().remove(passenger);
            --i;
        }
    }

    public void ElevatorArrived(Elevator elevator) {
        Building building = MainWindow.getInstance().getBuilding();
        int floorIndex = building.getFloors().indexOf(this);
        for (int i = 0; i < elevator.getPassengers().size(); ++i) {
            Passenger p = elevator.getPassengers().get(i);
            if (p.getDestinationFloor() == floorIndex) {
                p.setState(PassengerState.Leaving);
                elevator.getPassengers().remove(p);
                building.getLeavingList().add(p);
                p.Leave(elevator);
                --i;
            }
        }

        synchronized (this) {
            List<Passenger> addedPassengers = new ArrayList<>();
            for (int i = 0; i < passengerList.size(); ++i) {
                Passenger p = passengerList.get(i);
                if (elevator.canEnter(p.getWeight())) {
                    elevator.getPassengers().add(p);
                    addedPassengers.add(p);
                    p.setState(PassengerState.Entering);
                }
            }

            for (var p : addedPassengers) {
                p.Enter(elevator);
                passengerList.remove(p);
            }
        }
        //RearrangePassengers();
    }

    public void RearrangePassengers() {
        List<Passenger> backUp = new ArrayList<>(passengerList);
        passengerList.clear();
        final Object mutex = new Object();
        Thread rearrangeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (var p: backUp) {
                    synchronized (mutex) {
                        p.getStrategy().Move(getNextPassengerPosition());
                    }
                }
            }
        });
        rearrangeThread.start();
    }
}
