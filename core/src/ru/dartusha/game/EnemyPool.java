package ru.dartusha.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class EnemyPool extends SpritesPool<EnemyShip> {
    TextureAtlas atlas;
    BulletPool bulletPool;

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(atlas,bulletPool);
    }

    public void setPoolProperties(TextureAtlas atlas, BulletPool bulletPool){
        this.atlas=atlas;
        this.bulletPool=bulletPool;
    }
}
