package com.mutex.m2d;

public class HashMapEntry
{
	public int key;
	public Object value;
	
	public HashMapEntry(int key_, Object object_)
	{
		key = key_;
		value = object_;
	}
	
	public final int getKey()
	{
		return key;
	}
	
	public final Object getValue()
	{
		return value;
	}
}