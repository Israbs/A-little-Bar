package com.little.bar.Render;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.little.bar.Logica.BarState;
import com.little.bar.Objects.GameObject;
import com.badlogic.gdx.graphics.Texture;

public class WorldRenderer {

    // Hacemos que las constantes del mundo vivan aquí
    public static final int TILE_WIDTH = 128;
    public static final int TILE_HEIGHT = 64;
    public static final int GRID_SIZE_X = 10;
    public static final int GRID_SIZE_Y = 10;

    private Texture tileTexture;
    private float mapOffsetX, mapOffsetY;

    public WorldRenderer(Texture tileTexture, float viewportWidth, float viewportHeight) {
        this.tileTexture = tileTexture;
        this.mapOffsetX = viewportWidth / 2 - TILE_WIDTH / 2;
        this.mapOffsetY = viewportHeight * 0.2f;
    }

    public void render(SpriteBatch batch, BarState barState) {

        for (int gridY = 0; gridY < GRID_SIZE_Y; gridY++) {
            for (int gridX = 0; gridX < GRID_SIZE_X; gridX++) {
                float screenX = (gridX - gridY) * (TILE_WIDTH / 2.0f);
                float screenY = (gridX + gridY) * (TILE_HEIGHT / 2.0f);
                batch.draw(tileTexture, screenX + mapOffsetX, screenY + mapOffsetY);
            }
        }

        barState.getFurniture().sort((o1, o2) -> Integer.compare(o2.getGridY(), o1.getGridY()));

        for (GameObject obj : barState.getFurniture()) {
            float screenX = (obj.getGridX() - obj.getGridY()) * (TILE_WIDTH / 2.0f);
            float screenY = (obj.getGridX() + obj.getGridY()) * (TILE_HEIGHT / 2.0f);
            batch.draw(obj.getTexture(), screenX + mapOffsetX, screenY + mapOffsetY + TILE_HEIGHT);
        }
    }

    public float getMapOffsetX() { return mapOffsetX; }
    public float getMapOffsetY() { return mapOffsetY; }
}
