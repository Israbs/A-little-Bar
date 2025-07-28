package com.little.bar.Objects;

// package com.tuempresa.bar.objects;

import com.badlogic.gdx.graphics.Texture;

public class GameObject {

    protected int gridX, gridY;
    protected Texture texture;

    // Constructor
    public GameObject(Texture texture, int gridX, int gridY) {
        this.texture = texture;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    // Getters para acceder a las propiedades desde fuera
    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public Texture getTexture() {
        return texture;
    }
}
