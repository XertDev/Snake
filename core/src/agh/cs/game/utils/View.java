package agh.cs.game.utils;

public class View {
    public final Vector3D localUp;
    public final Vector3D localForward;

    public View(Vector3D localUp, Vector3D localForward) {
        this.localUp = localUp;
        this.localForward = localForward;
    }

    public View turnUp() {
        return new View(localForward.opposite(), localUp);
    }
    public View turnDown() {
        return new View(localForward, localUp.opposite());
    }

    public View turnRight() {
        return new View(localUp, localForward.product(localUp));
    }
    public View turnLeft() {
        return new View(localUp, localUp.product(localForward));
    }


}
