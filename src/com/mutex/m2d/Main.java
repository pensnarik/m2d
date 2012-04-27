package com.mutex.m2d;

import org.lwjgl.Sys;

import com.mutex.m2d.Renderer;
import com.mutex.m2d.Game;

public class Main {
	public static World world;
	private static Main mainObject;
	
	private Main() {}
	
	private static Game game;
	
	public static void main(String[] args) {
		mainObject = new Main();
		mainObject.run();		
	}
		
	public static void run() {
		try {
			Game.init();
			Game.loop();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			Sys.alert("Error", "Error occured");
		} finally {
			Game.cleanup();
		}
		System.exit(0);
	}
}