package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.*;

import fr.ubx.poo.ubomb.view.Sprite;
import fr.ubx.poo.ubomb.view.SpriteFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Game {

    private final Configuration configuration;
    private final Player player;

    private final Grid[] grid;
    private final int nbLevels;

    public Game(Configuration configuration, Grid grid[]) {
        this.configuration = configuration;
        this.grid = grid;
        this.nbLevels = grid.length;
        this.player = new Player(this, configuration.playerPosition());
        this.player.setInvincibilityTime(configuration.playerInvisibilityTime());

        for(int i = 1 ;i <= nbLevels; i++) {
            for(Monster monster : ((Level)(this.grid[i-1])).getMonstersWithoutGame()){
                monster = new Monster(this, monster.getPosition());
                monster.setLives(1 + i/2);
                ((Level)(this.grid[i-1])).addMonster(monster);
            }
        }
    }

    public Configuration configuration() {
        return configuration;
    }

    // Returns the player, monsters and bomb at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (player().getPosition().equals(position))
            gos.add(player);
        return gos;
    }

    public Grid grid(int index) {
        return grid[index-1];
    }
    public Grid[] grids() {
        return grid;
    }


    public Player player() {
        return this.player;
    }

    public int getNbLevels() {
        return nbLevels;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}