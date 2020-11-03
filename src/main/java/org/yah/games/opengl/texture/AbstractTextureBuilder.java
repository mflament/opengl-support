/**
 * 
 */
package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BORDER_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameterfv;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexParameteriv;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_A;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_B;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_G;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_R;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_RGBA;

import java.nio.ByteBuffer;

/**
 * @author Yah
 *
 */
abstract class AbstractTextureBuilder<T extends Texture, B extends AbstractTextureBuilder<T, B>> {

    protected final int id;

    protected TextureInternalFormat internalFormat = TextureInternalFormat.RGBA;

    private final int target;

    private boolean dataSet;

    public AbstractTextureBuilder(int target) {
        this.target = target;
        id = glGenTextures();
        glBindTexture(target, id);
    }

    abstract B self();

    protected abstract T createTexture();

    public B withInternalFormat(TextureInternalFormat internalFormat) {
        this.internalFormat = internalFormat;
        return self();
    }

    public B wrapS(TextureWrap wrapMode) {
        wrap(GL_TEXTURE_WRAP_S, wrapMode);
        return self();
    }

    public B wrapT(TextureWrap wrapMode) {
        wrap(GL_TEXTURE_WRAP_T, wrapMode);
        return self();
    }

    public B wrapR(TextureWrap wrapMode) {
        wrap(GL_TEXTURE_WRAP_R, wrapMode);
        return self();
    }

    public B swizzleR(TextureSwizzle swizzle) {
        swizzle(GL_TEXTURE_SWIZZLE_R, swizzle);
        return self();
    }

    public B swizzleG(TextureSwizzle swizzle) {
        swizzle(GL_TEXTURE_SWIZZLE_G, swizzle);
        return self();
    }

    public B swizzleB(TextureSwizzle swizzle) {
        swizzle(GL_TEXTURE_SWIZZLE_B, swizzle);
        return self();
    }

    public B swizzleA(TextureSwizzle swizzle) {
        swizzle(GL_TEXTURE_SWIZZLE_A, swizzle);
        return self();
    }

    public B swizzleRGBA(TextureSwizzle sr, TextureSwizzle sg, TextureSwizzle sb, TextureSwizzle sa) {
        glTexParameteriv(target, GL_TEXTURE_SWIZZLE_RGBA,
                new int[] { sr.getGlType(), sg.getGlType(), sb.getGlType(), sa.getGlType() });
        return self();
    }

    public B minFilter(TextureMinFilter filter) {
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, filter.getGlType());
        return self();
    }

    public B magFilter(TextureMagFilter filter) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, filter.getGlType());
        return self();
    }

    public B borderColor(float r, float g, float b, float a) {
        glTexParameterfv(target, GL_TEXTURE_BORDER_COLOR, new float[] { r, g, b, a });
        return self();
    }

    protected abstract void setData(int level, int internalFormat, int textureFormat, int dataType, ByteBuffer data);

    public final B withData(int level, TextureFormat format, TextureDataType dataType, ByteBuffer data) {
        setData(level, internalFormat.id(), format.id(), dataType.id(), data);
        dataSet = true;
        return self();
    }

    protected void wrap(int coord, TextureWrap wrapMode) {
        glTexParameteri(target, coord, wrapMode.getGlType());
    }

    protected void swizzle(int swizzleName, TextureSwizzle swizzle) {
        glTexParameteri(target, swizzleName, swizzle.getGlType());
    }

    public T build() {
        if (!dataSet) {
            setData(0, internalFormat.id(),
                    TextureFormat.RGBA.id(),
                    TextureDataType.UNSIGNED_BYTE.id(), null);
        }
        return createTexture();
    }

}
