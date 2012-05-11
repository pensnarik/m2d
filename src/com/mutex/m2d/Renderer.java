package com.mutex.m2d;

import org.newdawn.slick.opengl.Texture;

import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;

public class Renderer {
	public Texture textureDirt;
	public Texture textureStone;
	public Texture texturePlayer;
	public Texture textureSky;
	public Texture textureGray;
	public Texture textureInventory;
	public World world;
	public final int scale = 2;
	public final int blockSize = 16;
	public int selectedBlockX;
	public int selectedBlockY;
	public boolean isBlockSelected;
	public double mouseSceneX = 0;
	public double mouseSceneY = 0;
	
	public void doRender()
	{
		renderSky();
		renderMap();
		//drawGrid();
		renderPlayer();
		//testVBO();
		renderEntities();
		renderOverlay();		
	}
	
	public void setSelectedBlock(int x, int y)
	{
		selectedBlockX = x;
		selectedBlockY = y;
		isBlockSelected = true;
	}
	
	public void unselectBlock()
	{
		isBlockSelected = false;
	}
	
	public Renderer(World world_)
	{
		world = world_;
		isBlockSelected = false;
	}
	
	public void init()
	{
		loadTextures();
		loadFonts();
	}
	
	public void loadFonts()
	{	
	}
	
	public void loadTextures()
	{
		try {
			textureDirt      = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/dirt.png"));
			textureStone     = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/stone.png"));
			texturePlayer    = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/player.png"));
			textureSky       = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/sky.png"));			
			textureGray      = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/gray.png"));
			textureInventory = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/inventory.png"));
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void renderSky()
	{
		setupOverlayRendering();
		DrawTexturedBox(0 - Game.DisplayWidth / 2, 0 - Game.DisplayHeight / 2, Game.DisplayWidth / 2, Game.DisplayHeight / 2, textureSky.getTextureID());
		//glBindTexture(GL_TEXTURE_2D, textureSky.getTextureID());
		//renderTexturedRect(-400, -300, 0, 0, 256, 256);
	}
	
	public void testVBO()
	{
		Screever screever = Screever.instance;

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, textureStone.getTextureID());
		screever.startDrawingQuads();
		screever.addVertexWithUV(-1, -1, -1, -1);
		screever.addVertexWithUV(-1,  1, -1,  1);
		screever.addVertexWithUV( 1,  1,  1,  1);
		screever.addVertexWithUV( 1, -1,  1, -1);
		screever.draw();
		glDisable(GL_TEXTURE_2D);
	}
	
	public void drawGrid()
	{
		Screever screever = Screever.instance;
		screever.startDrawing(GL_LINES);
		glPushMatrix();
		glColor3f(0, 1, 0);
		
		for (int y = -10; y <= 10; y++)
		{
			for (int x = -10; x <= 10; x ++)
			{
				screever.addVertex(x, -10);
				screever.addVertex(x, 10);
			}
			screever.addVertex(-10, y);
			screever.addVertex(10, y);
		}
		screever.draw();
		glPopMatrix();
	}
	
	public void renderPlayer()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		float x1 = (float) world.player.boundingBox.minX;
		float y1 = (float) world.player.boundingBox.minY;
		float x2 = (float) world.player.boundingBox.maxX;
		float y2 = (float) world.player.boundingBox.maxY;
		DrawTexturedBox(x1, y1, x2, y2, texturePlayer.getTextureID());
		//renderAABB(world.player.boundingBox);
	}
	
