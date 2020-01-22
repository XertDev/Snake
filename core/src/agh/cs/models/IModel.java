package agh.cs.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface IModel {
    public void dispose();
    public Model[] getModels();
    public ModelInstance[] getModelInstances();
}
