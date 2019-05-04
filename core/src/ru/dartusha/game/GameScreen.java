package ru.dartusha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class GameScreen extends BaseScreen {

    private static final String FRAGS = "SCORE: ";
    private static final String HP = "HP";
    private static final String LEVEL = "LEVEL: ";
    private static final String LIFES = "♥";
    private static final String BONUS = "+";

    private enum State {PLAYING, PAUSE, GAME_OVER}

    private State state;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas,addonAtlas;

    private TrackingStar starList[];

    private MainShip mainShip;
    private int hpSmall=0;

    private BulletPool bulletPool;
    private EnemyPool  enemyPool;
    private BonusPool  bonusPool;
    Music gameMusic;
    //Music gameOverMusic;
    private ExplosionPool explosionPool;

    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private Sound bonusSound;

    private EnemyGenerator enemyGenerator;
    private BonusGenerator bonusGenerator;
    private Game game;

    private Font font, addonFont, bonusFont;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHp = new StringBuilder();
    private StringBuilder sbLevel = new StringBuilder();
    private StringBuilder sbBonus = new StringBuilder();

    private int currentBonus=0;
    private float currentBonusX=0, currentBonusY=0, bonusInterval=0, bonusTimer=0;

    private int frags = 0;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("background3.jpg");
        background = new Background(new TextureRegion(bg));
        background.setScale(1.4f);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSoundShort.mp3"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSoundEnemy.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosionMiddle.mp3"));
        bonusSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bonus.mp3"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        addonAtlas = new TextureAtlas("textures/addonAtlas.atlas");

        starList = new TrackingStar[64];
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);

        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new TrackingStar(atlas, mainShip.getV());
        }
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds, mainShip);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);
        bonusPool=new BonusPool(explosionSound,worldBounds);
        bonusGenerator = new BonusGenerator(addonAtlas, bonusPool, worldBounds);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameMusic.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();
        font = new Font("font/caption3.fnt", "font/caption3.png");
        font.setFontSize(0.03f);
        addonFont = new Font("font/addons.fnt", "font/addons.png");
        addonFont.setFontSize(0.03f);
        bonusFont = new Font("font/bonus.fnt", "font/bonus.png");
        bonusFont.setFontSize(0.04f);
        state = State.PLAYING;
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
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            enemyGenerator.generate(delta, frags);
            bonusGenerator.generate(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            bonusPool.updateActiveSprites(delta);
            if (bonusInterval>0) bonusTimer+=delta;
        }
    }

    public void gameOver() {
        state = State.GAME_OVER;
        gameMusic.stop();
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst(mainShip.pos) < minDist) {
                mainShip.damage(enemy.getHp());
                enemy.destroy();
                if (mainShip.isDestroyed()) {
                    mainShip.destroy();
                    gameOver();
                }
                return;
            }
        }
        List<Bonus> bonusList = bonusPool.getActiveObjects();
        for (Bonus bonus:bonusList){
            float minDist = bonus.getHalfWidth() + mainShip.getHalfWidth();
            if (bonus.pos.dst(mainShip.pos) < minDist) {
                mainShip.heal(bonus.getHp());
                addBonusPoints(bonus);
                bonus.destroy();
            }
        }


        List<Bullet> bulletList = bulletPool.getActiveObjects();

        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed()) {
                continue;
            }
            for (Bullet bulletOther:bulletPool.getActiveObjects()) {
                if ((!bullet.getOwner().equals(bulletOther.getOwner()))&&(!bullet.isOutside(bulletOther))){
                    bullet.destroy();
                    bulletOther.destroy();
                }
            }

            if (bullet.getOwner() == mainShip) {
                for (Enemy enemy : enemyList) {
                    if (enemy.isDestroyed()) {
                        continue;
                    }
                    if (enemy.isBulletCollision(bullet)) {
                        enemy.damage(bullet.getDamage());
                        bullet.destroy();
                        if (enemy.isDestroyed()) {
                            frags++;
                        }
                        return;
                    }
                }
            } else {
                if (mainShip.isBulletCollision(bullet)) {
                    bullet.destroy();
                    mainShip.damage(bullet.getDamage());
                    if (mainShip.isDestroyed()) {
                        gameOver();
                    }
                    return;
                }
            }
        }
    }

    private void freeAllDestroyedSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        bonusPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        if (state == State.PLAYING) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
            bonusPool.drawActiveSprites(batch);
        }
        if (state==State.GAME_OVER){
            this.reset();
            game.setScreen(new GameoverScreen(game));
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        printBonus();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        hpSmall=(int)mainShip.getHp()/20;
        if (hpSmall<0) hpSmall=0;
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());

        if (hpSmall>0) {
            addonFont.draw(batch, String.format("%" + hpSmall + "s", "").replace(" ", LIFES), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        }

        font.draw(batch, sbLevel.append(LEVEL).append(enemyGenerator.getStage()), worldBounds.getRight(), worldBounds.getTop(), Align.right);
    }

    private void printBonus() {
        if ((currentBonus>0)&&(bonusTimer<bonusInterval)) {
            sbBonus.setLength(0);
            bonusFont.draw(batch, sbBonus.append(BONUS).append(currentBonus).append(HP), currentBonusX, currentBonusY, Align.right);
        }
            else
        {
            sbBonus.setLength(0);
            bonusFont.draw(batch, sbBonus, currentBonusX, currentBonusY, Align.right);
            bonusInterval=0f;
            bonusTimer=0f;
        }
     }

     public void addBonusPoints(Bonus bonus){
         bonusSound.play();
         currentBonus=bonus.getHp();
         currentBonusX=bonus.pos.x;
         currentBonusY=bonus.pos.y;
         if (currentBonusX<-0.2f) currentBonusX+=0.1f;
         bonusInterval=0.5f;
     }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        if (state == State.PLAYING) {
            mainShip.resize(worldBounds);
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (state == State.PLAYING) {
            state = State.PAUSE;
        }
    }

    @Override
    public void resume() {
        super.resume();
        if (state == State.PAUSE) {
            state = State.PLAYING;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        gameMusic.dispose();
        enemyPool.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        font.dispose();
        addonFont.dispose();
        addonAtlas.dispose();
        bonusPool.dispose();
        bonusSound.dispose();
        bonusFont.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        }
        return false;
    }

    public void reset() {
        state = State.PLAYING;

        mainShip.reset();
        frags = 0;

        enemyGenerator.setStage(1);

        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
        bonusPool.freeAllActiveSprites();
    }
}