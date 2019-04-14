package ru.dartusha.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen extends BaseScreen {

    private Texture bg, shipTexture;
    private Background background;
    private Ship ship;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background3.jpg");
        background = new Background(new TextureRegion(bg));
        shipTexture=new Texture("ship3.png");
        ship = new Ship (new TextureRegion(shipTexture));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        ship.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        ship.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        shipTexture.dispose();
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
