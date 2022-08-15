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
    private float scale_factor = 1.5f;
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
        objects.put(R.raw.arm_01_obj, null);
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
                getCurrentScene().addChild(object.getValue());
            }

            // TODO: Initial setup for each object
            // Base of arm
            objects.get(R.raw.base_arm_obj).setScale(0.7 * scale_factor);
            // Base 2 of the arm
            objects.get(R.raw.waist_arm_obj).setScale(0.7 * scale_factor);
            objects.get(R.raw.waist_arm_obj).rotate(Vector3.Axis.Y, 90);
            objects.get(R.raw.waist_arm_obj).moveUp(0.43f * scale_factor);
            objects.get(R.raw.waist_arm_obj).moveRight(-0.010f);
            objects.get(R.raw.waist_arm_obj).moveForward(-0.048f);
            // Arm low
            objects.get(R.raw.arm_01_obj).setScale(1.7 * scale_factor);
            objects.get(R.raw.arm_01_obj).moveUp(0.6 * scale_factor);
            objects.get(R.raw.arm_01_obj).moveRight(0.150f);
            objects.get(R.raw.arm_01_obj).moveForward(0.05f);


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
        // For debugging
        // Top down view
//        getCurrentCamera().setPosition(0,5,0);
//        getCurrentCamera().setLookAt(objects.get(R.raw.waist_arm_obj).getPosition());
        // Bottom up view
        // Side view
//        getCurrentCamera().setPosition(-5, 5, 0);
//        getCurrentCamera().setLookAt(objects.get(R.raw.waist_arm_obj).getPosition());
        // Front view
        getCurrentCamera().setPosition(0, 5, 5);
        getCurrentCamera().setLookAt(0,2,0);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // TODO: Move around the skybox in drag
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        if (mostrarOBJ == true) {
            super.onRender(elapsedTime, deltaTime);
//            objects.get(R.raw.waist_arm_obj).rotate(Vector3.Axis.Y, 1.0);
        }
    }
}
