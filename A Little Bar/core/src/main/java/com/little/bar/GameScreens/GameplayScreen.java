package com.little.bar.GameScreens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.little.bar.Main; // Usa tu clase Game principal
import com.little.bar.Render.WorldRenderer;

public class GameplayScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private WorldRenderer worldRenderer;

    // UI
    private Stage uiStage;
    private Skin uiSkin;
    private Label moneyLabel;
    private Label timerLabel;

    // Lógica del juego
    private float dayTimer;
    private static final float DAY_DURATION = 120; // 120 segundos = 2 minutos

    public GameplayScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Configuración del mundo (Cámara y Viewport)
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // ¡Reutilizamos el código! Creamos el renderer
        // Necesitamos una textura para el suelo. En lugar de cargarla aquí, la pasaremos desde la clase Game en el futuro.
        // Por ahora, la cargamos de nuevo para que funcione.
        Texture tileTexture = new Texture(Gdx.files.internal("floor-tile.png"));
        worldRenderer = new WorldRenderer(tileTexture, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Configuración de la UI
        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage);
        buildSkin();
        buildUI();

        // Inicializa el temporizador
        dayTimer = DAY_DURATION;
    }

    private void buildSkin() {
        uiSkin = new Skin();
        BitmapFont font = new BitmapFont(Gdx.files.internal("fuente.fnt")); // Usa tu fuente
        uiSkin.add("default", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        uiSkin.add("default", labelStyle);
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().pad(20);

        moneyLabel = new Label("Dinero: $" + game.barState.getMoney(), uiSkin);
        timerLabel = new Label("Tiempo: " + (int)dayTimer, uiSkin);

        mainTable.add(moneyLabel).expandX().left();
        mainTable.add(timerLabel).expandX().right();

        uiStage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        // Lógica de actualización
        update(delta);

        // Limpieza de pantalla
        ScreenUtils.clear(0.345f, 0.392f, 0.282f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Dibujado del mundo
        batch.begin();
        worldRenderer.render(batch, game.barState);
        batch.end();

        // Dibujado de la UI
        uiStage.act(delta);
        uiStage.draw();
    }

    private void update(float delta) {
        // Actualizar el temporizador
        dayTimer -= delta;
        timerLabel.setText("Tiempo: " + (int)Math.ceil(dayTimer));

        // Comprobar si el día ha terminado
        if (dayTimer <= 0) {
            endDay();
        }
    }

    private void endDay() {
        System.out.println("¡El día ha terminado!");
        // En el futuro, pasaremos datos del día a la pantalla de resumen
        game.setScreen(game.endOfDayScreen);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        uiStage.dispose();
        uiSkin.dispose();
        // La textura del tile se libera en otro lado, lo arreglaremos con un AssetManager
    }

    // pause() y resume() se dejan vacíos por ahora
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
}
