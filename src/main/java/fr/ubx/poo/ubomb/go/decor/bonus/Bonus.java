/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public abstract class Bonus extends Decor implements Takeable {
    public Bonus(Position position) {
        super(position);
        setCanMoveMonster(true);
    }

    @Override
    public boolean walkableBy(Player player) {
        return true;
    }

    @Override
    public boolean explode() {
        remove();
        return true;
    }

    @Override
    public void takenBy(Player player){
    }

}