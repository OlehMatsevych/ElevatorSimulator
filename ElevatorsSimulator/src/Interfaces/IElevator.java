package Interfaces;

import Models.Floor;

public interface IElevator {
    public void Stop();
    public void Called(Floor floor);
    public void CloseDoors();
    public void OpenDoors();
}
