package agh.cs.models;

import agh.cs.game.INodePositionChangeObserver;
import agh.cs.game.entities.Node;
import agh.cs.game.utils.Vector3D;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class SnakeNodeModel implements IModel, INodePositionChangeObserver {
    Model model;
    ModelInstance modelInstance;
    final float cellSize;

    public SnakeNodeModel(Node node, float cellSize) {
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(cellSize, cellSize, cellSize,
                new Material(ColorAttribute.createDiffuse(Color.VIOLET)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setTranslation(node.getPosition().toVector3().scl(cellSize));

        node.addPositionChangeObserver(this);
        this.cellSize = cellSize;
    }

    @Override
    public void positionChanged(Vector3D oldPos, Vector3D newPos) {
        final Vector3D delta = newPos.subtract(oldPos);
        modelInstance.transform.trn(delta.toVector3().scl(cellSize));
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public Model[] getModels() {
        return new Model[] {model};
    }

    @Override
    public ModelInstance[] getModelInstances() {
        return new ModelInstance[] {modelInstance};
    }
}
