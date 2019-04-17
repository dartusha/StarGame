package ru.dartusha.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Ship extends Sprite {

    private Vector2 v;
    private Rect worldBounds;

    private Vector2 test, destination, point1, point2;
    private float rotation=0;
    float angle=0;


    public Ship(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        v = new Vector2(0f,0f);
        point1 = new Vector2(0, 0);
        point2 = new Vector2(0, 0);
        test = new Vector2(0, 0);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds=worldBounds;
        super.resize(worldBounds);
        pos.set(worldBounds.pos);
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        point1=pos;

        v.set(0f,0.001f);
        destination=touch;

        point1=point2;
        test=touch.cpy().sub(pos).nor();
        v.set(test.x/1000,test.y/1000);
        return false;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.add(v);
        if (v.y!=0){
            if ((Math.abs(pos.y-destination.y)<0.005)&&(Math.abs(pos.x -destination.x)<0.005)) {
                v.setZero();
            }
        }
    }


    public boolean keyMove(int keycode) {
        angle=0;

        if (keycode==20){
            pos.y=pos.y-0.02f;
        }
        if (keycode==19){
            pos.y=pos.y+0.02f;
        }
        if (keycode==22){
            pos.x=pos.x+0.02f;
        }
        if (keycode==21){
            pos.x=pos.x-0.02f;
        }
        return false;
    }


}
