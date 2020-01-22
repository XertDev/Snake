package agh.cs.game.entities;


import agh.cs.game.ISnakeNodeGrowObserver;
import agh.cs.game.utils.Vector3D;
import agh.cs.game.utils.View;

import java.util.*;

public class Snake {
    private View view;
    private List<Node> nodes = new LinkedList<>();
    private boolean shouldGrow;

    public int getEnergy() {
        return energy;
    }

    private int energy = 0;

    private Set<ISnakeNodeGrowObserver> growObservers = new HashSet<>();

    public View getView() {
        return new View(view.localUp, view.localForward);
    }

    public Snake(View view, Vector3D initialPos) {
        this.view = view;
        this.nodes.add(new Node(initialPos));
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public String toString() {
        if(nodes.isEmpty()) {
            return "Snake";
        } else {
            return String.format("Snake %s", nodes.get(0).toString());
        }
    }

    public void turn(ViewDirection dir) {
        switch (dir) {
            case LEFT:
                view = view.turnLeft();
                break;
            case RIGHT:
                view = view.turnRight();
                break;
            case DOWN:
                view = view.turnDown();
                break;
            case UP:
                view = view.turnUp();
                break;
        }
    }

    public void move() {
        final Vector3D tailPos = nodes.get(nodes.size()-1).getPosition();
        for(int i = nodes.size() - 1; i > 0; --i) {
            final Vector3D nextPos = nodes.get(i-1).getPosition();
            nodes.get(i).setPosition(nextPos);
        }
        final Vector3D latestHeadPos = nodes.get(0).getPosition();
        nodes.get(0).setPosition(latestHeadPos.add(view.localForward));

        if(shouldGrow) {
            Node tail = new Node(tailPos);
            nodes.add(tail);
            notifyObservers(tail);
            shouldGrow = false;

        }
    }

    private void grow() {
        shouldGrow = true;
    }

    public void eat(int energy) {
        this.energy += energy;
        shouldGrow = true;
    }

    private void notifyObservers(Node tail) {
        growObservers.forEach(observer -> observer.nodeGrow(tail));
    }

    public void addSnakeNodeGrowObserver (ISnakeNodeGrowObserver observer) {
        growObservers.add(observer);
    }

    public void removeSnakeNodeGrowObserver (ISnakeNodeGrowObserver observer) {
        growObservers.remove(observer);
    }
}
