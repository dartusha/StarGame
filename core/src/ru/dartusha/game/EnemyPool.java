package ru.dartusha.game;

import com.badlogic.gdx.audio.Sound;

public class EnemyPool extends SpritesPool<Enemy> {

    private Rect worldBounds;
    private BulletPool bulletPool;
    private Sound shootSound;
    private MainShip mainShip;

    public EnemyPool(BulletPool bulletPool, Sound shootSound, Rect worldBounds, MainShip mainShip) {
        this.bulletPool = bulletPool;
        this.shootSound = shootSound;
        this.worldBounds = worldBounds;
        this.mainShip = mainShip;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, shootSound, worldBounds, mainShip);
    }
}
