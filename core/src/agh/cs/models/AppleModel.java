package agh.cs.models;

import agh.cs.game.entities.Apple;
import agh.cs.game.utils.Vector3D;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class AppleModel implements IModel {
    private Model model;
    private ModelInstance modelInstance;

    public AppleModel(Apple apple, float cellSize) {
        ModelBuilder modelBuilder = new ModelBuilder();;
        final Vector3D pos = apple.getNode().getPosition();
        model = modelBuilder.createBox(cellSize, cellSize, cellSize,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setTranslation(pos.toVector3().scl(cellSize));
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
