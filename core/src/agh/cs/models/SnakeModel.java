package agh.cs.models;

import agh.cs.game.ISnakeNodeGrowObserver;
import agh.cs.game.entities.Node;
import agh.cs.game.entities.Snake;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.List;
import java.util.stream.Collectors;

public class SnakeModel implements IModel, ISnakeNodeGrowObserver {

        List<SnakeNodeModel> snakeNodeModels;
        final float cellSize;

    public SnakeModel(Snake snake, float cellSize) {
        this.cellSize = cellSize;
        snakeNodeModels = snake.getNodes().stream()
                .map(node -> new SnakeNodeModel(node, cellSize))
                .collect(Collectors.toList());
        snake.addSnakeNodeGrowObserver(this);
    }

    @Override
    public void dispose() {
        snakeNodeModels.forEach(SnakeNodeModel::dispose);
    }

    @Override
    public Model[] getModels() {
        return snakeNodeModels.stream().map(snakeNodeModel -> snakeNodeModel.getModels()[0]).toArray(Model[]::new);
    }

    @Override
    public ModelInstance[] getModelInstances() {
        return snakeNodeModels.stream().map(snakeNodeModel -> snakeNodeModel.getModelInstances()[0]).toArray(ModelInstance[]::new);
    }

    @Override
    public void nodeGrow(Node newNode) {
        snakeNodeModels.add(new SnakeNodeModel(newNode, cellSize));
    }
}
