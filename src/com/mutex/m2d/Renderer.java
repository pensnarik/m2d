package com.mutex.m2d;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

public class Renderer {
	public Texture texDirt;
	public Texture texStone;
	public Texture textPlayer;
	public World world;
	public final int scale = 2;
	public final int blockSize = 16;
	
	public Renderer(World world_)
	{
		world = world_;
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
			texDirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/dirt.png"));
			texStone = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/stone.png"));
			textPlayer = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/player.png"));
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawGrid()
	{
		glColor3f(0, 1, 0);
		for (int y = 0; y < Display.getHeight(); y+= 16) {
			for (int x = 0; x < Display.getWidth(); x += 16) {
				glBegin(GL_LINES);
					glVertex2f(x, 0);
					glVertex2f(x, Display.getHeight());
				glEnd();
			}
			glBegin(GL_LINES);
				glVertex2f(0, y);
				glVertex2f(Display.getWidth(), y);
			glEnd();
		}
	}
	
	public void renderPlayer()
	{
		double x = world.player.x;
		double y = world.player.y;
		float x1 = (float) x - blockSize / 2 * scale;
		float y1 = (float) y;
		float x2 = (float)(x + blockSize * scale);
		float y2 = (float)(y + blockSize * 2 * scale);
		DrawTexturedBox(x1, y1, x2, y2, textPlayer.getTextureID());
	}
	
	public void renderMap()
	{
		for (int i = 0; i < world.chunkProvider.loadedChunks.getSize(); i++)
		{
			Chunk chunk = (Chunk) world.chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.height * Chunk.height; j++) {
					Block b = chunk.blocks[j];
					int x1 = getScreenCoordX(b.x) - blockSize / 2 * scale;
					int x2 = getScreenCoordX(b.x) + blockSize / 2 * scale;
					int y1 = getScreenCoordY(b.y) - blockSize / 2 * scale;
					int y2 = getScreenCoordY(b.y) + blockSize / 2 * scale;
					if (b.blockID == 0) {
						DrawTexturedBox(x1, y1, x2, y2, texDirt.getTextureID());
					} else if (b.blockID == 1) {
						DrawTexturedBox(x1, y1, x2, y2, texStone.getTextureID());
					} else {
						continue;
					}
			}
		}
	}
	
	public void DrawTexturedBox(float x1, float y1, float x2, float y2, int textureID)
	{
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);	
			glTexCoord2f(0, 1);
			glVertex2f(x1, y1);
			glTexCoord2f(1, 1);
			glVertex2f(x2, y1);
			glTexCoord2f(1, 0);
			glVertex2f(x2, y2);
			glTexCoord2f(0, 0);
			glVertex2f(x1, y2);
		glEnd();
		glDisable(GL_TEXTURE_2D);		
	}
	
	public void drawPartOfTexture(float x1, float y1, float x2, float y2, float tx1, float ty1, float tx2, float ty2, int textureID)
	{
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		glColor3f(1, 1, 0.5f);
		glBegin(GL_QUADS);
			glTexCoord2f(tx1, ty1); glVertex2f(x1, y1);
			glTexCoord2f(tx2, ty1); glVertex2f(x2, y1);
			glTexCoord2f(tx2, ty2); glVertex2f(x2, y2);
			glTexCoord2f(tx1, ty2); glVertex2f(x1, y2);
		glEnd();
	}
	
	public void renderOverlay()
	{
		/*
		 This doesn't works properly now.
		 
		 Color.black.bind(); 
	     font.drawString(100, 200, "Hello!");
		*/
		//DrawTexturedBox(10, 10, 200, 300, texDirt.getTextureID());
		glPushMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,  800, 0, 600, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glTranslatef(0, 0, 0);
		int x = Mouse.getX();
		int y = Mouse.getY();
		Main.fr.renderString("" + x, -370, 270, 1);
		Main.fr.renderString("" + y, -370, 260, 1);		
		glPopMatrix();
	}
	
	public int getScreenCoordX(double x_)
	{
		return (int) (x_ * scale * blockSize - world.player.x * scale * blockSize);
	}
	
	public int getScreenCoordY(double y_)
	{
		return (int) (y_ * scale * blockSize - world.player.y * scale * blockSize);
	}
}