package ru.dartusha.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bonus extends Sprite {

    protected Rect worldBounds;
    protected Vector2 v;
    protected Vector2 v0;
    protected int hp;
    private Vector2 descentV;
    private Sound bonusSound;

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    public int getHp() {
        return hp;
    }
    public Vector2 getV() {
        return v;
    }

    public Bonus(Sound bonusSound, Rect worldBounds)//, MainShip mainShip)
    {
        this.worldBounds = worldBounds;
        this.bonusSound = bonusSound;
        this.descentV = new Vector2(0, -0.3f);
        this.v =  new Vector2();
        this.v0 = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(descentV, delta);

        if (getTop() <= worldBounds.getTop()) {
            v.set(v0);
        }

        if (getBottom() < worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        setHeightProportion(height);
        this.hp = hp;
        v.set(descentV);
    }

}
