package com.little.bar.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.little.bar.Main;
import com.little.bar.Objects.GameObject;

import java.util.ArrayList;

public class UpgradeScreen implements Screen {

    public static final int TILE_WIDTH = 128;
    public static final int TILE_HEIGHT = 64;
    public static final int GRID_SIZE_X = 10;
    public static final int GRID_SIZE_Y = 10;

    private float mapOffsetX;
    private float mapOffsetY;

    private final Main game;
    private SpriteBatch batch;
    private Texture tileTexture;
    private Skin uiSkin;
    private Label moneyLabel;
    private Texture tableTexture;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Stage uiStage;
    private GameObject selectedObjectToPlace;

    public UpgradeScreen(Main game) {
        this.game = game;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();

        uiSkin = new Skin();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("fuente.fnt"));
        uiSkin.add("default", myFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = uiSkin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        uiSkin.add("default", labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = uiSkin.getFont("default"); // Usa la misma fuente
        textButtonStyle.fontColor = Color.CYAN; // Pongámosle un color diferente para distinguirlo

        // Para que sea un "botón" real, necesita un fondo (Drawable).
        // Como no tenemos una imagen, podemos crear un fondo de color sólido programáticamente.
        // Esto es opcional, pero hace que se vea mejor.
        Pixmap pixmap = new Pixmap(100, 50, Pixmap.Format.RGBA8888); // Crea un rectángulo en memoria
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        textButtonStyle.up = new TextureRegionDrawable(new Texture(pixmap)); // Fondo para estado normal
        pixmap.dispose(); // Ya no necesitamos el pixmap

        // Añade el estilo al Skin con el nombre "default"
        uiSkin.add("default", textButtonStyle);

        tileTexture = new Texture(Gdx.files.internal("floor-tile.png"));
        tableTexture = new Texture(Gdx.files.internal("table.png"));

        game.barState.getFurniture().add(new GameObject(tableTexture, 1, 3)); // Mesa 1
        game.barState.getFurniture().add(new GameObject(tableTexture, 1, 1));

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        mapOffsetX = viewport.getWorldWidth() / 2 - TILE_WIDTH / 2;
        mapOffsetY = viewport.getWorldHeight() * 0.2f;
        camera.update();

        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage);

        uiStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Si el clic fue sobre un actor de la UI (como el botón), no hacemos nada aquí.
                if (event.getTarget() instanceof Image) {
                    return;
                }

                if (selectedObjectToPlace != null) {
                    // Convertir coordenadas de pantalla (x,y) a coordenadas de la cuadrícula
                    // ¡Ojo! Las coordenadas x, y del clic están en el espacio del Stage/Viewport.
                    // Tenemos que "deshacer" el offset del mapa.
                    float worldX = x - mapOffsetX;
                    float worldY = y - mapOffsetY - TILE_HEIGHT;

                    // Aplicamos la fórmula inversa
                    float gridX_float = (worldX / (TILE_WIDTH / 2.0f) + worldY / (TILE_HEIGHT / 2.0f)) / 2.0f;
                    float gridY_float = (worldY / (TILE_HEIGHT / 2.0f) - (worldX / (TILE_WIDTH / 2.0f))) / 2.0f;

                    // Redondeamos al entero más cercano
                    int gridX = Math.round(gridX_float);
                    int gridY = Math.round(gridY_float);

                    System.out.println("Clic en el mundo. Grid Coords: (" + gridX + ", " + gridY + ")");

                    // Añadimos el objeto a la lista de objetos del juego
                    game.barState.getFurniture().add(new GameObject(selectedObjectToPlace.getTexture(), gridX, gridY));

                    // Deseleccionamos el objeto, ya no lo tenemos "en la mano"
                    selectedObjectToPlace = null;

                    int tableCost = 50; // Mismo coste que definimos en el botón

                    // Añadimos el objeto a la lista
                    game.barState.getFurniture().add(new GameObject(selectedObjectToPlace.getTexture(), gridX, gridY));

                    // Restamos el dinero
                    game.barState.spendMoney(tableCost);

                    // Actualizamos el texto del label
                    moneyLabel.setText("Dinero: $" + game.barState.getMoney());

                    // Deseleccionamos el objeto
                    selectedObjectToPlace = null;
                }
            }
        });

        selectedObjectToPlace = null;
        buildUI();
    }

    private void buildUI() {
        uiStage.clear();

        Table infoTable = new Table();
        infoTable.setFillParent(true);
        infoTable.top().left();
        moneyLabel = new Label("Dinero: $" + game.barState.getMoney(), uiSkin);
        TextButton nextDayButton = new TextButton("Empezar Dia", uiSkin);
        nextDayButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.gameplayScreen);
            }
        });
        infoTable.add(nextDayButton).pad(20);
        infoTable.add(moneyLabel).pad(20);
        uiStage.addActor(infoTable);

        Table buildTable = new Table();
        buildTable.setFillParent(true);
        buildTable.bottom();

        // Creamos un "botón" a partir de la imagen de la mesa.
        // Hacemos una copia de la textura para no tener problemas si se libera en otro sitio
        Image tableButton = new Image(tableTexture);
        tableButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Aquí viene la lógica de coste
                int tableCost = 50; // Las mesas cuestan 50
                if (game.barState.getMoney() >= tableCost) {
                    System.out.println("Seleccionada mesa para colocar.");
                    selectedObjectToPlace = new GameObject(tableTexture, 0, 0);

                    // Opcional: añadir un "modo construcción" visual
                    // Podríamos cambiar el cursor o mostrar un objeto fantasma que siga al ratón.
                } else {
                    System.out.println("¡Dinero insuficiente!");
                }
            }
        });

        buildTable.add(tableButton).pad(20);
        uiStage.addActor(buildTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.345f, 0.392f, 0.282f, 1);

        // 2. Actualizamos la cámara
        camera.update();

        // 3. Le decimos al SpriteBatch que use las coordenadas de nuestra cámara
        batch.setProjectionMatrix(camera.combined);

        // 4. Empezamos a dibujar
        batch.begin();

        // 5. Dibujamos la cuadrícula
        for (int gridY = 0; gridY < GRID_SIZE_Y; gridY++) {
            for (int gridX = 0; gridX < GRID_SIZE_X; gridX++) {
                // Aplicamos la fórmula mágica
                float screenX = (gridX - gridY) * (TILE_WIDTH / 2.0f);
                float screenY = (gridX + gridY) * (TILE_HEIGHT / 2.0f);

                // Dibujamos el tile en la posición calculada
                batch.draw(tileTexture, screenX + mapOffsetX, screenY + mapOffsetY);
            }
        }

        game.barState.getFurniture().sort((o1, o2) -> {
            // Si Y son diferentes, el que tenga Y mayor va primero (se dibuja antes)
            if (o1.getGridY() != o2.getGridY()) {
                return Integer.compare(o2.getGridY(), o1.getGridY());
            }
            // Si Y son iguales, el que tenga X menor va primero.
            // Esto es opcional pero evita que los objetos "parpadeen" si están en la misma fila.
            return Integer.compare(o1.getGridX(), o2.getGridX());
        });
        for (GameObject obj : game.barState.getFurniture()) {
            // Usamos la misma fórmula isométrica
            float screenX = (obj.getGridX() - obj.getGridY()) * (TILE_WIDTH / 2.0f);
            float screenY = (obj.getGridX() + obj.getGridY()) * (TILE_HEIGHT / 2.0f);

            // Ojo al anclaje: puede que necesitemos ajustar la Y para que el objeto se pose sobre el tile.
            // La base del sprite debe coincidir con la "punta" superior del rombo del tile.
            // Por ahora, lo dibujamos en la posición base.
            batch.draw(obj.getTexture(), screenX + mapOffsetX, screenY + mapOffsetY + TILE_HEIGHT);
        }
        batch.end();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if (uiStage != null) {
            uiStage.getViewport().update(width, height, true); // Actualiza el viewport del Stage
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        tileTexture.dispose();
        tableTexture.dispose();
        if (uiStage != null) uiStage.dispose();
        if (uiSkin != null) uiSkin.dispose();
    }
}
