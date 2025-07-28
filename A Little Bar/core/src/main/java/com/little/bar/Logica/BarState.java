package com.little.bar.Logica;

import com.little.bar.Objects.GameObject;

import java.util.ArrayList;

public class BarState {
    private int money;
    private ArrayList<GameObject> furniture = new ArrayList<>(); // Lista de todos los muebles del bar

    public void BarState() {
        this.money = 100; // Empezamos con algo de dinero
    }

    // --- Getters y Setters ---

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void spendMoney(int amount) {
        this.money -= amount;
    }

    public ArrayList<GameObject> getFurniture() {
        return furniture;
    }

    // Podríamos añadir más cosas en el futuro:
    // - Nivel del bar
    // - Recetas desbloqueadas
    // - etc.
}
