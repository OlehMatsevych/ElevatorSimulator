package Logic;

import Interfaces.ElevatorStrategy;
import Models.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PickingStrategy extends BaseStrategy implements ElevatorStrategy {
    private static final Object isEmptyLocker = new Object();

    public PickingStrategy(Elevator elevator, BlockingQueue<Passenger> floorQueue) {
        super(elevator, floorQueue);
    }

    @Override
    public void Move() {
        //may be changed later
        WorldInformation wi = WorldInformation.getInstance();
        boolean isCalled = true;
        double step = 0.0000005;
        var floors = new ArrayList<>(wi.getBuilding().getFloors());
        while (true) {
            Passenger firstPassenger = null;
            if(elevator.getPassengers().isEmpty()) {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (isEmptyLocker) {
                        if (!floorQueue.isEmpty()) {
                            firstPassenger = floorQueue.poll();
                            isCalled = true;
                            break;
                        }
                    }
                }
            }
            else {
                if(elevator.getPassengers().isEmpty())
                    continue;
                firstPassenger = elevator.getPassengers().get(0);
                isCalled = false;
            }

            if(isCalled) {
                Floor firstCalledFloor = floors.get(firstPassenger.getSourceFloor());
                while (Math.abs(elevator.getY() - firstCalledFloor.getY()) > step) {
                    if (elevator.getY() < firstCalledFloor.getY()) {
                        elevator.setY(elevator.getY() + step);
                    } else {
                        elevator.setY(elevator.getY() - step);
                    }
                }

                elevator.Stop(firstCalledFloor);
                if(!elevator.getPassengers().contains(firstPassenger))
                    continue;
            }

            Floor destinationFloor = floors.get(firstPassenger.getDestinationFloor());
            var interFloors = floors.stream()
                    .filter(x -> !x.getPassengerList().isEmpty())
                    .collect(Collectors.toList());

            while (Math.abs(elevator.getY() - destinationFloor.getY()) > step) {
                    if (elevator.getY() < destinationFloor.getY()) {
                        elevator.setY(elevator.getY() + step);
                    } else {
                        elevator.setY(elevator.getY() - step);
                    }
                    for (var x: interFloors) {
                        if(Math.abs(elevator.getY() - x.getY()) <= step)
                            elevator.Stop(x);
                    }
            }
            elevator.Stop(destinationFloor);
        }
    }
}
