package com.mutex.m2d;

import java.nio.*;
import org.lwjgl.opengl.*;

public class Screever
{
	public static final Screever instance = new Screever(0x200000);	
	private boolean useVBO;
	private int vboCount;
	private IntBuffer vertexBuffers;
	private int[] rawBuffer;
	private int rawBufferIndex;
	public double xOffset;
	public double yOffset;
	private int vertexCount;
	private int bufferSize;
	private boolean isDrawing;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private int vboIndex;
	private boolean hasTexture;
	private double textureU;
	private double textureV;
	private int drawMode;
	private boolean hasColor;
	private boolean isColorDisabled;
	private int color;
	
	private Screever(int bufferSize_)
	{
		vboCount = 10;
		rawBufferIndex = 0;
		vertexCount = 0;
		bufferSize = bufferSize_;
		rawBuffer = new int[bufferSize_];
		isDrawing = false;
		hasColor = false;
		isColorDisabled = false;
        byteBuffer = GLAllocation.createDirectByteBuffer(bufferSize * 4);
        intBuffer = byteBuffer.asIntBuffer();		
		useVBO = GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
        if(useVBO)
        {
            vertexBuffers = GLAllocation.createDirectIntBuffer(vboCount);
            ARBVertexBufferObject.glGenBuffersARB(vertexBuffers);
        }		
	}
	
	public void reset()
	{
		rawBufferIndex = 0;
		vertexCount = 0;
	}
	
	public void startDrawingQuads()
	{
		startDrawing(GL11.GL_QUADS);
	}
	
	public int draw()
	{
        if(!isDrawing)
        {
            throw new IllegalStateException("Not drawing!");
        }		
        isDrawing = false;
        if (vertexCount > 0)
        {
        	intBuffer.clear();
        	intBuffer.put(rawBuffer, 0, rawBufferIndex);
        	byteBuffer.position(0);
        	byteBuffer.limit(rawBufferIndex * 4);
        	if (useVBO)
        	{
        		vboIndex = (vboIndex + 1) % vboCount;
        		ARBVertexBufferObject.glBindBufferARB(GL15.GL_ARRAY_BUFFER, vertexBuffers.get(vboIndex));
        		ARBVertexBufferObject.glBufferDataARB(GL15.GL_ARRAY_BUFFER, byteBuffer, GL15.GL_STATIC_DRAW ); /* STREAM */
        		if (hasTexture)
        		{
        			if (useVBO)
        			{
        				/* void glTexCoordPointer(GLint size, GLenum type, GLsizei stride, const GLvoid *pointer); */
        				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
        			}
        			else
        			{
        				floatBuffer.position(3);
        				GL11.glTexCoordPointer(2, 32, floatBuffer);
        			}
        			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);        			
        		}
        		if(hasColor)
        		{
        			if(useVBO)
        			{
        				GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 0L);
        			}
        			else
        			{
        				byteBuffer.position(20);
        				GL11.glColorPointer(4, true, 32, byteBuffer);
        			}
        			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        		}
        		if (useVBO)
        		{
        			GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
        		}
        		else
        		{
        			floatBuffer.position(0);
        			GL11.glVertexPointer(3, 32, floatBuffer);
        		}
        		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        		GL11.glDrawArrays(drawMode, 0, vertexCount);
        		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        		if (hasTexture)
        		{
        			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        		}
        		if(hasColor)
        		{
        			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        		}
        	}
        }
        int i = rawBufferIndex * 4;
        reset();
        return i;
	}
	
	public void addVertex(double x, double y)
	{
		/*
		 * Формат бефера VBO:
		 *   position        normal     texture coords
		 * x    y    z  |  a    b   c |  U   V
		 */
		rawBuffer[rawBufferIndex + 0] = Float.floatToRawIntBits((float)(x + xOffset));
		rawBuffer[rawBufferIndex + 1] = Float.floatToRawIntBits((float)(y + yOffset));
		rawBuffer[rawBufferIndex + 2] = Float.floatToRawIntBits(0.0f);
		
		if(hasTexture)
		{
			rawBuffer[rawBufferIndex + 3] = Float.floatToRawIntBits((float)textureU);
			rawBuffer[rawBufferIndex + 4] = Float.floatToRawIntBits((float)textureV);
		}
		if(hasColor)
		{
			rawBuffer[rawBufferIndex + 5] = color;
		}
		
		rawBufferIndex += 8;
		vertexCount++;
		if (vertexCount % 4 == 0 && rawBufferIndex >= bufferSize - 32)
		{
			draw();
			isDrawing = true;
		}		
	}
	
	public void setTextureUV(double U, double V)
	{
		hasTexture = true;
		textureU = U;
		textureV = V;
	}
	
	public void startDrawing(int drawMode_)
	{
		if(isDrawing)
		{
			throw new IllegalStateException("Already drawing");
		}
		else
		{
			isDrawing = true;
			reset();
			drawMode = drawMode_;
			hasTexture = false;
			hasColor = false;
			isColorDisabled = false;
			return;
		}
	}
	
	public void addVertexWithUV(double x, double y, double U, double V)
	{
		setTextureUV(U, V);
		addVertex(x, y);
	}
	
	/* Colors */
	
	public void setColorRGBA(int r, int g, int b, int alpha)
	{
		if(isColorDisabled) return;
		hasColor = true;
		if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
		{
			color = alpha << 24 | b << 24 | g << 24 | r;
		}
		else
		{
			color = r << 24 | g << 24 | b << 24 | alpha;
		}
	}
	
	public void setColorOpaque(int r, int g, int b)
	{
		setColorRGBA(r, g, b, 255);
	}
}