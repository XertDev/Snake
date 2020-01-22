package agh.cs.game.entities;

import agh.cs.game.utils.Vector3D;

public class Apple implements ICollidable, IEatable{
    private final Node node;
    private final int energy;

    public Apple(Vector3D position, int energy) {
        this.node = new Node(position);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public Node getNode() {
        return node;
    }

}
