package Models;

import Interfaces.IBuilding;
import Interfaces.IElevator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Building implements IBuilding {
    private List<Elevator> elevators;
    private List<Floor> floors;
    private BlockingQueue<Passenger> passengersQueue;
    private CopyOnWriteArrayList<Passenger> leavingList;
    private Object updateLocker = new Object();

    public Building(List<Elevator> elevators, List<Floor> floors, BlockingQueue<Passenger> passengersQueue){
        this.elevators = elevators;
        this.floors = floors;
        this.passengersQueue = passengersQueue;
        leavingList = new CopyOnWriteArrayList<>();
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    @Override
    public void updateQueue(Passenger passenger) {
        Elevator minUsed = elevators.get(elevators.size() - 1);
        for (int i = elevators.size() - 1; i >= 0 ; --i) {
            var elevator = elevators.get(i);
            int using = elevator.getStrategy().getFloorQueue().size() +
                    elevator.getPassengers().size();
            int minUsing = minUsed.getStrategy().getFloorQueue().size() +
                    elevator.getPassengers().size();
            if(minUsing > using){
                minUsed = elevator;
            }
        }
        minUsed.getStrategy().getFloorQueue().add(passenger);
    }

    public BlockingQueue<Passenger> getPassengersQueue() {
            return passengersQueue;
    }

    public void setPassengersQueue(BlockingQueue<Passenger> passengersQueue) {
        this.passengersQueue = passengersQueue;
    }

    public void runAllThreads(){
        Thread factoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PassengerFactory factory = new PassengerFactory(floors.size());
                while (true){
                    if(passengersQueue.size() > 10) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Passenger passenger = factory.getPassenger();
                    Floor passangersFloor = floors.get(passenger.getSourceFloor());
                    passenger.setY(passangersFloor.getY());
                    passenger.setX(WorldInformation.getInstance().getWorldWidth());
                    passangersFloor.getPassengerList().add(passenger);
                    Thread passangerThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            passenger.getStrategy().Move(passangersFloor.getNextPassengerPosition());
                            synchronized (updateLocker) {
                                updateQueue(passenger);
                            }
                        }
                    });
                    passangerThread.start();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        factoryThread.start();
        for (var elevator: elevators) {
            Thread thread = new Thread(elevator);
            thread.start();
        }
    }

    public CopyOnWriteArrayList<Passenger> getLeavingList() {
        return leavingList;
    }
}
