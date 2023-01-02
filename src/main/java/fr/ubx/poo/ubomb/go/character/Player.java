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
import javafx.geometry.Pos;

import java.util.List;
import java.util.Map;

public class Player extends Character implements Movable, TakeVisitor {


    // Ajout
    private int numberKeys =0;

    private int bombBagCapacity;
    private int bombRange = 1;

    private boolean princessFound = false;

    private Map<Integer, List<Monster>> monsters;



    public Player(Game game, Position position) {
        super(game, position);
        this.setDirection(Direction.DOWN);
        this.setLives(game.configuration().playerLives());
        this.bombBagCapacity = game.getConfiguration().bombBagCapacity();
        this.setInvincibilityTime(game.getConfiguration().playerInvisibilityTime());
        this.setTimerInvincibilityTime(new Timer(getInvincibilityTime()));
    }


    @Override
    public void take(Key key) {
        numberKeys++;
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
            this.numberKeys --;
            DoorNextOpened d =new DoorNextOpened(nextPosition, false, false);
            game.grid(inLevel).set(nextPosition, d);
            nextObject.setModified(true);
        }
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid(inLevel).get(nextPos);

        if (next != null){
            if(next instanceof Box) {
                Position nextNextPos = direction.nextPosition(nextPos);
                if (boxCanMove(nextNextPos))
                    return true;
            }
            return next.getIsAccessible();
        }

        else if(nextPos.x() < 0 ||nextPos.y() < 0 || nextPos.x() >= game.grid(inLevel).width() || nextPos.y() >= game.grid(inLevel).height()){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid(inLevel).get(nextPos);

        if (next instanceof Bonus bonus) {
            bonus.takenBy(this);
        }

        else if(next instanceof Box) {
            Position nextNextPos = direction.nextPosition(nextPos);
            next.remove();
            Box box = new Box(new Position(nextNextPos),false);
            box.setLevelObj(inLevel);
            game.grid(inLevel).set(nextNextPos, box);
        }

        else if( next instanceof DoorNextOpened) {
            inLevel++;
        }

        else if( next instanceof DoorPrevOpened) {
            inLevel--;
        }

        setSaveLastPosition(getPosition());
        setPosition(nextPos);
    }
    public boolean boxCanMove(Position nextPos) {
        GameObject next = game.grid(inLevel).get(nextPos);
        boolean isMonster = false;
        List<Monster> listMonster = this.monsters.get(inLevel);

        for(Monster monster : listMonster){
            if(monster.getPosition().equals(nextPos)){
                isMonster = true;
                break;
            }
        }



        if(nextPos.x() < 0 ||nextPos.y() < 0 || nextPos.x() >= game.grid(inLevel).width() || nextPos.y() >= game.grid(inLevel).height()){
            return false;
        }
        else if (next == null && isMonster == false){

            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public String toString() {
        return " PLAYER | Position X: " + this.getPosition().x() + " | Position Y: "+this.getPosition().y()+" | Vie: "+this.getLives();
    }

    @Override
    public boolean explode() {
        setLives(getLives()-1);
        return true;
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


    public void setMonsters(Map<Integer, List<Monster>> monsters){
        this.monsters = monsters;
    }

}