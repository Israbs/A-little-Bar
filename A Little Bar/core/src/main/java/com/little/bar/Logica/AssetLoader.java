package com.little.bar.Logica;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AssetLoader {

    public final AssetManager manager = new AssetManager();

    // Nombres de los archivos de assets para no tener "strings mágicos" por el código
    public static final String FLOOR_TILE_TEXTURE = "tile.png";
    public static final String TABLE_TEXTURE = "table.png";
    public static final String FONT = "my-font.fnt";

    // Referencias a los assets una vez cargados
    public static Texture floorTile;
    public static Texture table;
    public static Skin skin;

    // Método para poner en cola todos los assets que queremos cargar
    public void queueAssetsToLoad() {
        manager.load(FLOOR_TILE_TEXTURE, Texture.class);
        manager.load(TABLE_TEXTURE, Texture.class);
        manager.load(FONT, BitmapFont.class);
    }

    // Método que se llama cuando el AssetManager ha terminado de cargar todo
    public void assignAssetsAfterLoading() {
        floorTile = manager.get(FLOOR_TILE_TEXTURE, Texture.class);
        table = manager.get(TABLE_TEXTURE, Texture.class);

        // Construimos el Skin aquí, una sola vez, usando los recursos ya cargados
        buildSkin();
    }

    private void buildSkin() {
        skin = new Skin();
        skin.add("default", manager.get(FONT, BitmapFont.class));

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.getFont("default"), Color.WHITE);
        skin.add("default", labelStyle);

        // ... Aquí también va el código para crear el TextButtonStyle ...
        // TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        // ...
        // skin.add("default", textButtonStyle);
    }

    public void dispose() {
        manager.dispose();
        skin.dispose();
    }
}
