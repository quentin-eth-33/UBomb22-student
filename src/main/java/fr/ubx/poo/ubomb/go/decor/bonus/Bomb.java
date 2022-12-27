package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;

public class Bomb extends Bonus{

    private int range;

    public Bomb(Position position, int range) {
        super(position);
        this.range =range;
    }
}
