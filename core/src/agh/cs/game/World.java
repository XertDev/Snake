package agh.cs.game;

import agh.cs.game.entities.Apple;
import agh.cs.game.entities.Snake;
import agh.cs.game.errors.GameLogicException;
import agh.cs.game.utils.Vector3D;

import java.util.*;

public class World {

    private WorldMap worldMap;
    private Snake snake;

    static private Random generator = new Random();

    private Set<IAppleEatObserver> fruitEatObservers = new HashSet<>();
    private Set<IAppleCreateObserver> fruitCreateObservers = new HashSet<>();

    public World(int mapWidth, int mapHeight, int mapDepth, Snake snake) {
        worldMap = new WorldMap(mapWidth, mapHeight, mapDepth, snake);
        this.snake = snake;
    }


    private boolean isSnakeSelfCollide() {
        Map<Vector3D, Integer> occupation = new HashMap<>();
        snake.getNodes().forEach(node -> {
            Vector3D pos = node.getPosition();
            if(occupation.containsKey(pos)){
                occupation.replace(pos, occupation.get(pos) + 1);
            } else {
                occupation.put(pos, 0);
            }
        });

        return occupation.values().stream().anyMatch(nodeCount -> nodeCount > 1);

    }

    private boolean snakeMapEntityCollide() {
        Vector3D snakeHead = snake.getNodes().get(0).getPosition();
        if(worldMap.getApple().isEmpty()) return false;
        Vector3D rigidEntity = worldMap.getApple().get().getNode().getPosition();
        return rigidEntity.equals(snakeHead);
    }

    public boolean update() {
        snake.move();
        if(isSnakeSelfCollide()) {
            return false;
        }
        Vector3D snakeHead = snake.getNodes().get(0).getPosition();

        if(snakeMapEntityCollide()){
            if(worldMap.getApple().isEmpty()) {
                throw new GameLogicException("Apple not exist");
            }
            final int appleEnergy = worldMap.getApple().get().getEnergy();
            snake.eat(appleEnergy);
            appleEaten();
            worldMap.removeApple();
        }
        if(!worldMap.isInsideMap(snakeHead)) {
            return false;
        }
        if(worldMap.getApple().isEmpty() && generator.nextBoolean()) {
            worldMap.placeAppleAtRandomCell();
            appleCreateNotify();
        }
        return true;
    }

    private void appleEaten() {
        fruitEatObservers.forEach(IAppleEatObserver::appleEat);
    }

    public void addAppleEatObserver(IAppleEatObserver observer) {
        fruitEatObservers.add(observer);
    }

    public void removeAppleEatObserver(IAppleEatObserver observer) {
        fruitEatObservers.remove(observer);
    }

    private void appleCreateNotify() {
        final Apple apple = worldMap.getApple().orElseThrow();
        fruitCreateObservers.forEach(observer -> observer.appleCreate(apple));
    }

    public void addAppleCreateObserver(IAppleCreateObserver observer) {
        fruitCreateObservers.add(observer);
    }

    public void removeAppleCreateObserver(IAppleCreateObserver observer) {
        fruitCreateObservers.remove(observer);
    }
}
