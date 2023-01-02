/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.DoorNextOpened;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Player player;

    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;

    private Scene scenes[] ;
    private List<StatusBar> listStatusBar = new LinkedList<>();
    private Pane[] layer;
    private Input input;
    private int currentLevel = 1;

    private final List<Bomb> listBomb = new LinkedList<>();


    public GameEngine(Game game, final Stage stage) {
        this.stage = stage;
        this.game = game;
        this.player = game.player();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Map<Integer, List<Monster>> monsterMap = new HashMap<>();
        layer = new Pane[game.getNbLevels()];
        scenes = new Scene[game.getNbLevels()];
        for (int i = 1; i <= game.getNbLevels(); i++) {
            layer[i-1] = new Pane();
            Group root = new Group();

            int height = game.grid(i).height();
            int width = game.grid(i).width();
            int sceneWidth = width * ImageResource.size;
            int sceneHeight = height * ImageResource.size;


            scenes[i-1] = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
            scenes[i-1].getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

            if (i==1) {
                stage.setScene(scenes[0]);
                stage.setResizable(false);
                stage.sizeToScene();
                stage.hide();
                stage.show();
                input = new Input(scenes[0]);

            }

            root.getChildren().add(layer[i-1]);
            listStatusBar.add(new StatusBar(root, sceneWidth, sceneHeight, game, i));

            // Create sprites
            for (var decor : game.grid(i).values()) {
                if (!(decor instanceof Monster)) {
                    decor.setLevelObj(i);
                    sprites.add(SpriteFactory.create(layer[i-1], decor));
                    decor.setModified(true);
                }
            }

            for (Monster monster : ((Level)this.game.grid(i)).getMonsters()) {
                monster.setInLevel(i);
                sprites.add(new SpriteMonster(layer[i-1], monster));
            }
            monsterMap.put(i, ((Level)this.game.grid(i)).getMonsters());


        }
        sprites.add(new SpritePlayer(layer[0], player));
        player.setMonsters(monsterMap);
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                checkBomb(now);
                update(now);

                // Graphic update
                cleanupSprites();
                addSprite();
                render();


                listStatusBar.forEach(statusBar -> {
                    statusBar.update(game);
                });


            }
        };
    }

    private void doExplosions(Bomb bomb) {

        Direction tabDirection[] = Direction.values();
        Direction direction;
        Position explosionPosition;
        Position referencePosition;
        GameObject objExplosion;

        referencePosition = bomb.getPosition();
        explosionPosition =bomb.getPosition();
        if(currentLevel == bomb.getLevelObj()){
            animateExplosion(referencePosition, explosionPosition);
        }

        objExplosion= game.grid(bomb.getLevelObj()).get(explosionPosition);

        if(objExplosion != null){
            objExplosion.explode();
        }
        if(player.getPosition().equals(explosionPosition)){
            player.explode();
        }

        for(Monster monster : ((Level)this.game.grid(bomb.getLevelObj())).getMonsters()){
            if(monster.getPosition().equals(explosionPosition)){
                monster.explode();
            }
        }

        for(int i=0; i<tabDirection.length; i++){
            direction = tabDirection[i];
            referencePosition = bomb.getPosition();

            for(int y=0; y< player.getBombRange(); y++){
                explosionPosition = direction.nextPosition(referencePosition);
                objExplosion= game.grid(bomb.getLevelObj()).get(explosionPosition);
                referencePosition = explosionPosition;

                if(currentLevel == bomb.getLevelObj()){
                    animateExplosion(referencePosition, explosionPosition);
                }

                if(objExplosion != null){
                    if(!objExplosion.explode()){
                        break;
                    }

                }

                if(player.getPosition().equals(explosionPosition)){
                    player.explode();
                }

                for(Monster monster : ((Level)this.game.grid(bomb.getLevelObj())).getMonsters()){
                    if(monster.getPosition().equals(explosionPosition)){
                        monster.explode();
                    }
                }
            }

        }
    }


    private void animateExplosion(Position src, Position dst) {
        ImageView explosion = new ImageView(ImageResource.EXPLOSION.getImage());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), explosion);
        tt.setFromX(src.x() * Sprite.size);
        tt.setFromY(src.y() * Sprite.size);
        tt.setToX(dst.x() * Sprite.size);
        tt.setToY(dst.y() * Sprite.size);
        tt.setOnFinished(e -> {
            layer[currentLevel-1].getChildren().remove(explosion);
        });
        layer[currentLevel-1].getChildren().add(explosion);
        tt.play();
    }

    private void createNewBombs(long now) {
        if(player.getBombBagCapacity()>0){
            Bomb bomb = new Bomb(player.getPosition(), currentLevel);
            game.grid(currentLevel).set(player.getPosition(), bomb);
            sprites.add(new SpriteBomb(layer[currentLevel-1], bomb));
            player.setBombBagCapacity(player.getBombBagCapacity()-1);
            listBomb.add(bomb);

        }
    }
    private void checkBomb(long now) {
        List<Bomb> listBombRemove = new LinkedList<>();
        for(Bomb bomb : listBomb){
            bomb.getTimerBomb().update(now);
            if(bomb.getCurrentEvolution() > bomb.getNbEvolution()){
                doExplosions(bomb);
                listBombRemove.add(bomb);
                bomb.remove();
                player.setBombBagCapacity(player.getBombBagCapacity()+1);
            }
            else if(!(bomb.getTimerBomb().isRunning())){
                bomb.setCurrentEvolution(bomb.getCurrentEvolution()+1);
                bomb.getTimerBomb().start();
                bomb.setModified(true);
            }
        }
        listBomb.removeAll(listBombRemove);
    }




    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isKey()) {
            player.requestOpen();
        }
        else if (input.isBomb()) {
            createNewBombs(now);
        }

        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void update(long now) {
        Direction direction;

        for(int i =1 ; i<=this.game.getNbLevels(); i++ )
        {
            for(Monster monster : ((Level)this.game.grid(i)).getMonsters()){
                monster.getTimerMoveMonster().update(now);
                if(!monster.getTimerMoveMonster().isRunning()) {
                    direction = Direction.random();
                    monster.requestMove(direction);

                    monster.setTimerMoveMonster(new Timer( ((monster.getMonsterVelocity())*200)/monster.getInLevel()));
                    monster.getTimerMoveMonster().start();
                }

                if(monster.getLives() <=0){
                    monster.remove();
                }
                monster.update(now);


                if(monster.getInLevel() == currentLevel && monster.getPosition().equals(player.getPosition()))
                {
                    player.getTimerInvincibilityTime().update(now);
                    if(!(player.getTimerInvincibilityTime().isRunning())){
                        player.setLives(player.getLives()-1);
                        player.getTimerInvincibilityTime().start();
                    }
                }
                else if(monster.getInLevel() == currentLevel &&
                        monster.getPosition().equals(player.getSaveLastPosition()) &&
                        player.getPosition().equals(monster.getSaveLastPosition()))
                {
                    player.getTimerInvincibilityTime().update(now);
                    if(!(player.getTimerInvincibilityTime().isRunning())){
                        player.setLives(player.getLives()-1);
                        player.getTimerInvincibilityTime().start();
                    }
                }


            }
        }

        if( player.getInLevel()!= currentLevel) {
            boolean next = currentLevel < player.getInLevel();
            Position newPlayerPosition;

            currentLevel = player.getInLevel();
            stage.setScene(scenes[currentLevel-1]);
            input = new Input(scenes[currentLevel-1]);
            stage.sizeToScene();


            if (next)
                newPlayerPosition = new Position(((Level)game.grid(currentLevel)).getFromPreviousLevel());
            else
                newPlayerPosition = new Position(((Level)game.grid(currentLevel)).getFromNextLevel());


            player.setPosition(newPlayerPosition);
            for(Sprite sprite : sprites){
                if(sprite instanceof SpritePlayer){
                    cleanUpSprites.add(sprite);
                }
            }
            sprites.add(new SpritePlayer(layer[currentLevel-1], player));

        }

        player.update(now);
        if (player.getLives() <= 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        else if(player.getPrincessFound() == true){
            gameLoop.stop();
            showMessage("Gagné!", Color.GREEN);
        }

    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                try{
                    game.grid(sprite.getGameObject().getLevelObj()).remove(sprite.getPosition());
                }
                catch(Exception e){

                }

                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void addSprite(){
        for(int i =1 ; i<=this.game.getNbLevels(); i++ )
        {
            for (var decor : game.grid(i).values()) {
                if(decor instanceof DoorNextOpened doorNextOpened)
                {
                    if(!(doorNextOpened.getIsAddToSprite()))
                    {
                        sprites.add(SpriteFactory.create(layer[i-1], decor));
                        decor.setModified(true);
                        doorNextOpened.setIsAddToSprite(true);
                    }
                }
                if(decor instanceof Box box)
                    if(!(box.getAddToSprite()))
                    {
                        sprites.add(SpriteFactory.create(layer[i-1], decor));
                        decor.setModified(true);
                        box.setAddToSprite(true);
                    }
            }
        }

    }
    public void start() {
        gameLoop.start();
    }
}