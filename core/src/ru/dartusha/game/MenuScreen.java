package ru.dartusha.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen extends BaseScreen {

    private Texture bg, shipTexture;
    private Background background;
    private Ship ship;
    private TextureAtlas atlas;
    private Star star;
    private Star starList[];

    @Override
    public void show() {
        super.show();
        //bg = new Texture("background3.jpg");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
       // shipTexture=new Texture("ship3.png");
       // ship = new Ship (new TextureRegion(shipTexture));
        atlas=new TextureAtlas("textures/mainAtlas.tpack");
        ship = new Ship (atlas);
        starList = new Star[256];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        ship.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }

    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        ship.draw(batch);
        batch.end();
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        ship.update(delta);
        for (Star star : starList) {
            star.update(delta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        ship.touchDown(touch, pointer); return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        ship.keyMove(keycode);
        return false;
    }

}
