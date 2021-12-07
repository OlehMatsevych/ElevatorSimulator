package Logic;

import Interfaces.ElevatorStrategy;
import Logger.CustomLogger;
import Models.*;

import java.util.concurrent.BlockingQueue;

public class DummStrategy extends BaseStrategy implements ElevatorStrategy {
    private static final Object isEmptyLocker = new Object();

    public DummStrategy(Elevator elevator, BlockingQueue<Passenger> floorQueue) {
        super(elevator, floorQueue);
    }

    @Override
    public void Move() {
        //may be changed later
        CustomLogger.info("Elevator " + elevator.getID() + " started");
        MainWindow wi = MainWindow.getInstance();
        boolean isCalled = true;
        double step = 0.0000005;
        while(true) {
                try {
                    Passenger firstPassenger;
                    if(elevator.getPassengers().isEmpty()) {
                        while (true) {
                            CustomLogger.info("Elevator " + elevator.getID() + " waiting");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                CustomLogger.warn(e.getMessage());
                                e.printStackTrace();
                            }
                            synchronized (isEmptyLocker) {
                                if (!floorQueue.isEmpty()) {
                                    firstPassenger = floorQueue.poll();
                                    CustomLogger.info(firstPassenger.getName() + " polled");
                                    isCalled = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        firstPassenger = elevator.getPassengers().get(0);
                        isCalled = false;
                    }

                    if(isCalled) {
                        Floor firstCalledFloor = wi.getBuilding().getFloors().get(firstPassenger.getSourceFloor());
                        CustomLogger.info("Elevator " + elevator.getID() + " moving to " + firstPassenger.getSourceFloor() + " floor");
                        while (Math.abs(elevator.getY() - firstCalledFloor.getY()) > step) {
                            if (elevator.getY() < firstCalledFloor.getY()) {
                                elevator.setY(elevator.getY() + step);
                            } else {
                                elevator.setY(elevator.getY() - step);
                            }
                        }

                        CustomLogger.info("Elevator " + elevator.getID() + " stopped on " + firstPassenger.getSourceFloor() + " floor");
                        elevator.setCurrentFloor(firstCalledFloor);
                        elevator.Stop(firstCalledFloor);

                        if(!elevator.getPassengers().contains(firstPassenger))
                            CustomLogger.warn("Elevator is empty");
                            continue;
                    }

                    //deliver
                    Floor destinationFloor = wi.getBuilding().getFloors().get(firstPassenger.getDestinationFloor());
                    CustomLogger.info("Elevator " + elevator.getID() + " moving to " + firstPassenger.getDestinationFloor() + " floor");
                    while (Math.abs(elevator.getY() - destinationFloor.getY()) > step) {
                        if (elevator.getY() < destinationFloor.getY()) {
                            elevator.setY(elevator.getY() + step);
                        } else {
                            elevator.setY(elevator.getY() - step);
                        }
                    }

                    CustomLogger.info("Elevator " + elevator.getID() + " stopped on " + firstPassenger.getDestinationFloor() + " floor");
                    elevator.setCurrentFloor(destinationFloor);
                    elevator.Stop(destinationFloor);
                    elevator.getPassengers().remove(firstPassenger);
                } catch (NullPointerException ignored) {
                    CustomLogger.error(ignored.getMessage());
                }
            }
    }
}
