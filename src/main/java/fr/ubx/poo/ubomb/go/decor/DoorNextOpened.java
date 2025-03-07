package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;

public class DoorNextOpened extends Decor {

    private boolean isUpdate;

    private boolean isAddToSprite;
    public DoorNextOpened(Position position) {
        super(position);
        this.isUpdate = true;
        this.isAddToSprite =true;
    }

    public DoorNextOpened(Position position, boolean isUpdate,boolean isAddToSprite) {
        super(position);
        this.isUpdate = isUpdate;
        this.isAddToSprite =isAddToSprite;
    }

    public void setIsUpdate(boolean val){
        this.isUpdate=val;
    }

    public boolean getIsUpdate(){
        return this.isUpdate;
    }

    public void setIsAddToSprite(boolean val){
        this.isAddToSprite=val;
    }

    public boolean getIsAddToSprite(){
        return this.isAddToSprite;
    }
}