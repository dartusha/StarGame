package ru.dartusha.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
/*
ДЗ 4
Реализовать звёзды на игровом экране
Реализовать спрайт корабля
*Cделать управление кораблём
 */
public class Ship extends Sprite {

    private Vector2 v;
    private Rect worldBounds;

    private Vector2 test, tmp;
    private float rotation=0;
    float angle=0;
    float touchX=0;
    boolean flag=false;


    public Ship(TextureAtlas atlas,
                String shipName) {
        super(atlas.findRegion(shipName));
        v = new Vector2(0f,0f);
        test = new Vector2(0, 0);
        tmp = new Vector2(0, 0);
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
        v.set(0f,0.001f);
        tmp.set(touch.x,pos.y);
        test=tmp.cpy().sub(pos).nor();
       // v.set(test.x/1000,test.y/1000);
        v.set(test.x/1000,0);
        touchX=touch.x;
        return false;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.add(v);
        if ((Math.abs(pos.x-touchX)<=0.001)&&!flag) {
            v.set(0,0);
            pos.x=touchX;
        }
    }

    public void goUp(){
        v.set(0f,0.068f);
        flag=true;
    }


    public boolean keyMove(int keycode) {
        if (keycode==22){
            pos.x=pos.x+0.02f;
        }
        if (keycode==21){
            pos.x=pos.x-0.02f;
        }
        return false;
    }


}
