package ru.dartusha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class MenuScreen extends BaseScreen {
    private static final String caption = "STAR GAME";

    private Texture bg;
    private Background background;
   // public  MainShip mainShip;
    private TextureAtlas atlas, menuAtlas;
    private Star starList[];
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private Game game;
  //  private BulletPool bulletPool;
    Music startMusic;
    private Font font;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        menuAtlas=new TextureAtlas("textures/menuAtlas.tpack");

        atlas=new TextureAtlas("textures/mainAtlas.tpack");
      //  bulletPool = new BulletPool();
      //  mainShip = new MainShip (atlas, bulletPool);
        starList = new Star[256];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        buttonExit = new ButtonExit(menuAtlas);
        buttonPlay = new ButtonPlay(menuAtlas, game);
        startMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/startMusic.mp3"));
        startMusic.play();
        font = new Font("font/caption3.fnt", "font/caption3.png");
        font.setFontSize(0.06f);
}

    private void printInfo() {
        font.draw(batch, caption, worldBounds.getHalfWidth()+worldBounds.getLeft(), worldBounds.getTop()-0.4f,Align.center);
    }
    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
      //  mainShip.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);

    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
       // mainShip.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        printInfo();
        batch.end();
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
      //  mainShip.update(delta);
        for (Star star : starList) {
            star.update(delta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        startMusic.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        //ship.touchDown(touch, pointer);

        buttonExit.touchDown(touch, pointer);
        buttonPlay.touchDown(touch, pointer);//,mainShip);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch, pointer);
        buttonPlay.touchUp(touch, pointer);
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
       // mainShip.keyMove(keycode);
        return false;
    }

}
