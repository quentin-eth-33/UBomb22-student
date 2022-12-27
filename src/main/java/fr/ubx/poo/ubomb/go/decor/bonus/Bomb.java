package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Position;

public class Bomb extends Bonus{

    private int range;

    private final int nbEvolution = 3;

    private final int durationEvalution = 1000;

    private int currentEvolution;

    private int level;
    private Timer timerBomb;
    public Bomb(Position position, int range, int level) {
        super(position);
        this.range =range;
        this.level =level;
        this.currentEvolution = 0;
        timerBomb = new Timer(durationEvalution);
        timerBomb.start();
    }

    public Timer getTimerBomb(){
        return timerBomb;
    }

    public void setTimerBomb(Timer timerBomb){
        this.timerBomb =  timerBomb;
    }


    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level =  level;
    }

    public int getNbEvolution(){
        return nbEvolution;
    }

    public int getDurationEvalution(){
        return durationEvalution;
    }

    public int getCurrentEvolution(){
        return currentEvolution;
    }

    public void setCurrentEvolution(int currentEvolution){
        this.currentEvolution = currentEvolution;
    }

}
