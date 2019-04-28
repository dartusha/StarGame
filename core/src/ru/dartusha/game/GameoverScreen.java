package ru.dartusha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GameoverScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star starList[];
    private ButtonNewGame buttonNewGame;
    private Game game;
    Music gameOverMusic;
    private TextGameOver textGameOver;

    public GameoverScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("gameoverBG2.jpg");
        background = new Background(new TextureRegion(bg));
        atlas=new TextureAtlas("textures/mainAtlas.tpack");
        atlas.findRegion("message_game_over");
        textGameOver=new TextGameOver(atlas,game);
        buttonNewGame = new ButtonNewGame(atlas, game);

        starList = new Star[64];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameOverMusic.mp3"));
        gameOverMusic.play();
        gameOverMusic.setLooping(true);
}

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        textGameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        textGameOver.draw(batch);
        buttonNewGame.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        batch.end();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        gameOverMusic.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonNewGame.touchDown(touch, pointer);
        textGameOver.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonNewGame.touchUp(touch, pointer);
        textGameOver.touchUp(touch, pointer);
        return false;
    }

}
