package com.mutex.m2d;

import java.util.Random;

public class World {
	
	public ChunkProvider chunkProvider;
	public EntityPlayer player;
	public Random RandomGen;
	
	World()
	{
		RandomGen = new Random(1);
		chunkProvider = new ChunkProvider(this);
		int hash = chunkProvider.chunkCoordsToHash(2, 2);
		System.out.println("hash = " + hash);
		player = new EntityPlayer(this);
		prepareVisibleChunks();
	}
	
	private void prepareVisibleChunks()
	{
		int Cx = (int)(player.posX / Chunk.width);
		int Cy = (int)(player.posY / Chunk.height);
		for (int x = Cx - 1; x <= Cx + 1; x++) {
			for (int y = Cy - 1; y <= Cy + 1; y++) {
				Chunk chunk = chunkProvider.provideChunk(x,  y);
			}
		}
		System.out.println("Chunks loaded: " + chunkProvider.loadedChunks.getSize());
	}
	
	public void tick()
	{
		updateEntities();
		updateBlocks();
	}
	
	public void checkCollisions()
	{
		
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
	
}