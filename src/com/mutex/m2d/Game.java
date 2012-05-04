package com.mutex.m2d;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game 
{
	public static final String GAME_TITLE = "m2d";
	public static final int DisplayWidth = 800;
	public static final int DisplayHeight = 600;
	public static final int FRAMERATE = 60;
	public static boolean running;
	public static boolean ShowGrid;	
	
	private static Timer timer;
	public static World world;
	public static Renderer r;
	public static FontRenderer fr;
	public static GameSettings gameSettings = new GameSettings();
	public static int ticksRun; /* private */
	private static int fpsCounter;
	public static int lastFps;
	private static long currentTime;
	private static int seconds;
	private static int keyPressTime;
	public static long memoryUsed;
	private static int secCounter;
	public static EntityPlayerSP player;
	public static Gui gui;
	
	private static Game thisGame = new Game();
	
	public Game()
	{
		timer = new Timer(40);
		ticksRun = 0;
		fpsCounter = 0;
		lastFps = 0;
		currentTime = System.currentTimeMillis();
		seconds = 0;
		ShowGrid = false;
		keyPressTime = 0;
		secCounter = 0;
	}	
	
	public static void init() throws Exception
	{
		Display.setTitle(GAME_TITLE);
		Display.setDisplayMode(new DisplayMode(DisplayWidth, DisplayHeight));		
		Display.setVSyncEnabled(true);
		Display.create();
		AL.create();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0, DisplayWidth, 0.0, DisplayHeight, -1.0, 1.0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, DisplayWidth, DisplayHeight);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		world = new World();
		player = new EntityPlayerSP(world);
		player.setPosition(0,  1.01);
		player.movementInput = new MovementInput(gameSettings);
		world.player = player;
		world.prepareVisibleChunks();
		r = new Renderer(world);
		fr = new FontRenderer(r);
		r.init();
		r.loadTextures();
		gui = new Gui(world, r);
	}
	
	public static void loop()
	{
		running = true;
		while (running) {
			Display.update();
			if (Display.isCloseRequested()) {
				running = false;
			} else if (Display.isActive()) {
				run();
				Display.sync(FRAMERATE);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				logic();
				//if (Display.isVisible() || Display.isDirty()) {
				//	render();
				//}
			}
		}		
	}
		
	public static void run()
	{
		timer.updateTimer();
		long l = System.nanoTime();
		render();
		logic();
		for (int i = 0; i < timer.elapsedTicks; i++)
		{
			ticksRun++;
			runTick();
			continue;
		}
		fpsCounter++;
		while (System.currentTimeMillis() >= currentTime  + 1000)
		{
			currentTime += 1000;
			lastFps = fpsCounter;
			seconds++;
			System.out.println("fps: " + lastFps);
			fpsCounter = 0;
		}
	}
	
	public static void runTick()
	{
		secCounter++;
		if (secCounter == 20) {
			updateMemoryUsage();
			secCounter = 0;
		}
		do
		{
			if (!Keyboard.next())
			{
				break;		
			}
			KeyBinding.setState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
			if (Keyboard.getEventKeyState())
			{
				KeyBinding.onTick(Keyboard.getEventKey());
			}
		} while (true);
		world.tick();
		gui.onTick();
		do
		{
			if(!Mouse.next())
			{
				break;
			}
			if (Mouse.isButtonDown(0))
			{
				System.out.println("Button clicked");
				Gui.onMouseClick(0, Mouse.getX(), Mouse.getY());
			}
		} while (true);
	}
	
	private static void logic() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			running = false;
		}
	}
	
	private static void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClear(GL_COLOR_BUFFER_BIT);
		r.renderSky();
		r.renderMap();
		//r.drawGrid();
		r.renderOverlay();
		r.renderPlayer();
		//r.testVBO();
	}	
	
	public static void cleanup() {
		AL.destroy();
		Display.destroy();
	}
	
	public static void updateMemoryUsage()
	{
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
		
		memoryUsed = (totalMemory - freeMemory) / 1024 / 1024;
	}
}