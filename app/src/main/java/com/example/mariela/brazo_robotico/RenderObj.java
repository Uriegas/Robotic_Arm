package com.example.mariela.brazo_robotico;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.HashMap;
import java.util.Map;

public class RenderObj extends RajawaliRenderer {
    public Context context;
    private Object3D obj;
    private boolean mostrarOBJ;
    private Object3D base, base_arm,                                            // Base
                     arm_low, arm_high, arm_wrist,                              // Arm
                     wrist, gear_1, gear_2, link_1, link_2, finger_1, finger_2; // Palm
    private HashMap<Integer, Object3D> objects; // A map from the file name of an obj and the obj

    public RenderObj(Context context, boolean mostrarOBJ) {
        super(context);
        this.context = context;
        this.mostrarOBJ = mostrarOBJ;
        setFrameRate(60);
        objects = new HashMap<>();
        objects.put(R.raw.base_arm_obj, null);
        objects.put(R.raw.waist_arm_obj, null);
    }

    public void setmostrarOBJ(boolean mostrarOBJ) {
        this.mostrarOBJ = mostrarOBJ;
    }

    @Override
    protected void initScene() {
        DirectionalLight key = new DirectionalLight(-3,-4,-5);
        key.setPower(2);
        getCurrentScene().addLight(key);

        try {
            // Load every object file
            // TODO: Move LoaderOBJ outside for loop
            for(Map.Entry<Integer, Object3D> object : objects.entrySet()) {
                LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), getTextureManager(),
                                                 object.getKey());
                loader.parse();
                objects.put(object.getKey(), loader.getParsedObject());
                object.getValue().setScale(0.7f); // At this point the object should be loaded
                getCurrentScene().addChild(object.getValue());
            }

            // TODO: Initial setup for each object
            objects.get(R.raw.base_arm_obj).setScale(0.7);
            objects.get(R.raw.waist_arm_obj).setScale(0.7);
            objects.get(R.raw.waist_arm_obj).moveUp(0.45f);

            // Set skybox
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy,
                                        R.drawable.negy, R.drawable.posz, R.drawable.negz);

//            obj.moveRight(10.0);
            //getCurrentScene().addChild(obj);
            //getCurrentCamera().setZ(3.2f);
            //getCurrentCamera().setY(10.5f);
            //obj.rotate(Vector3.Axis.Y, 30.0);

        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (ATexture.TextureException e ) {
            Log.e("Render", "Skybox not found");
        }

        // Set camera position
        //getCurrentCamera().setPosition(6,20,6);
        getCurrentCamera().setPosition(6,5,5);
        //getCurrentCamera().setLookAt(obj.getPosition());
        getCurrentCamera().setLookAt(0,2,0);
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
            objects.get(R.raw.waist_arm_obj).rotate(Vector3.Axis.Y, 1.0);
        }
    }
}
