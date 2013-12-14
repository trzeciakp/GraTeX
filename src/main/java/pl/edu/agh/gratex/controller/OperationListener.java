package pl.edu.agh.gratex.controller;

public interface OperationListener {
    public void startOperationEvent(String info);

    public void finishOperationEvent(Operation operation);

    public void genericOperationEvent(String info);
}
