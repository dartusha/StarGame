package ru.dartusha.game;

import com.badlogic.gdx.audio.Sound;

public class BonusPool extends SpritesPool<Bonus> {

    private Rect worldBounds;
    private Sound bonusSound;

    public BonusPool(Sound bonusSound, Rect worldBounds) {
        this.bonusSound = bonusSound;
        this.worldBounds = worldBounds;
    }

    @Override
    protected Bonus newObject() {
        return new Bonus(bonusSound, worldBounds);
    }
}
