package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;

public class Box extends Decor{
    private boolean addToSprite;

    public Box(Position position) {
        super(position);
        this.setIsAccessible(false);
        this.addToSprite =true;
    }

    public Box(Position position, boolean AddToSprite) {
        super(position);
        setIsAccessible(false);
        this.addToSprite = AddToSprite;
    }

    public void setAddToSprite(boolean val){
        this.addToSprite =val;
    }

    public boolean getAddToSprite(){
        return this.addToSprite;
    }



    @Override
    public boolean explode() {
        this.remove();
        return false;
    }
}
