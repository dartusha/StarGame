package ru.dartusha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class MenuScreen extends BaseScreen {

    private Vector2 touch;
    private Vector2 pos;
    private Vector2 v;
    private Texture img;

    private Vector2 test;

    @Override
    public void show() {
        super.show();
        touch = new Vector2();
        pos = new Vector2();
        v = new Vector2(0f,2f);
        img = new Texture("ship3.png");
    }

    /*
    2.Реализовать движение логотипа badlogic (можно свою картинку вставить)
    при нажатии клавиши мыши (touchDown) в точку нажатия на экране и остановку в данной точке.
     */
    @Override
    public void render(float delta) {
            super.render(delta);
        pos.add(v);
        batch.begin();
        batch.draw(img, pos.x, pos.y);
        batch.end();
        if ((Math.abs(pos.y-touch.y)<5)&&(Math.abs(pos.x -touch.x)<5)){
          // System.out.println("stop");
            v.setZero();
       }
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
       // System.out.println("touchDown touch.x = " + touch.x + " touch.y = " + touch.y);
       // System.out.println("touchDown pos.x = " + pos.x + " pos.y = " + pos.y);

        test=touch.cpy().sub(pos).nor();
       // System.out.println(test.x+","+test.y);
        v.set(test.x,test.y);
        return false;
    }

    @Override
    /*
    3.Реализовать управление логотипом с помощью клавиш со стрелками на клавиатуре*** (дополнительное задание, не обязательное к выполнению)
     */
    public boolean keyUp(int keycode) {
        if (keycode==20){
            pos.y=pos.y-50;
        }
        if (keycode==19){
            pos.y=pos.y+50;
        }
        if (keycode==22){
            pos.x=pos.x+50;
        }
        if (keycode==21){
            pos.x=pos.x-50;
        }
        return false;
    }


}
