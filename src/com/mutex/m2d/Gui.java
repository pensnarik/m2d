package com.mutex.m2d;

import org.lwjgl.input.Mouse;

public class Gui
{
	public static World world;
	public static Renderer renderer;
	
	public Gui(World world_, Renderer renderer_)
	{
		world = world_;
		renderer = renderer_;
	}
	
	public static void onMouseClick(int button, int x, int y)
	{
		int sceneX = MathHelper.floor_double(renderer.getSceneCoordX(x));
		int sceneY = MathHelper.floor_double(renderer.getSceneCoordY(y));
		int blockID = world.getBlockID(sceneX, sceneY);
		if (blockID > 0)
		{
			world.setBlockID(sceneX, sceneY, 0);
		}
	}
	
	public void onTick()
	{
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		int sceneX = MathHelper.floor_double(renderer.getSceneCoordX(mouseX));
		int sceneY = MathHelper.floor_double(renderer.getSceneCoordY(mouseY));
		int blockID = world.getBlockID(sceneX, sceneY);
		if (blockID > 0)
		{
			renderer.setSelectedBlock(sceneX, sceneY);
		} else {
			renderer.unselectBlock();
		}
	}
}