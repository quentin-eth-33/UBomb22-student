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

    public void setAddToSprite(boolean val){
        this.AddToSprite =val;
    }

    public boolean getAddToSprite(){
        return this.AddToSprite;
    }



    @Override
    public boolean explode() {
        this.remove();
        return false;
    }
}
