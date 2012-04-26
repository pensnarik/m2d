package com.mutex.m2d;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import java.awt.Font;

import java.io.IOException;

@SuppressWarnings("deprecation")

public class Renderer {
	public Texture texDirt; 
	public Texture textPlayer;
	public World world;
	private TrueTypeFont font;
	
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
		Font awtFont = new Font("Courier New", Font.BOLD, 42);		
		font = new TrueTypeFont(awtFont, false);		
	}
	
	public void loadTextures()
	{
		try {
			texDirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/dirt.png"));
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
		
		DrawTexturedBox(0, 0, 40, 40, textPlayer.getTextureID());
	}
	
	public void renderMap()
	{
		for (int i = 0; i < world.chunkProvider.loadedChunks.getSize(); i++)
		{
			Chunk chunk = (Chunk) world.chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.height * Chunk.height; j++) {
					Block b = chunk.blocks[j];
					DrawTexturedBox(b.x*32 - 16, b.y*32 - 16, b.x*32 + 16, b.y*32 + 16, texDirt.getTextureID());
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
		Main.fr.renderString("012345", 100, 100, 2);
	}
}