package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;

public class Box extends Decor{
    private boolean AddToSprite;

    public Box(Position position) {
        super(position);
        setIsAccessible(false);
        AddToSprite =true;
    }

    public Box(Position position, boolean AddToSprite) {
        super(position);
        setIsAccessible(false);
        this.AddToSprite = AddToSprite;
    }

    public void isAddToSprite(boolean val){
        this.AddToSprite =val;
    }

    public boolean isAddToSprite(){
        return this.AddToSprite;
    }

    @Override
    public void explode() {
        this.remove();
    }
}
