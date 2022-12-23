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

import java.util.ArrayList;
import java.util.List;

public class Player extends Character implements Movable, TakeVisitor {


    // Ajout
    private int numberKeys =0;

    private int bombBagCapacity;

    private List<Bomb> bombs = new ArrayList<Bomb>();
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
    }
    @Override
    public void take(BombNumberDec db) {
        this.bombBagCapacity --;
    }

    @Override
    public void take(AddLife al) {
        setLives(this.getLives()+1);
    }

    @Override
    public void take(Bomb bomb) {
        if (bombs.size() < bombBagCapacity)
            bombs.add(bomb);
    }

    @Override
    public void take(BombRangeInc ib) {
        Bomb.setRange(Bomb.getRange()+1);
    }
    @Override
    public void take(BombRangeDec db) {
        int bombRange = Bomb.getRange();
        if (bombRange>1)
            Bomb.setRange(bombRange-1);
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

    public int getAvailableBombs()  {
        return bombs.size();
    }
}
