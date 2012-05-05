package com.mutex.m2d;

public class HashMap
{
	private HashMapEntry hashArray[];
	private int size;
	
	public HashMap()
	{
		hashArray = new HashMapEntry[256];
		size = 0;
	}
	
	public void add(int key, Object obj)
	{
		if (size == 256)
		{
			return;
		}
		hashArray[size] = new HashMapEntry(key, obj);
		size++;
	}
	
	public Object getValueByKey(int key)
	{
		for(int i = 0; i < size; i++)
		{
			if (hashArray[i].key == key) {
				return (Object) hashArray[i].value;
			}
		}
		return null;
	}
	
	public Object getValueByIndex(int i)
	{
		return (Object) hashArray[i].value;
	}
	
	public int getSize()
	{
		return size;
	}
}