package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.decor.bonus.*;

// Double dispatch visitor pattern
public interface TakeVisitor {
    // Key
    default void take(Key key) {}
    default void take(BombNumberInc ab) {}

    default void take(BombNumberDec db) {}

    default void take(Heart al) {}

    default void take(BombRangeInc bomb) {}

    default void take(BombRangeDec bomb) {}

}