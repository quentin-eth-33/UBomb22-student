package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public abstract class Character extends GameObject{

    private Direction direction;
    private int lives;

    private Position saveLastPosition;
    private long invincibilityTime;

    private Timer timerInvincibilityTime;
    private boolean moveRequested = false;
    protected int inLevel = 1;


    public Character(Game game, Position position) {
        super(game, position);
        setSaveLastPosition(position);
    }

    public Character(Position position) {
        super(position);
        setSaveLastPosition(position);
    }

    public void requestMove(Direction direction) {
        if (direction != this.getDirection()) {
            this.setDirection(direction);
            setModified(true);
        }
        moveRequested = true;
    }

    public Direction getDirection() {
        return direction;
    }



    public void setTimerInvincibilityTime(Timer timerInvincibilityTime){
        this.timerInvincibilityTime= timerInvincibilityTime;
    }

    public Timer getTimerInvincibilityTime(){
        return this.timerInvincibilityTime;
    }
    public void setInvincibilityTime(long invincibilityTime){
        this.invincibilityTime= invincibilityTime;
    }

    public long getInvincibilityTime(){
        return this.invincibilityTime;
    }
    public boolean getMoveRequested() {
        return this.moveRequested;
    }

    public void setMoveRequested(boolean moveRequested) {
        this.moveRequested = moveRequested;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    public int getLives() {
        return lives;
    }

    public int getInLevel() {
        return inLevel;
    }

    public void setInLevel(int inLevel) {
        this.inLevel = inLevel;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }


    public Position getSaveLastPosition() {
        return saveLastPosition;
    }

    public void setSaveLastPosition(Position saveLastPosition) {
        this.saveLastPosition = saveLastPosition;
    }


    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid(inLevel).get(nextPos);

        if (next != null){
            return next.getIsAccessible();
        }
        else if(nextPos.x() < 0 || nextPos.y() < 0 || nextPos.x() >= game.grid(inLevel).width() || nextPos.y() >= game.grid(inLevel).height()){
            return false;
        }
        else{
            return true;
        }

    }

    public void doMove(Direction direction) {}

    public void update(long now) {
        getTimerInvincibilityTime().update(now);

        if (this.getMoveRequested()) {
            if (canMove(this.getDirection())) {
                doMove(this.getDirection());
            }
        }
        this.setMoveRequested(false);
    }
}