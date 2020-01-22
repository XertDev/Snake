package agh.cs.models;

import com.badlogic.gdx.math.Vector3;

public class WallModel {
    private Vector3 Position;
    private Vector3 size;

    public WallModel(Vector3 position, Vector3 size) {
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
