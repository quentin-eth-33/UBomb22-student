package fr.ubx.poo.ubomb.game;

public record Position (int x, int y) {

    public Position(Position position) {
        this(position.x, position.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position pos)
            return this.x == pos.x() && this.y == pos.y();
        return false;
    }
}