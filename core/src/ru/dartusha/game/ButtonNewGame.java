package ru.dartusha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class ButtonNewGame extends ScaledTouchUpButton {

    private Game game;

    public ButtonNewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
        setHeightProportion(0.05f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getTop() - 0.5f);
        setLeft(worldBounds.getLeft() + 0.2f);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }
}
