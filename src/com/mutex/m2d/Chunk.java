package com.mutex.m2d;

import com.mutex.m2d.Block;
import java.util.*;

public class Chunk
{
	public byte blocks[];
	public ArrayList entities;
	public final int x;
	public final int y;
	public World world;
	public static final int width = 64;
	public static final int height = 64;
	
	
	public Chunk (World world_, int x_, int y_)
	{
		world = world_;
		x = x_; y = y_;
		entities = new ArrayList();
	}
	
	public Chunk (World world_, byte blocks_[], int x_, int y_)
	{
		this(world_, x_, y_);
		blocks = blocks_;
	}
	
	public int getBlockID(int x_, int y_)
	{
		return blocks[x_ + y_ * width];
	}
	
	public void setBlockID(int x_, int y_, int blockID_)
	{
		blocks[x_ + y_ * width] = (byte) blockID_;
	}
	
	public void addEntity(Entity entity)
	{
		entities.add(entity);
		entity.chunkX = x;
		entity.chunkY = y;
	}
	
	public void removeEntity(Entity entity)
	{
		entities.remove(entity);
	}
	
	public void getEntitiesWithinBoundingBoxExcluding(Entity entity, BoundingBox boundingBox, List list)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity searchingEntity = (Entity) entities.get(i);
			if (searchingEntity == entity || !searchingEntity.boundingBox.intersectsWith(boundingBox))
			{
				continue;
			}
			list.add(entity);			
		}
	}
}