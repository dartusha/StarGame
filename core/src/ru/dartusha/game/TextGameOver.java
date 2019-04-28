package ru.dartusha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextGameOver extends ScaledTouchUpButton {

    private Game game;

    public TextGameOver(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("message_game_over"));
        this.game = game;
        setHeightProportion(0.07f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getTop() - 0.2f);
        setLeft(worldBounds.getLeft() + 0.02f);
    }

    @Override
    protected void action() {
        Gdx.app.exit();
    }

}
