package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;
import fr.ubx.poo.ubomb.go.decor.bonus.Princess;

public class Monster extends Character {

    private Timer timerMoveMonster;

    private int monsterVelocity;
    public Monster(Game game, Position position) {
        super(game, position);
        this.setDirection(Direction.DOWN);
        this.monsterVelocity = game.getConfiguration().monsterVelocity();
        this.setInvincibilityTime(game.getConfiguration().monsterInvisibilityTime());
        this.timerMoveMonster = new Timer(monsterVelocity*200);
        this.timerMoveMonster.start();
        this.setTimerInvincibilityTime(new Timer(getInvincibilityTime()));
    }

    public Monster(Position position) {
        super(position);
        this.setDirection(Direction.DOWN);
        this.setLives(1);
        monsterVelocity =5;
        setInvincibilityTime(1000);
        timerMoveMonster = new Timer(monsterVelocity*200);
        timerMoveMonster.start();
        setTimerInvincibilityTime(new Timer(getInvincibilityTime()));
    }

    public Timer getTimerMoveMonster(){
        return timerMoveMonster;
    }

    public void setTimerMoveMonster(Timer timerMonster){
        this.timerMoveMonster =  timerMonster;
    }

    public void setMonsterVelocity(int velocity){
        this.monsterVelocity = velocity;
    }

    public int getMonsterVelocity(){
        return this.monsterVelocity;
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid(inLevel).get(nextPos);

        if (next != null){
            return next.getCanMoveMonster();
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
        setSaveLastPosition(getPosition());
        setPosition(nextPos);
    }

    @Override
    public boolean explode(){
        if(!(getTimerInvincibilityTime().isRunning())){
            setLives(getLives()-1);
            getTimerInvincibilityTime().start();
        }
        return true;
    }

    @Override
    public String toString() {
        return " MONSTER "+this.hashCode()+ " | Position X: " + this.getPosition().x() + " | Position Y: "+this.getPosition().y()+" | Vie: "+this.getLives()+" | Level: "+this.getInLevel();
    }
}