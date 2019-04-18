package ru.dartusha.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends BaseScreen {
    private Texture bg, shipTexture;
    private Background background;
    public Ship ship;
    private TextureAtlas atlas, menuAtlas;
    private Star starList[];

    @Override
    public void show() {
        super.show();
        //bg = new Texture("background3.jpg");
        bg = new Texture("background3.jpg");
        background = new Background(new TextureRegion(bg));
        atlas=new TextureAtlas("textures/mainAtlas.tpack");
        ship = new Ship (atlas,"main_ship");
        starList = new Star[100];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        ship.pos.y = worldBounds.getBottom()+0.1f;

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
            star.scale=Rnd.nextFloat(0.5f,2.5f);
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
        ship.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        ship.keyMove(keycode);
        return false;
    }
}
