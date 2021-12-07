package Models;

import Logic.PassengerBrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassengerFactory {
    private int maxWeight = 120;
    private int minWeight = 0;
    private int maxCountOfFloors;
    public List<Passenger> passengerList;

    public PassengerFactory(int _maxCountOfFloors){
        maxCountOfFloors = _maxCountOfFloors;
    }

    public Passenger getPassenger(){
        if(passengerList == null){
            passengerList = new ArrayList<>();
            return createNewPerson();
        }else {
            return getExistingOrNewPerson();
        }
    }

    private Passenger getExistingOrNewPerson() {
        Passenger passengerToReturn = passengerList
                .stream()
                .filter(e -> e.getState() == PassengerState.Left)
                .findFirst()
                .orElse(null);

        if(passengerToReturn == null){
            return createNewPerson();
        }else {
            return updatePerson(passengerToReturn);
        }
    }

    private Passenger createNewPerson() {
        var createdPassenger = createRandomPassenger();
        createdPassenger.setStrategy(new PassengerBrain(createdPassenger));
        passengerList.add(createdPassenger);
        return  createdPassenger;
    }

    private Passenger createRandomPassenger(){
        int weight = getRandomInteger(minWeight, maxWeight);

        var createdPassenger = new Passenger(weight, 0, 0, PassengerState.Spawned);
        setFloors(createdPassenger);

        return createdPassenger;
    }

    private Passenger updatePerson(Passenger passengerToReturn) {
        passengerToReturn.setState(PassengerState.Spawned);

        setFloors(passengerToReturn);

        return  passengerToReturn;
    }

    private int getRandomInteger(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    private void setFloors(Passenger passenger) {
        int sourceFloor = getRandomInteger(0, maxCountOfFloors);
        int destinationFloor = getRandomInteger(0, maxCountOfFloors);

        while(sourceFloor == destinationFloor){
            sourceFloor = getRandomInteger(0, maxCountOfFloors);
            destinationFloor = getRandomInteger(0, maxCountOfFloors);
        }

        passenger.setSourceFloor(sourceFloor);
        passenger.setDestinationFloor(destinationFloor);
    }

}
