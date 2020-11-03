package org.yah.games.opengl.texture;

public interface Texture {

    TextureTarget getTarget();
    
    TextureInternalFormat getInternalFormat();

    void bind();

    void delete();
}