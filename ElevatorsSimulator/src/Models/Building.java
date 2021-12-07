package Models;

import Interfaces.IBuilding;

import java.util.*;
import java.util.concurrent.*;

public class Building implements IBuilding {
    private List<Elevator> elevators;
    private List<Floor> floors;
    private BlockingQueue<Passenger> passengersQueue;
    private final CopyOnWriteArrayList<Passenger> leavingList;
    private final Object updateLocker = new Object();

    public Building(List<Elevator> elevators, List<Floor> floors, BlockingQueue<Passenger> passengersQueue) {
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
        Integer[] passengerSizesArray = new Integer[elevators.size()];
        for (int i = 0 ; i < elevators.size(); i++) {
            passengerSizesArray[i] = elevators.get(i).getStrategy().getFloorQueue().size()+
                    elevators.get(i).getPassengers().size();
        }
        int minIndex = elevators.size() - 1;
        for (int i = elevators.size() - 1; i >= 0; --i) {
            if (passengerSizesArray[minIndex] > passengerSizesArray[i]) {
                minIndex= i;
            }
        }
        elevators.get(minIndex).getStrategy().getFloorQueue().add(passenger);
    }

    public BlockingQueue<Passenger> getPassengersQueue() {
        return passengersQueue;
    }

    public void setPassengersQueue(BlockingQueue<Passenger> passengersQueue) {
        this.passengersQueue = passengersQueue;
    }

    public void runAllThreads() throws InterruptedException {
        Thread factoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PassengerFactory factory = new PassengerFactory(floors.size());
                Random random = new Random();
                ExecutorService executor = Executors.newFixedThreadPool(10);
                while (true) {
                    if (passengersQueue.size() > 10) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Passenger passenger = factory.getPassenger();
                    Floor passengersFloor = floors.get(passenger.getSourceFloor());
                    passenger.setY(passengersFloor.getY());
                    passenger.setX(MainWindow.getInstance().getWorldWidth());
                    passengersFloor.getPassengerList().add(passenger);
                    Future<?> f = executor.submit(() -> {
                                passenger.getStrategy().Move(passengersFloor.getNextPassengerPosition());
                                synchronized (updateLocker) {
                                    updateQueue(passenger);
                                }
                            }
                    );
                    try {
                        Thread.sleep(random.nextInt(10000) + 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        executor.shutdownNow();
                    }
                }
            }
        });
        factoryThread.start();
        for (var elevator : elevators) {
            Thread thread = new Thread(elevator);
            thread.start();
        }
    }
    public CopyOnWriteArrayList<Passenger> getLeavingList() {
        return leavingList;
    }
}
