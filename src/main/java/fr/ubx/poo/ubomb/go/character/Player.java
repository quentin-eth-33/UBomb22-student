/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.TakeVisitor;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends Character implements Movable, TakeVisitor {


    // Ajout
    private int numberKeys =0;

    private int bombBagCapacity;
    private int bombRange = 1;

    private boolean princessFound = false;



    public Player(Game game, Position position) {
        super(game, position);
        this.setDirection(Direction.DOWN);
        this.setLives(game.configuration().playerLives());
        this.bombBagCapacity = game.getConfiguration().bombBagCapacity();
        setInvincibilityTime(4000);
        setTimerInvincibilityTime(new Timer(getInvincibilityTime()));
    }


    @Override
    public void take(Key key) {
        numberKeys++;
        System.out.println("Take the key ...");
        key.remove();
    }

    @Override
    public void take(BombNumberInc ab) {
        this.bombBagCapacity ++;
        ab.remove();
    }
    @Override
    public void take(BombNumberDec db) {
        if(this.bombBagCapacity>1){
            this.bombBagCapacity --;
            db.remove();
        }

    }

    @Override
    public void take(Heart al) {
        setLives(this.getLives()+1);
        al.remove();
    }


    @Override
    public void take(BombRangeInc ib) {
        this.bombRange ++;
        ib.remove();
    }
    @Override
    public void take(BombRangeDec db) {
        if (this.bombRange>1){
            this.bombRange --;
            db.remove();
        }

    }
    // Ajout
    public int getNumberKeys()
    {
        return numberKeys;
    }





    public boolean getPrincessFound() {
        return princessFound;
    }

    public void setPrincessFound(boolean val) {
        this.princessFound = val;
    }



    public void requestOpen(){
        Position nextPosition = this.getDirection().nextPosition(getPosition());
        GameObject nextObject = game.grid(inLevel).get(nextPosition);
        if(nextObject instanceof DoorNextClosed && this.getNumberKeys() >=1)
        {
            System.out.println("Eligible");
            this.numberKeys --;
            DoorNextOpened d =new DoorNextOpened(nextPosition, false, false);
            game.grid(inLevel).set(nextPosition, d);
            nextObject.setModified(true);
        }
        //openDoor = true;
    }

    public void tryBombDeposit(){
        if(bombBagCapacity>0){
            Bomb bomb = new Bomb(this.getPosition(), bombRange);
            game.grid(inLevel).set(this.getPosition(), bomb);
        }

    }

    @Override
    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid(inLevel).get(nextPos);

        if (next instanceof Bonus bonus) {
            bonus.takenBy(this);
        }
        else if(next instanceof Monster)
        {
            if(!(getTimerInvincibilityTime().isRunning())){
                this.setLives(getLives()-1);
                getTimerInvincibilityTime().setRemaining(getInvincibilityTime());
                getTimerInvincibilityTime().start();
            }
            else{
                System.out.println("JE SUIS INVINCIBLE!!!");
            }

        }

        if( next instanceof DoorNextOpened) {
            inLevel++;
        }

        if( next instanceof DoorPrevOpened) {
            inLevel--;
        }
        setPosition(nextPos);
    }
    @Override
    public String toString() {
        return " PLAYER | Position X: " + this.getPosition().getX() + " | Position Y: "+this.getPosition().getY()+" | Vie: "+this.getLives();
    }


    @Override
    public void explode() {
        // TODO
    }

    public int getBombBagCapacity()  {
        return bombBagCapacity;
    }

    public void setBombBagCapacity(int bombBagCapacity)  {
        this.bombBagCapacity =bombBagCapacity ;
    }

    public int getBombRange() {
        return bombRange;
    }

    public void setBombRange(int bombRange)  {
        this.bombRange =bombRange ;
    }
}