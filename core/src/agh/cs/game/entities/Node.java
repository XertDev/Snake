package agh.cs.game.entities;

import agh.cs.game.INodePositionChangeObserver;
import agh.cs.game.utils.Vector3D;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private Vector3D position;
    private Set<INodePositionChangeObserver> observers = new HashSet<>();

    public Node(Vector3D pos) {
        position = pos;
    }

    public void setPosition(Vector3D pos) {
        Vector3D oldPos = position;
        position = pos;
        notifyObservers(oldPos, position);
    }

    private void notifyObservers(Vector3D oldPos, Vector3D newPos) {
        observers.forEach(observer -> observer.positionChanged(oldPos, newPos));
    }

    public void addPositionChangeObserver(INodePositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removePositionChangeObserver(INodePositionChangeObserver observer) {
        observers.remove(observer);
    }

    public Vector3D getPosition() {
        return new Vector3D(position.x, position.y, position.z);
    }
}
