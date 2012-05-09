package com.mutex.m2d;

import java.util.*;


public class World {
	
	public ChunkProvider chunkProvider;
	public EntityPlayer player;
	public Random RandomGen;
	private int totalTicks;
	public ArrayList<Block> blocks;
	private ArrayList<BoundingBox> collidingBoundingBoxes;
	public List listEntities;
	public List excludingEntityBoundingBoxes;
	
	World()
	{
		totalTicks = 0;
		RandomGen = new Random(1);
		chunkProvider = new ChunkProvider(this);
		blocks = new ArrayList<Block>();		
		collidingBoundingBoxes = new ArrayList();
		excludingEntityBoundingBoxes = new ArrayList();
		listEntities = new ArrayList();
	}
	
	public void prepareVisibleChunks()
	{
		int Cx = (int)(player.posX / Chunk.width);
		int Cy = (int)(player.posY / Chunk.height);
		for (int y = Cy - 1; y <= Cy + 1; y++)
		{
			for (int x = Cx - 1; x <= Cx + 1; x++)
			{
				@SuppressWarnings("unused")
				Chunk chunk = chunkProvider.provideChunk(x, y);
			}
		}
		System.out.println("Chunks loaded: " + chunkProvider.loadedChunks.getSize());
	}
	
	public void tick()
	{
		updateEntities();
		updateBlocks();
		if (totalTicks++ % 20 == 0)
		{
			System.out.println("Chunks loaded: " + chunkProvider.loadedChunks.getSize());
			//System.out.println("blocks.size() = " + blocks.size());
			//System.out.println("player.isCollided = " + player.isCollided);
		}
	}
	
	public void updateBlocks()
	{
		for (int i = 0; i < chunkProvider.loadedChunks.getSize(); i++)		
		{
			Chunk chunk = (Chunk) chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.height * Chunk.width; j++) {
				Block b = null; //chunk.blocks[j];
				if (b == null) continue;
				b.tick();
			}
		}		
	}
	
	public void updateEntities()
	{
		Entity e = (Entity) player;
		e.onUpdate();
		for (int i = 0; i < listEntities.size(); i++)
		{
			Entity entity = (Entity) listEntities.get(i);
			if (entity.isDead)
			{
				int chunkX = entity.chunkX;
				int chunkY = entity.chunkY;
				getChunkFromChunkCoords(chunkX, chunkY).removeEntity(entity);
			}
			entity.onUpdate();
		}
	}
	
	public void newBlock(Block block)
	{
		blocks.add(block);
	}
	
	public int getBlockUnderEntity(Entity e)
	{
		/* Очевидно, что блок нужно искать только в текущем чанке или в чанках, которые находятся
		 * под ним
		 */
		if (true) return 0;
		int posX = MathHelper.round_float((float)e.posX);
		int posY = MathHelper.round_float((float)e.posY);
		for (int y = posY; y > -1000; y--)
		{
			int blockID = getBlockID(posX, y); 
			if (blockID != 0)
			{
				return blockID;
			}
		}
		return 0;
	}
	
	public Chunk getChunkFromChunkCoords(int x_, int y_)
	{
		return chunkProvider.provideChunk(x_, y_);
	}
	
	public Chunk getChunkFromBlockCoords(int x_, int y_)
	{
		return getChunkFromChunkCoords(x_ >> 6, y_ >> 6);
	}
	
	public int getBlockID(int x_, int y_)
	{
		return getChunkFromChunkCoords(x_ >> 6, y_ >> 6).getBlockID(x_ & 0x3f, y_ & 0x3f);
	}
	
	public void setBlockID(int x_, int y_, int blockID_)
	{
		getChunkFromChunkCoords(x_ >> 6, y_ >> 6).setBlockID(x_ & 0x3f, y_ & 0x3f, blockID_);
	}
	
	public void removeBlock(int x_, int y_)
	{
		if (isAirBlock(x_, y_)) return;
		int blockID = getBlockID(x_, y_);
		Block.blocksList[blockID].dropBlockAsItem(this, x_, y_);
		setBlockID(x_, y_, 0);
	}
	
	public boolean isAirBlock(int x_, int y_)
	{
		return getBlockID(x_, y_) == 0;
	}
	
	/*
	 * Возвращает все bounding box'ы, пересекающиеся с boundingBox, за исключением сущности entity
	 */
	
	public List<BoundingBox> getCollidingBoundingBoxes(Entity entity, BoundingBox boundingBox)
	{
		collidingBoundingBoxes.clear();
		
		int cubeMinX = MathHelper.floor_double(boundingBox.minX);
		int cubeMinY = MathHelper.floor_double(boundingBox.minY);
		int cubeMaxX = MathHelper.floor_double(boundingBox.maxX + 1);
		int cubeMaxY = MathHelper.floor_double(boundingBox.maxY + 1);
		
		for (int x = cubeMinX; x < cubeMaxX; x++)
		{
			for (int y = cubeMinY; y < cubeMaxY; y++)
			{
				Block block = Block.blocksList[getBlockID(x, y)];
				if (block != null)
				{
					block.getCollidingBoundingBoxes(this, x, y, boundingBox, collidingBoundingBoxes);
				}
			}
		}
		
		return collidingBoundingBoxes;
	}
	
	public void addEntity(Entity entity)
	{
		listEntities.add(entity);
		int chunkX = MathHelper.floor_double(entity.posX) >> 6;
		int chunkY = MathHelper.floor_double(entity.posY) >> 6;
		Chunk chunk = getChunkFromChunkCoords(chunkX, chunkY);
		if (chunk != null)
		{
			chunk.addEntity(entity);
		}
	}
	
	/*
	 * Возвращает список сущностей, пересекающихся с bounding-box'ом boundingBox,
	 * исключая саму сущность
	 */
	
	public List getEntitiesWithinBoundingBoxEcxluding(Entity entity, BoundingBox boundingBox)
	{
		excludingEntityBoundingBoxes.clear();
		int x1 = MathHelper.floor_double(boundingBox.minX / 64);
		int y1 = MathHelper.floor_double(boundingBox.minY / 64);
		int x2 = MathHelper.floor_double(boundingBox.maxX / 64);
		int y2 = MathHelper.floor_double(boundingBox.maxY / 64);
		for (int i = x1; i <= x2; i++)
		{
			for (int j = y1; j <= y2; j++)
			{
				Chunk chunk = getChunkFromBlockCoords(i, j);
				if (chunk != null)
				{
					chunk.getEntitiesWithinBoundingBoxExcluding(entity, boundingBox, excludingEntityBoundingBoxes);
				}				
			}
		}
		return excludingEntityBoundingBoxes;
	}

}