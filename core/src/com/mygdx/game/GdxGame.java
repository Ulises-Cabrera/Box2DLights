package com.mygdx.game;

import com.badlogic.gdx.Game;

public class GdxGame extends Game {

    public GdxGame(){
    }

    @Override
    public void create() {
        setScreen(new GameplayScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
    }

}


