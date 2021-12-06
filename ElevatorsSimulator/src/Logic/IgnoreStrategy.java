package Logic;

import Interfaces.ElevatorStrategy;
import Models.*;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class IgnoreStrategy extends BaseStrategy implements ElevatorStrategy {
    private static Object isEmptyLocker = new Object();

    public IgnoreStrategy(Elevator elevator, BlockingQueue<Passenger> floorQueue) {
        super(elevator, floorQueue);
    }

    @Override
    public void Move() {
        //may be changed later
        System.out.println("Elevator started");
        WorldInformation wi = WorldInformation.getInstance();
        boolean isCalled = true;
        double step = 0.0000005;
        while(true) {
                try {
                    Passenger firstPassanger;
                    if(elevator.getPassengers().isEmpty()) {
                        while (true) {
                            System.out.println("Elevator waiting");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            synchronized (isEmptyLocker) {
                                if (!floorQueue.isEmpty()) {
                                    firstPassanger = floorQueue.poll();
                                    isCalled = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        firstPassanger = elevator.getPassengers().get(0);
                        isCalled = false;
                    }

                    if(isCalled) {
                        Floor firstCalledFloor = wi.getBuilding().getFloors().get(firstPassanger.getSourceFloor());
                        System.out.println("Elevator moving to " + firstPassanger.getSourceFloor() + " floor");
                        while (Math.abs(elevator.getY() - firstCalledFloor.getY()) > step) {
                            if (elevator.getY() < firstCalledFloor.getY()) {
                                elevator.setY(elevator.getY() + step);
                            } else {
                                elevator.setY(elevator.getY() - step);
                            }
                        }

                        System.out.println("Elevator stopped on " + firstPassanger.getSourceFloor() + " floor");
                        elevator.setCurrentFloor(firstCalledFloor);
                        elevator.Stop(firstCalledFloor);

                        if(!elevator.getPassengers().contains(firstPassanger))
                            continue;
                    }

                    //deliver
                    Floor destinationFloor = wi.getBuilding().getFloors().get(firstPassanger.getDestinationFloor());
                    System.out.println("Elevator moving to " + firstPassanger.getDestinationFloor() + " floor");
                    while (Math.abs(elevator.getY() - destinationFloor.getY()) > step) {
                        if (elevator.getY() < destinationFloor.getY()) {
                            elevator.setY(elevator.getY() + step);
                        } else {
                            elevator.setY(elevator.getY() - step);
                        }
                    }

                    System.out.println("Elevator stopped on " + firstPassanger.getDestinationFloor() + " floor");
                    elevator.setCurrentFloor(destinationFloor);
                    elevator.Stop(destinationFloor);
                    if(elevator.getPassengers().contains(firstPassanger))
                        elevator.getPassengers().remove(firstPassanger);
                } catch (NullPointerException e) {
                    continue;
                }
            }
    }
}
