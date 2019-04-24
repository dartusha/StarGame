package ru.dartusha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star starList[];

    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyPool  enemyPool;
    Music gameMusic;

    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private EnemyGenerator enemyGenerator;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background3.jpg");
        background = new Background(new TextureRegion(bg));
        background.setScale(1.4f);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSoundShort.mp3"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSoundEnemy.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosionMiddle.mp3"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        starList = new Star[64];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        mainShip = new MainShip(atlas, bulletPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, bulletSound, worldBounds, mainShip);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameMusic.mp3"));
        gameMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedSprites();
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyGenerator.generate(delta);
    }

    private void checkCollisions() {
        for (Enemy el:enemyPool.getActiveObjects()) {
            if (!(mainShip.isOutside(el))){
                System.out.println("Вы поймали вражеский кораблик");
                el.hp--;
                if (el.hp==0){
                    el.destroy();
                    explosionSound.play();
                }
            }
        }

        for (Bullet el:bulletPool.getActiveObjects()) {
            if (!(mainShip.isOutside(el))&&(!el.getOwner().equals(mainShip))){
                System.out.println("Вы словили пульку");
                el.destroy();
                mainShip.hp--;
                if (mainShip.hp==0){
                    mainShip.destroy();
                    explosionSound.play();
                }
            }
            for (Bullet elBl:bulletPool.getActiveObjects()) {
                if ((!el.getOwner().equals(elBl.getOwner()))&&(!el.isOutside(elBl))){
                    el.destroy();
                    elBl.destroy();
                    System.out.println("Вы уничтожили чужую пульку");
                }
            }
            for (Enemy enEl:enemyPool.getActiveObjects()) {
                if (!(enEl.isOutside(el))&&(el.getOwner().equals(mainShip))){
                    System.out.println("Ваша пуля влетела во вражеский кораблик");
                    el.destroy();
                    enEl.hp--;
                    if (enEl.hp==0){
                        enEl.destroy();
                        explosionSound.play();
                    }
                }
            }

        }

    }

    private void freeAllDestroyedSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }
    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        gameMusic.dispose();
        enemyPool.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        if (keycode==62){
            laserSound.play();
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return false;
    }
}