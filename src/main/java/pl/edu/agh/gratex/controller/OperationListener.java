package pl.edu.agh.gratex.controller;

public interface OperationListener {
    public void operationStarted(String info);
    public void operationInProgress(String info);
    public void operationFinished(String info);
}
