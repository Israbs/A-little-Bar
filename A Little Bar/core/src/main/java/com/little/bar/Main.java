package com.little.bar;

import com.badlogic.gdx.Game;
import com.little.bar.GameScreens.EndOfDayScreen;
import com.little.bar.GameScreens.GameplayScreen;
import com.little.bar.GameScreens.UpgradeScreen;
import com.little.bar.Logica.BarState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public BarState barState;

    // Es buena práctica tener una referencia a las pantallas para no crearlas constantemente
    public GameplayScreen gameplayScreen;
    public EndOfDayScreen endOfDayScreen;
    public UpgradeScreen upgradeScreen;

    @Override
    public void create() {
        // Creamos el estado del bar una sola vez
        barState = new BarState();

        // Creamos las instancias de las pantallas
        // Les pasamos una referencia a 'this' (la clase Game) para que puedan cambiar de pantalla
        gameplayScreen = new GameplayScreen(this);
        endOfDayScreen = new EndOfDayScreen(this);
        upgradeScreen = new UpgradeScreen(this);

        // Decidimos con qué pantalla empezar el juego.
        // Para el desarrollo, empezaremos directamente en la pantalla de mejoras.
        // Más adelante, podríamos empezar en un menú principal.
        this.setScreen(upgradeScreen);
    }
}
