package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;

import java.nio.ByteBuffer;

import org.yah.games.opengl.GLObject;

public final class Texture2D extends GLObject implements Texture {

    private final TextureInternalFormat internalFormat;

    private final int width, height;

    private Texture2D(Builder builder) {
        super(builder.id);
        this.width = builder.width;
        this.height = builder.height;
        this.internalFormat = builder.internalFormat;
    }

    @Override
    public TextureTarget getTarget() { return TextureTarget.TEXTURE_2D; }

    @Override
    public TextureInternalFormat getInternalFormat() { return internalFormat; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void updateData(TextureFormat format, TextureDataType dataType, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat.id(), width, height, 0, format.id(),
                dataType.id(), data);
    }

    public void updateData(int level, int xOffset, int yOffset, int width, int height, TextureFormat format,
            TextureDataType dataType, ByteBuffer data) {
        glTexSubImage2D(GL_TEXTURE_2D, level, xOffset, yOffset, width, height, format.id(), dataType.id(),
                data);
    }

    public void getData(int level, TextureFormat format, TextureDataType dataType, ByteBuffer buffer) {
        glGetTexImage(GL_TEXTURE_2D, level, format.id(), dataType.id(), buffer);
    }

    @Override
    public void delete() {
        glDeleteTextures(id);
    }

    public static Builder builder(int width, int height) {
        return new Builder(width, height);
    }

    public final static class Builder extends AbstractTextureBuilder<Texture2D, Builder> {

        public final int width;

        public final int height;

        public Builder(int width, int height) {
            super(GL_TEXTURE_2D);
            this.width = width;
            this.height = height;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        protected Texture2D createTexture() {
            return new Texture2D(this);
        }

        @Override
        protected void setData(int level, int internalFormat, int format, int dataType, ByteBuffer data) {
            if (data != null)
                glTexImage2D(GL_TEXTURE_2D, level, internalFormat, width, height, 0, format, dataType, data);
            else
                glTexImage2D(GL_TEXTURE_2D, level, internalFormat, width, height, 0, format, dataType, 0);
        }

    }

}
