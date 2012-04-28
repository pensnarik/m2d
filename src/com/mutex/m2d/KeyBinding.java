package com.mutex.m2d;

public class KeyBinding
{
	public int key;
	public boolean pressed;
	public int pressTime;
	
	public static HashMap hashMap = new HashMap();
	
	KeyBinding(int key_)
	{
		key = key_;
		hashMap.add(key_, this);
	}
	
	public static void onTick(int key_)
	{
		KeyBinding keyBinding = (KeyBinding) hashMap.getValueByKey(key_);
		if (keyBinding != null)
		{
			keyBinding.pressTime++;
		}
	}
	
	@SuppressWarnings("unused")
	private void unpressKey()
	{
		pressTime = 0;
		pressed = false;
	}
	
	public boolean isPressed()
	{
		if (pressTime == 0)
		{
			return false;
		} 
		else
		{
			pressTime--;
			return true;
		}
	}
	
	public static void setState(int key_, boolean pressed_)
	{
		KeyBinding keyBinding = (KeyBinding) hashMap.getValueByKey(key_);
		if (keyBinding != null)
		{
			keyBinding.pressed = pressed_;
		}
	}
}