	public void renderMap()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//glTranslatef((float)world.player.posX, (float)world.player.posY, 0);
		//glPushMatrix();
		for (int i = 0; i < world.chunkProvider.loadedChunks.getSize(); i++)
		{
			Chunk chunk = (Chunk) world.chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.width * Chunk.height; j++) {
					int blockID = chunk.blocks[j];
					
					float x1 = (chunk.x * Chunk.width + ((j & 0x3f)));
					float x2 = x1 + 1;
					float y1 = (chunk.y * Chunk.height + ((j & 0xfc0) >> 6));
					float y2 = y1 + 1;
					if (!isCoordInScreen(x1, y1)) continue;
					if (blockID == Block.blockDirt.blockID) {
						DrawTexturedBox(x1, y1, x2, y2, textureDirt.getTextureID());
					} else if (blockID == Block.blockStone.blockID) {
						DrawTexturedBox(x1, y1, x2, y2, textureStone.getTextureID());
					} else {
						continue;
					}
					
			}
		}
		renderChunkBorders();
		renderSelectedBlock();
		//glPopMatrix();
	}
	
	public void renderSelectedBlock()
	{
		if (!isBlockSelected)
		{
			//if (mouseSceneX <= 0) return;
			BoundingBox boundingBox = BoundingBox.getBoundingBox(mouseSceneX, mouseSceneY, mouseSceneX + 1, mouseSceneY + 1);
			renderAABB(boundingBox);
		}
		else
		{
			int blockID = world.getBlockID(selectedBlockX, selectedBlockY);
			/* TODO: Change to getSelectionBoundingBoxFromPool */
			if (blockID > 0)
			{
				BoundingBox boundingBox = Block.blocksList[blockID].getCollisionBoundingBoxFromPool(world, selectedBlockX, selectedBlockY);
				renderAABB(boundingBox);
			}
		}
	}
	
	public boolean isCoordInScreen(float x, float y)
	{
		return ((world.player.posX - x) > -13 & (world.player.posX - x) < 14 &
				(world.player.posY - y) > -12 & (world.player.posY - y)  < 12);
	}
	
	public void DrawTexturedBox(float x1, float y1, float x2, float y2, int textureID)
	{
		Screever screever = Screever.instance;
		
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);	
		
		glColor3f(1, 1, 1);
		screever.startDrawingQuads();
		screever.addVertexWithUV(x1, y1, 0, 1);
		screever.addVertexWithUV(x2, y1, 1, 1);
		screever.addVertexWithUV(x2, y2, 1, 0);
		screever.addVertexWithUV(x1, y2, 0, 0);
		screever.draw();

		glDisable(GL_TEXTURE_2D);		
	}
	
	public void drawPartOfTexture(float x1, float y1, float x2, float y2, float tx1, float ty1, float tx2, float ty2, int textureID)
	{
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		glColor3f(0, 0, 0f);
		glBegin(GL_QUADS);
			glTexCoord2f(tx1, ty1); glVertex2f(x1, y1);
			glTexCoord2f(tx2, ty1); glVertex2f(x2, y1);
			glTexCoord2f(tx2, ty2); glVertex2f(x2, y2);
			glTexCoord2f(tx1, ty2); glVertex2f(x1, y2);
		glEnd();
	}
	
	public void renderOverlay()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-400, 400, -300, 300, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
		glColor3f(1, 0, 0);
		glTranslatef(0, 0, 0);		
		Screever screever = Screever.instance;
		
		glBindTexture(GL_TEXTURE_2D, textureGray.getTextureID());
		int w = Game.DisplayWidth; int h = Game.DisplayHeight;
		
		int x = Mouse.getX();
		int y = Mouse.getY();
		glColor3f(1, 1, 1);
		Game.fr.renderString("m2d, version 0.0.1a", -380, 290, 1);
		Game.fr.renderString("Mouse X:" + getSceneCoordX(x), -380, 270, 1);
		Game.fr.renderString("Mouse Y:" + getSceneCoordY(y), -380, 260, 1);
		Game.fr.renderString("Player X:" + world.player.posX, -380, 250, 1);
		Game.fr.renderString("Player Y:" + world.player.posY, -380, 240, 1);
		Game.fr.renderString("fps: " + Game.lastFps, -380, 230, 1);
		Game.fr.renderString("Memory: " + Game.memoryUsed + "MB", -380, 220, 1);
		Game.fr.renderString("Age: " + Game.player.entityAge, -380, 210, 1);
		Game.fr.renderString("Ticks: " + Game.ticksRun, -380, 200, 1);
		//Game.fr.renderString("" + world.player.stopTimer, -370, 220, 1);
		setupOverlayRendering();
		renderInventary(w, h);
		glPopMatrix();
	}
	
	public void renderInventary(int w, int h)
	{
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4f(1, 1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureInventory.getTextureID());
		renderTexturedRect(w/2 - 122, 10, 0, 0, 112, 46);
		//drawPartOfTexture(100, 100, 116, 116, 0, 0, 1, 1, textureStone.getTextureID());
		
		glDisable(GL_TEXTURE_2D);
	}
	
	public void setupOverlayRendering()
	{
		//glClear(256);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 400, 300, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//glTranslatef(0, 0, 0);
	}
	
	public void renderTexturedRect(int x, int y, int tx1, int ty1, int tx2, int ty2)
	{
		/*
		Screever screever = Screever.instance;
		screever.startDrawingQuads();
		float fScale = 1/256;
		
		screever.addVertexWithUV(x + 0,   y + ty2,  (float)(tx1 + 0)   * fScale, (float) (ty1 + ty2) * fScale);
		screever.addVertexWithUV(x + tx2, y + ty2,  (float)(tx1 + tx2) * fScale, (float) (ty1 + ty2) * fScale);
		screever.addVertexWithUV(x + tx2, y + 0, 	(float)(tx1 + tx2) * fScale, (float) (ty1 + 0) * fScale);
		screever.addVertexWithUV(x + 0,   y + 0, 	(float)(tx1 + 0)   * fScale, (float) (ty1 + 0) * fScale);
		
		screever.draw();
		*/
		float fScale = (float) 0.00390625f;
		
		float tx1_ = (float) (tx1 * fScale);
		float tx2_ = (float) ((tx2) * fScale);
		float ty1_ = (float) ((ty1) * fScale);
		float ty2_ = (float) ((ty2) * fScale);
		
		glColor4f(1, 1, 1, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(tx1_, ty2_);  	glVertex2f(x + 0,   y + ty2 - ty1);
		glTexCoord2f(tx2_, ty2_);   glVertex2f(x + tx2 - tx1, y + ty2 - ty1); 
		glTexCoord2f(tx2_, ty1_); 	glVertex2f(x + tx2 - tx1, y + 0);	
		glTexCoord2f(tx1_, ty1_);  	glVertex2f(x + 0,   y + 0);	
		glEnd();
	}
	
	public int getScreenCoordX(double x_)
	{
		return (int) (x_ * scale * blockSize - world.player.posX * scale * blockSize);
	}
	
	public int getScreenCoordY(double y_)
	{
		return (int) (y_ * scale * blockSize - world.player.posY * scale * blockSize);
	}
	
	public double getSceneCoordX(int x_)
	{
		return (double) world.player.posX + (double) ((x_ - Game.DisplayWidth / 2) / ((double) (scale * blockSize)));
	}
	
	public double getSceneCoordY(int y_)
	{
		return (double) world.player.posY + (double) ((y_ - Game.DisplayHeight / 2) / ((double) (scale * blockSize)));	
	}
	
	/*
	 * Отрисовка контуров bounding box'a
	 */
	public void renderAABB(BoundingBox bb)
	{
		Screever screever = Screever.instance;
		screever.startDrawing(GL_LINES);
		glColor4f(0.2f, 0.2f, 0.2f, 0.5f);
		//glLineWidth(2.0f);
		screever.addVertex(bb.minX, bb.minY);
		screever.addVertex(bb.minX, bb.maxY);
		screever.addVertex(bb.minX, bb.maxY);
		screever.addVertex(bb.maxX, bb.maxY);
		screever.addVertex(bb.maxX, bb.maxY);
		screever.addVertex(bb.maxX, bb.minY);
		screever.addVertex(bb.maxX, bb.minY);
		screever.addVertex(bb.minX, bb.minY);
		screever.draw();
	}
	
	public void renderListAABB(List list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			renderAABB((BoundingBox) list.get(i));
		}
	}
	
	public void renderChunkBorders()
	{
		int gridSize = 6;
		Screever screever = Screever.instance;
		screever.startDrawing(GL_LINES);
		glColor3f(0, 0, 0);
		glLineWidth(2);
		for (int x = -Chunk.width * gridSize; x <= Chunk.width * gridSize; x += Chunk.width )
		{
			for (int y = -Chunk.height * gridSize; y <= Chunk.height * gridSize; y += Chunk.height)
			{
				screever.addVertex(-Chunk.width * gridSize, y);
				screever.addVertex( Chunk.width * gridSize, y);			
			}
			screever.addVertex(x, -Chunk.height * gridSize);
			screever.addVertex(x,  Chunk.height * gridSize);
		}
		screever.draw();
	}
	
	public void renderEntities()
	{
		for (int i = 0; i < world.listEntities.size(); i++)
		{
			Entity e = (Entity)(world.listEntities.get(i));
			if (e instanceof EntityItem)
			{
				EntityItem item = (EntityItem) e;
				if (item.item.itemID == Item.itemBlock.itemID)
				{
					//ItemBlock itemBlock = (ItemBlock) item.item;
					DrawTexturedBox((float)e.boundingBox.minX, (float)e.boundingBox.minY,
									(float)e.boundingBox.maxX, (float)e.boundingBox.maxY,
									textureDirt.getTextureID());
					glLineWidth(1);			
					renderAABB(e.boundingBox);
				}
			}
		}
	}
}