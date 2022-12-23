package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Bonus{
    private static int range = 1;

    public Bomb(Position position) {
        super(position);
    }

    @Override
    public void takenBy(Player player) {
        player.take(this);
    }

    public static int getRange() {
        return range;
    }

    public static void setRange(int range) {
        Bomb.range = range;
    }


}
