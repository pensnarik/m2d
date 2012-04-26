package com.mutex.m2d;

public class Block {
	public final int blockID;
	
	public int x;
	public int y;	
	
	Block(int blockID_, int x_, int y_)
	{
		x = x_; y = y_;
		blockID = blockID_;
		System.out.println("New block: " + x + ", " + y);
	}
}