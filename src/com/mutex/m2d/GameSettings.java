package com.mutex.m2d;

import org.lwjgl.input.Keyboard;

public class GameSettings
{
	KeyBinding keyBindLeft;
	KeyBinding keyBindRight;
	KeyBinding keyBindUp;
	KeyBinding keyBindDown;
	KeyBinding keyBindJump;
	
	GameSettings()
	{
		keyBindLeft = new KeyBinding(Keyboard.KEY_LEFT);
		keyBindRight = new KeyBinding(Keyboard.KEY_RIGHT);
		keyBindUp = new KeyBinding(Keyboard.KEY_UP);
		keyBindDown = new KeyBinding(Keyboard.KEY_DOWN);
		keyBindJump = new KeyBinding(Keyboard.KEY_SPACE);			
	}
}