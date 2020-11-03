package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.glTexImage1D;
import static org.lwjgl.opengl.GL11.glTexSubImage1D;

import java.nio.ByteBuffer;

import org.yah.games.opengl.GLObject;

public final class Texture1D extends GLObject implements Texture {

    private final TextureInternalFormat internalFormat;

    private final int size;

    private Texture1D(Builder builder) {
        super(builder.id);
        this.size = builder.size;
        this.internalFormat = builder.internalFormat;
    }

    @Override
    public TextureInternalFormat getInternalFormat() { return internalFormat; }

    public int getSize() { return size; }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_1D, id);
    }

    @Override
    public TextureTarget getTarget() { return TextureTarget.TEXTURE_1D; }

    public void updateData(TextureFormat format, TextureDataType dataType, ByteBuffer data) {
        glTexImage1D(GL_TEXTURE_1D, 0, internalFormat.id(), size, 0, format.id(),
                dataType.id(), data);
    }

    public void updateData(int level, int offset, int size, TextureFormat format, TextureDataType dataType,
            ByteBuffer data) {
        glTexSubImage1D(GL_TEXTURE_1D, level, offset, size, format.id(), dataType.id(), data);
    }

    public void getData(int level, TextureFormat format, TextureDataType dataType, ByteBuffer buffer) {
        glGetTexImage(GL_TEXTURE_1D, level, format.id(), dataType.id(), buffer);
    }

    @Override
    public void delete() {
        glDeleteTextures(id);
    }

    public static Builder builder(int size) {
        return new Builder(size);
    }

    public final static class Builder extends AbstractTextureBuilder<Texture1D, Builder> {

        public final int size;

        public Builder(int size) {
            super(GL_TEXTURE_1D);
            this.size = size;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        protected Texture1D createTexture() {
            return new Texture1D(this);
        }

        @Override
        protected void setData(int level, int internalFormat, int textureFormat, int dataType, ByteBuffer data) {
            if (data != null)
                glTexImage1D(GL_TEXTURE_1D, level, internalFormat, size, 0, textureFormat, dataType, data);
            else
                glTexImage1D(GL_TEXTURE_1D, level, internalFormat, size, 0, textureFormat, dataType, 0);
        }
    }

}
