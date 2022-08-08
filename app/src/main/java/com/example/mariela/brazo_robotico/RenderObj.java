package com.example.mariela.brazo_robotico;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

public class RenderObj extends RajawaliRenderer {
    public Context context;
    private Object3D obj;
    boolean mostrarOBJ;

    public RenderObj(Context context, boolean mostrarOBJ) {
        super(context);
        this.context = context;
        this.mostrarOBJ = mostrarOBJ;
        setFrameRate(60);
    }

    public void setmostrarOBJ(boolean mostrarOBJ) {
        this.mostrarOBJ = mostrarOBJ;
    }

    @Override
    protected void initScene() {

        DirectionalLight key = new DirectionalLight(-3,-4,-5);
        key.setPower(2);
        getCurrentScene().addLight(key);

        LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), getTextureManager(), R.raw.brazo_skin);

        try {

            loader.parse();
            obj = loader.getParsedObject();
            obj.setScale(0.5f);
            getCurrentScene().addChild(obj);

            //obj.moveRight(10.0);
            //getCurrentScene().addChild(obj);
            //getCurrentCamera().setZ(3.2f);
            //getCurrentCamera().setY(10.5f);
            //obj.rotate(Vector3.Axis.Y, 30.0);

        } catch (ParsingException e) {
            e.printStackTrace();
        }

        //getCurrentCamera().setPosition(6,20,6);
        getCurrentCamera().setPosition(0,5,5);
        //getCurrentCamera().setLookAt(obj.getPosition());
        getCurrentCamera().setLookAt(0,0,0);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        if (mostrarOBJ == true) {
            super.onRender(elapsedTime, deltaTime);
            obj.rotate(Vector3.Axis.Y, 1.0);
        }
    }
}
