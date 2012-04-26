package com.mutex.m2d;

import org.lwjgl.Sys;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import com.mutex.m2d.Renderer;

public class Main {

	public static final String GAME_TITLE = "m2d";
	public static final int FRAMERATE = 60;
	public static boolean finished;
	public static float angle;
	public static World world;
	
	private Main() {}
	private static Renderer r;
	public static FontRenderer fr;
	
	public static void main(String[] args) {
		//System.setProperty("org.lwjgl.util.Debug", "true");
		try {
			init();
			run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			Sys.alert(GAME_TITLE, "Error occured");
		} finally {
			cleanup();
		}
		System.exit(0);
	}
	
	public static void init() throws Exception {
		Display.setTitle(GAME_TITLE);
		//Display.setFullscreen(true);
		Display.setDisplayMode(new DisplayMode(800, 600));
		//Display.setResizable(true);
		Display.setVSyncEnabled(true);
		Display.create();
		AL.create();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		world = new World();
		r = new Renderer(world);
		fr = new FontRenderer(r);
		r.init();
		r.loadTextures();
		fr.renderString("0123456789", 100, 100, 2);
	}
	
	private static void run() {
		while (!finished) {
			Display.update();
			if (Display.isCloseRequested()) {
				finished = true;
			} else if (Display.isActive()) {
				logic();
				render();
				Display.sync(FRAMERATE);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				logic();
				if (Display.isVisible() || Display.isDirty()) {
					render();
				}
			}
		}
	}
	
	private static void cleanup() {
		AL.destroy();
		Display.destroy();
	}
	
	private static void logic() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			finished = true;
		}
	}
	
	private static void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClear(GL_COLOR_BUFFER_BIT);
		glPushMatrix();

		glTranslatef(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2, 0.0f);
		r.renderMap();
		r.drawGrid();
		r.renderOverlay();
		r.renderPlayer();
		glPopMatrix();
	}
}