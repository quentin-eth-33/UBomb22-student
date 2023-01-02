package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.go.decor.bonus.Bomb;

import static fr.ubx.poo.ubomb.launcher.Entity.*;

public class MapLevelDefault extends MapLevel {
    private final static Entity[][] level1 = {
            {Empty, Heart, Heart, BombNumberDec, BombNumberInc, BombRangeDec, BombRangeInc, BombRangeInc, BombRangeInc, BombRangeInc, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Empty, Stone, Empty, Box, Box, Box, Empty},
            {Empty, Empty, Empty, Empty, Stone, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Empty, Box, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Tree, Empty, Tree, Empty, Heart, Empty, Box, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Tree, Empty, Heart, Heart, Heart, Empty, Stone, Empty, Empty},
            {Empty, Tree, Tree, Tree, Empty, Heart, Heart, Heart, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Stone, Stone, Stone, Stone, Stone, Empty, Empty, Empty, Stone, Stone, Empty, Stone},
            {Empty, DoorNextOpened, DoorNextClosed, DoorPrevOpened, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Monster, Empty, Empty, Empty, Empty, Empty, Empty, Stone, Monster, Princess}
    };
    private final static int width = 12;
    private final static int height = 13;

    public MapLevelDefault() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }
}