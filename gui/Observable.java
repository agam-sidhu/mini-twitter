package gui;
// Observable interface

interface Observable {

    void addObserver(Observer observer);
    void notifyObservers(String message);

}