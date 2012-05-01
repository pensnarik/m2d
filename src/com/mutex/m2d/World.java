package com.mutex.m2d;

import java.util.*;


public class World {
	
	public ChunkProvider chunkProvider;
	public EntityPlayer player;
	public Random RandomGen;
	private int totalTicks;
	ArrayList<Block> blocks;
	
	World()
	{
		totalTicks = 0;
		RandomGen = new Random(1);
		chunkProvider = new ChunkProvider(this);
		blocks = new ArrayList<Block>();		
	}
	
	public void prepareVisibleChunks()
	{
		int Cx = (int)(player.posX / Chunk.width);
		int Cy = (int)(player.posY / Chunk.height);
		for (int x = Cx - 1; x <= Cx + 1; x++) {
			for (int y = Cy - 1; y <= Cy + 1; y++) {
				@SuppressWarnings("unused")
				Chunk chunk = chunkProvider.provideChunk(x,  y);
			}
		}
		System.out.println("Chunks loaded: " + chunkProvider.loadedChunks.getSize());
	}
	
	public void tick()
	{
		updateEntities();
		updateBlocks();
		if (totalTicks++ % 1000 == 0)
		{
			//System.out.println("blocks.size() = " + blocks.size());
			System.out.println("player.isCollided = " + player.isCollided);
		}
	}
	
	public void checkCollisions()
	{
		/* Moved to Entity.moveEntity() */
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
		player.onUpdate();
	}
	
	public void newBlock(Block block)
	{
		blocks.add(block);
	}
	
	public Block getBlockUnderEntity(Entity e)
	{
		Iterator<Block> iter = blocks.iterator();
		while (iter.hasNext())
		{
			Block b = iter.next();
			double delta = e.bb.y1 - b.bb.y2;
			if ( delta > 0 && delta < 0.1 && ( b.bb.posX > e.bb.x1 && b.bb.posX < e.bb.x2 )) {
				return b;
			}
		}
		return null;
	}
	
	public Chunk getChunkFromChunkCoords(int x_, int y_)
	{
		return chunkProvider.provideChunk(x_, y_);
	}
	
	public Chunk getChunkFromBlockCoords(int x_, int y_)
	{
		return getChunkFromChunkCoords(x_ >> 4, y_ >> 4);
	}
	
	public int getBlockID(int x_, int y_)
	{
		return getChunkFromBlockCoords(x_, y_).getBlockID(x_ & 16, y_ & 16);
	}
	
}