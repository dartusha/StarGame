package ru.dartusha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Ship extends Sprite {

    private Vector2 v;

    private Vector2 test, destination, point1, point2;
    private float rotation=0;
    float angle=0;


    public Ship(TextureRegion region) {
        super(region);
        v = new Vector2(0f,0f);
        point1 = new Vector2(0, halfHeight);
        point2 = new Vector2(0, 0);

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight()/10);
        pos.set(worldBounds.pos);
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer) {

        v.set(0f,0.001f);
        destination=touch;

        test=touch.cpy().sub(pos).nor();
        v.set(test.x/1000,test.y/1000);

        point1.set(0, touch.len());
        point2.set(touch.x, touch.y);

        angle = point1.angle(point2);

        rotation=0;
        rotation=angle;

        return false;
    }


    public void draw(SpriteBatch batch) {
        pos.add(v);
        if (v.y!=0){
            if ((Math.abs(pos.y-destination.y)<0.005)&&(Math.abs(pos.x -destination.x)<0.005)) {
                v.setZero();
            }
            }
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,rotation
               // angle

        );
    }

    public boolean keyMove(int keycode) {
        angle=0;

        if (keycode==20){
            pos.y=pos.y-0.01f;
            rotation=180;
        }
        if (keycode==19){
            pos.y=pos.y+0.01f;
            rotation=0;
        }
        if (keycode==22){
            pos.x=pos.x+0.01f;
            rotation=-90;
        }
        if (keycode==21){
            pos.x=pos.x-0.01f;
            rotation=90;
        }
        return false;
    }


}
