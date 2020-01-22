package agh.cs.desktop;

import agh.cs.game.errors.GameLogicException;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import agh.cs.SnakeGame;
import org.lwjgl.opengl.OpenGLException;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Snake Programowanie Obiektowe";
        try {
            new LwjglApplication(new SnakeGame(), config);
        }
        catch (OpenGLException | GameLogicException e) {
            e.printStackTrace();
        }
	}
}
