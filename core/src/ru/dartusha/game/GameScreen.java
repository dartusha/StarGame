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
    private EnemyShip enemyShip;

    private BulletPool bulletPool;
    private EnemyPool  enemyPool;
    Music gameMusic;
    Sound gunSound;

    private float enemyInterval = 10f;
    private float enemyTimer;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background3.jpg");
        background = new Background(new TextureRegion(bg));
        background.setScale(1.4f);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        starList = new Star[64];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        mainShip = new MainShip(atlas, bulletPool);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameMusic.mp3"));
        gameMusic.play();
        gunSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSoundShort.mp3"));
        enemyPool = new EnemyPool();
        enemyPool.setPoolProperties(atlas, bulletPool);
        newEnemy();
    }

    public void newEnemy(){
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.set(this,  new Vector2(0, 0.4f), new Vector2(0,-0.1f), 0.15f, worldBounds, 1);
        enemyTimer=0;
    }

    public void redrawEnemy(float delta){
        enemyTimer += delta;
        if (enemyTimer >= enemyInterval) {
            newEnemy();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyedSprites();
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
           //  star.scale=Rnd.nextFloat(0.5f,2.5f);
        }
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        redrawEnemy(delta);
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
        mainShip.draw(batch);
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
        gunSound.dispose();
        enemyPool.dispose();
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
            gunSound.play();
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
