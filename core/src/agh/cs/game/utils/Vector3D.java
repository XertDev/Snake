package agh.cs.game.utils;

import com.badlogic.gdx.math.Vector3;

import java.util.Objects;

public class Vector3D {
    final public int x;
    final public int y;
    final public int z;


    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    public boolean precedes(Vector3D other) {
        return x <= other.x && y <= other.y && z <= other.z;
    }

    public boolean follows(Vector3D other) {
        return x >= other.x && y >= other.y && z >= other.z;
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(
                x + other.x,
                y + other.y,
                z + other.z
        );
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(
                x - other.x,
                y - other.y,
                z - other.z
        );
    }

    public Vector3D opposite() {
        return new Vector3D(-x, -y, -z);
    }

    public Vector3D product(Vector3D other) {
        final int productX = (y*other.z - z*other.y);
        final int productY = (z*other.x - x*other.z);
        final int productZ = (x*other.y - y*other.x);

        return new Vector3D(productX, productY, productZ);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Vector3D vector3D = (Vector3D) obj;
        return x == vector3D.x && y == vector3D.y && z == vector3D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public Vector3 toVector3() {
        return new Vector3(x, y, z);
    }
}
