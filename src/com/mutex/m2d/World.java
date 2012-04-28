package com.mutex.m2d;

import java.util.Random;
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
		checkCollisions();
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
				Block b = chunk.blocks[j];
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
	
}