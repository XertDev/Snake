package agh.cs;

import agh.cs.game.IAppleCreateObserver;
import agh.cs.game.IAppleEatObserver;
import agh.cs.game.World;
import agh.cs.game.entities.Apple;
import agh.cs.game.entities.Snake;
import agh.cs.game.utils.ViewDirection;
import agh.cs.game.utils.Vector3D;
import agh.cs.game.utils.View;
import agh.cs.models.AppleModel;
import agh.cs.models.SnakeModel;
import agh.cs.utils.Wall;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends ApplicationAdapter implements IAppleEatObserver, IAppleCreateObserver {
    private ModelBatch modelBatch;
    private Environment environment;

    private PerspectiveCamera camera;

    private BitmapFont bitmapFont;
    private SpriteBatch textBatch;


    private List<Model> walls = new ArrayList<>();
    private List<ModelInstance> wallInstances = new ArrayList<>();
    private SnakeModel snakeModel;
    private Snake snake;

    private World world;

    private AppleModel appleModel;

    private Long elapsedTime;
    private int frame = 0;

    static private final int  mapSize = 9;
    static private final float mapCellSize = 5f;

    private int actualSnakeEnergy = 0;
    static private int wallDepth= 5;

    private Wall[] generateWalls() {
        int halfMapSize = mapSize/2;
        return new Wall[] {

                        new Wall(
                                new Vector3(mapSize*mapCellSize, halfMapSize*mapCellSize ,halfMapSize*mapCellSize),
                                new Vector3(wallDepth, mapSize*mapCellSize, mapSize*mapCellSize)
                        ),
                        new Wall(
                                new Vector3(-wallDepth, halfMapSize*mapCellSize ,halfMapSize*mapCellSize),
                                new Vector3(wallDepth, mapSize*mapCellSize, mapSize*mapCellSize)
                        ),
                        new Wall(
                                new Vector3(halfMapSize*mapCellSize, mapSize*mapCellSize ,halfMapSize*mapCellSize),
                                new Vector3(mapSize*mapCellSize, wallDepth, mapSize*mapCellSize)
                        ),
                        new Wall(
                                new Vector3(halfMapSize*mapCellSize, -wallDepth  ,halfMapSize*mapCellSize),
                                new Vector3(mapSize*mapCellSize, wallDepth, mapSize*mapCellSize)
                        ),
                        new Wall(
                                new Vector3(halfMapSize*mapCellSize, halfMapSize*mapCellSize  ,mapSize*mapCellSize),
                                new Vector3(mapSize*mapCellSize, mapSize*mapCellSize, wallDepth)
                        ),
                        new Wall(
                                new Vector3(halfMapSize*mapCellSize, halfMapSize*mapCellSize  ,-wallDepth),
                                new Vector3(mapSize*mapCellSize, mapSize*mapCellSize, wallDepth)
                        ),
        };
    }

    @Override
	public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1));
        environment.add(new PointLight().set(1f, 1f, 1f, 0, 0,0, 1000f));

        modelBatch = new ModelBatch();
        textBatch = new SpriteBatch();

        Wall[] wallSections = generateWalls();

        ModelBuilder modelBuilder = new ModelBuilder();

        for(Wall wallSection: wallSections) {
            final float width = wallSection.getSize().x;
            final float height = wallSection.getSize().y;
            final float depth = wallSection.getSize().z;
            final Texture wallTexture = new Texture("room.png");
            Model model = modelBuilder.createBox(width, height, depth,
                    new Material((TextureAttribute.createDiffuse(wallTexture))),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
            walls.add(model);
            wallInstances.add(new ModelInstance(model, wallSection.getPosition()));
        }
        camera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.far = 150f;

        bitmapFont = new BitmapFont();

        initGame();

        elapsedTime = System.currentTimeMillis();
	}

	private void update() {
        if(System.currentTimeMillis() - elapsedTime > 30) {
            actualSnakeEnergy = snake.getEnergy();
            frame++;
            if(frame % 10 == 0) {
                final boolean success = world.update();
                frame = 0;
                System.out.println(snake.getNodes().get(0).getPosition().toString());
                if(!success) {
                    System.out.println(String.format("Score %d", actualSnakeEnergy));
                    dispose();
                }
            }
            elapsedTime = System.currentTimeMillis();

        }

    }

    private void initGame() {
        final View startingView = new View(new Vector3D(0,1,0), new Vector3D(1, 0, 0));
        snake = new Snake(startingView, new Vector3D(0, 0, 0));

        snakeModel = new SnakeModel(snake, mapCellSize);
        world = new World(mapSize, mapSize, mapSize, snake);

        world.addAppleCreateObserver(this);
        world.addAppleEatObserver(this);
    }

    private void updateCamera() {
        Vector3 cameraPosition = snake.getNodes().get(0).getPosition().toVector3().scl(mapCellSize);
        Vector3 upCamera = snake.getView().localUp.toVector3().scl(mapCellSize/2);
        camera.position.set(cameraPosition);
        final Vector3 lookAtPoint = new Vector3(camera.position)
                .add(snake.getView().localForward.toVector3().scl(mapCellSize));
        camera.lookAt(lookAtPoint);
        camera.update();

    }

	@Override
	public void render () {
        update();
        onKeyPress();
        updateCamera();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        modelBatch.begin(camera);
        for(ModelInstance wallSection: wallInstances) {
            modelBatch.render(wallSection, environment);
        }
        for(ModelInstance snakeNodeModelInstance: snakeModel.getModelInstances()) {
            modelBatch.render(snakeNodeModelInstance);
        }
        if(appleModel != null) {
            modelBatch.render(appleModel.getModelInstances()[0]);
        }
        modelBatch.end();

        textBatch.begin();
        bitmapFont.draw(textBatch, String.format("Score: %d ", actualSnakeEnergy), 20, Gdx.graphics.getHeight()-30);
        textBatch.end();

    }
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		for(Model wallSection: walls) {
		    wallSection.dispose();
        }

		snakeModel.dispose();
		appleModel.dispose();
	}

	private void onKeyPress() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            snake.turn(ViewDirection.UP);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            snake.turn(ViewDirection.DOWN);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            snake.turn(ViewDirection.LEFT);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            snake.turn(ViewDirection.RIGHT);
        }
    }

    @Override
    public void appleCreate(Apple apple) {
        appleModel = new AppleModel(apple, mapCellSize);
    }

    @Override
    public void appleEat() {
        appleModel.dispose();
        appleModel = null;
    }
}
