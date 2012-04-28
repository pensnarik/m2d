package com.mutex.m2d;

public class MovementInput
{
	public float moveX;
	public float moveY;
	public boolean jump;
	
	private GameSettings gameSettings;
	
	public MovementInput(GameSettings gameSettings_)
	{
		gameSettings = gameSettings_;
		moveX = 0;
		moveY = 0;
		jump = false;
	}
	
	public void update()
	{
		moveX = 0;
		moveY = 0;
		
		if (gameSettings.keyBindLeft.pressed)
		{
			moveX--;
		}
		if (gameSettings.keyBindRight.pressed)
		{
			moveX++;			
		}
		if (gameSettings.keyBindUp.pressed)
		{
			moveY++;
		}
		if (gameSettings.keyBindDown.pressed)
		{
			moveY--;
		}
		jump = gameSettings.keyBindJump.pressed;
	}
}