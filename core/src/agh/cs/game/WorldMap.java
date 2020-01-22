package agh.cs.game;

import agh.cs.game.entities.Apple;
import agh.cs.game.entities.Snake;
import agh.cs.game.errors.GameLogicError;
import agh.cs.game.utils.Vector3D;

import java.util.*;

public class WorldMap {
    private final Vector3D leftBottomWorldCorner;
    private final Vector3D rightTopWorldCorner;


    private Snake snake;
    private int appleEnergy;

    static private Random generator = new Random();

    private Apple apple;

    public WorldMap(int mapWidth, int mapHeight, int mapDepth, Snake snake) {
        this(mapWidth, mapHeight, mapDepth, snake, 30);
    }

    public WorldMap(int mapWidth, int mapHeight, int mapDepth, Snake snake, int appleEnergy) {
        leftBottomWorldCorner = new Vector3D(0,0, 0);
        rightTopWorldCorner = new Vector3D(mapWidth - 1, mapHeight - 1, mapDepth - 1);
        this.snake = snake;
        this.appleEnergy = appleEnergy;
    }

    public boolean isOccupied(Vector3D field) {
        return (apple != null && apple.getNode().getPosition().equals(field)) || snake.getNodes().stream().anyMatch(node -> {
            Vector3D pos = node.getPosition();
            return field.equals(pos);
        });
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Optional<Apple> getApple() {
        return Optional.ofNullable(apple);
    }

    public Vector3D getMapSize() {
        return rightTopWorldCorner;
    }

    public boolean isInsideMap(Vector3D pos) {
        return pos.follows(leftBottomWorldCorner) && pos.precedes(rightTopWorldCorner);
    }

    private Optional<Vector3D> getRandomEmptyField() {
        for(int i = 0; i < rightTopWorldCorner.x*rightTopWorldCorner.y*rightTopWorldCorner.z; ++i) {
            int x = generator.nextInt(rightTopWorldCorner.x);
            int y = generator.nextInt(rightTopWorldCorner.y);
            int z = generator.nextInt(rightTopWorldCorner.z);
            final Vector3D pos = new Vector3D(x, y, z);
            if(!isOccupied(pos)) {
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }

    public boolean placeAppleAtRandomCell() {
        
        if(apple != null) {
            throw new GameLogicError("Apple already placed");
        }
        
        final Optional<Vector3D> newApplePos = getRandomEmptyField();
        if(newApplePos.isEmpty()){
            return false;
        }
        apple = new Apple(newApplePos.get(), appleEnergy);
        return true;
    }

    public void removeApple() {
        final Apple toEat = apple;
        apple = null;
    }
}
