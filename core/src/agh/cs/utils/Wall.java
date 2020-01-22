package agh.cs.utils;

import com.badlogic.gdx.math.Vector3;

public class Wall {
    private Vector3 Position;
    private Vector3 size;

    public Wall(Vector3 position, Vector3 size) {
        Position = position;
        this.size = size;
    }

    public Vector3 getPosition() {
        return Position;
    }

    public Vector3 getSize() {
        return size;
    }
}
