package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

class Lights {

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body player;
    private RayHandler rayHandler;
    private OrthographicCamera camera;
    private float posX = 0, posY = 0;
    private InputControl inputControl;

    Lights(OrthographicCamera camera) {
        this.camera = camera;
        this.world = new World(new Vector2(0, 0), false);
        this.b2dr = new Box2DDebugRenderer();
        inputControl = new InputControl(camera);
        Gdx.input.setInputProcessor(inputControl);
        createPlayer();
        createBoxes();
        createAmbientLighting();
        createPointLight();
        //createConeLight();
    }

    private void createPlayer() {
        this.player = createBox(world, 0, 0, 32, 32,
                false, false);
        this.player.setLinearDamping(20f);
    }

    private void createBoxes() {
        createBox(world, 200, 0, 64, 96,
                true, true);
        createBox(world, -200, 0, 64, 150,
                true, true);
        createBox(world, 0, 200, 150, 64,
                true, true);
        createBox(world, 0, -200, 150, 64,
                true, true);
    }

    private Body createBox(World world, float x, float y, int width, int height, boolean isStatic,
                           boolean fixedRotation) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x / 32, y / 32);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);
        pBody.setUserData("wall");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f / 32, height / 2f / 32);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.filter.categoryBits = 1;
        fd.filter.maskBits = 2 | 1 | 4;
        fd.filter.groupIndex = 0;
        pBody.createFixture(fd);
        shape.dispose();
        return pBody;
    }

    private void createAmbientLighting() {
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5f);
    }

    private void createPointLight() {
        PointLight pointLight = new PointLight(rayHandler, 50, Color.WHITE, 6, 1, 1);
        pointLight.setSoftnessLength(0f);
        pointLight.attachToBody(player);
        pointLight.setXray(false);
    }

    private void createConeLight() {
        ConeLight coneLight = new ConeLight(rayHandler, 120, Color.WHITE, 6, 0, 0,
                player.getAngle(), 30);
        coneLight.setSoftnessLength(0f);
        coneLight.attachToBody(player);
        coneLight.setXray(false);
    }

    void update(SpriteBatch batch) {
        world.step(1 / 60f, 6, 2);
        rayHandler.update();
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(32));
    }

    void render() {
        if (inputControl.posx > 0) {
            posX += 0.15;
            inputControl.posx = 0;
            player.setLinearVelocity(posX, player.getLinearVelocity().y);
        } else if (inputControl.posx < 0) {
            posX -= 0.15;
            inputControl.posx = 0;
            player.setLinearVelocity(posX, player.getLinearVelocity().y);
        }
        if (inputControl.posy < 0) {
            posY += 0.15;
            inputControl.posy = 0;
            player.setLinearVelocity(player.getLinearVelocity().x, posY);
        } else if (inputControl.posy > 0) {
            posY -= 0.15;
            inputControl.posy = 0;
            player.setLinearVelocity(player.getLinearVelocity().x, posY);
        }


        Gdx.gl20.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, camera.combined.cpy().scl(32));
        rayHandler.render();
    }

    void dispose() {
        rayHandler.dispose();
        b2dr.dispose();
        world.dispose();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = camera.position.x + (player.getPosition().scl(32).x - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getPosition().scl(32).y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }

    private static class InputControl extends InputAdapter {
        private OrthographicCamera camera;
        float posx, posy;

        InputControl(OrthographicCamera camera) {
            this.camera = camera;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            float factorMovimientoGesture = 5.5f;
            float x = (Gdx.input.getDeltaX() / factorMovimientoGesture) * 2;
            float y = (Gdx.input.getDeltaY() / factorMovimientoGesture) * 2;
            posx = x;
            posy = y;

            camera.translate(-x, y);
            camera.update();
            return true;
        }
    }

}
