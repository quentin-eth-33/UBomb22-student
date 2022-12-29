/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;

public abstract class GameObject implements Walkable {
    public final Game game;
    private boolean deleted = false;
    private boolean modified = true;
    private Position position;

    private boolean isAccessible =true;

    private boolean canMoveMonster;

    public GameObject(Game game, Position position) {
        this.game = game;
        this.position = position;
    }

    public GameObject(Position position) {
        this(null, position);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        setModified(true);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void remove() {
        deleted = true;
    }

    public boolean explode() {
        return true;
    }

    public void setIsAccessible(boolean isAccessible) {
        this.isAccessible = isAccessible;
    }
    public boolean getIsAccessible() {
        return isAccessible;
    }

    public Game getGame() {
        return game;
    }

    public boolean getCanMoveMonster(){
        return canMoveMonster;
    }

    public void setCanMoveMonster(boolean canMoveMonster){
        this.canMoveMonster= canMoveMonster;
    }


}