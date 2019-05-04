package ru.dartusha.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BonusGenerator {

    private static final float BONUS_SMALL_HEIGHT = 0.04f;
    private static final int BONUS_SMALL_HP = 10;

    private static final float BONUS_MEDIUM_HEIGHT = 0.05f;
    private static final int BONUS_MEDIUM_HP = 5;

    private static final float BONUS_BIG_HEIGHT = 0.08f;
    private static final int BONUS_BIG_HP = 1;

    private Rect worldBounds;

    private float generateInterval = 4f;
    private float generateTimer;

    private final TextureRegion[] bonusSmallRegion;
    private final TextureRegion[] bonusMediumRegion;
    private final TextureRegion[] bonusBigRegion;

    private final Vector2 bonusSmallV = new Vector2(0, -0.4f);
    private final Vector2 bonusMediumV = new Vector2(0, -0.3f);
    private final Vector2 bonusBigV = new Vector2(0, -0.2f);

    private final BonusPool bonusPool;

    public BonusGenerator(TextureAtlas atlas, BonusPool bonusPool, Rect worldBounds) {
        TextureRegion heal0 = atlas.findRegion("heal2");
        this.bonusSmallRegion = Regions.split(heal0, 1, 1, 1);
        TextureRegion heal1 = atlas.findRegion("heal2");
        this.bonusMediumRegion = Regions.split(heal1, 1, 1, 1);
        TextureRegion heal2 = atlas.findRegion("heal2");
        this.bonusBigRegion = Regions.split(heal2, 1, 1, 1);
        this.bonusPool = bonusPool;
        this.worldBounds = worldBounds;
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            Bonus bonus = bonusPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                bonus.set(
                        bonusSmallRegion,
                        bonusSmallV,
                        BONUS_SMALL_HEIGHT,
                        BONUS_SMALL_HP
                );
            } else if (type < 0.85f) {
                bonus.set(
                        bonusMediumRegion,
                        bonusMediumV,
                        BONUS_MEDIUM_HEIGHT,
                        BONUS_MEDIUM_HP
                );
            } else {
                bonus.set(
                        bonusBigRegion,
                        bonusBigV,
                        BONUS_BIG_HEIGHT,
                        BONUS_BIG_HP
                );
            }
            bonus.pos.x = Rnd.nextFloat(worldBounds.getLeft() + bonus.getHalfWidth(),
                    worldBounds.getRight() - bonus.getHalfWidth());
            bonus.setBottom(worldBounds.getTop());
        }
    }

}
