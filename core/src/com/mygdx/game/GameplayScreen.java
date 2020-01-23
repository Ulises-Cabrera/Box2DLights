package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameplayScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private Lights lights;

    GameplayScreen() {

    }

    @Override
    public void show() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
        camera.zoom = 2;
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        lights = new Lights(camera);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        batch.dispose();
        lights.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0.201f, 0.253f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lights.update(batch);
        lights.render();
    }
}