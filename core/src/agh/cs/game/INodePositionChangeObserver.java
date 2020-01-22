package agh.cs.game;

import agh.cs.game.utils.Vector3D;

public interface INodePositionChangeObserver {
    public void positionChanged(Vector3D oldPos, Vector3D newPos);
}
