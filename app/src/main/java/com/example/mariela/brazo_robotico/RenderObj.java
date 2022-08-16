package com.example.mariela.brazo_robotico;

import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RenderObj extends RajawaliRenderer {
    private final String TAG = "Renderer";
    private float scale_factor = 1.5f;
    public float cameraX = 0, cameraY = 2, cameraZ = 0; // Initial values of the camera look at
    public float canvasX = 0, canvasY = 0, canvasZ = 0;
    private float moveSpeed = 0.02f;
    public Context context;
    private boolean mostrarOBJ;
    private HashMap<Integer, Object3D> objects; // A map from the file name of an obj and the obj

    // For reference; will be deleted anyways by the garbage collector
    private Object3D base, base_arm,                                            // Base
                     arm_low, arm_high, arm_wrist,                              // Arm
                     wrist, gear_1, gear_2, link_1, link_2, finger_1, finger_2; // Palm

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
        // Set lighting for the scene
        DirectionalLight key = new DirectionalLight(-3,-4,-5);
        key.setPower(2);
        getCurrentScene().addLight(key);

        // Material for blue parts (texture and more)
        Material mBlue = new Material();
        mBlue.enableLighting(true);
        mBlue.setDiffuseMethod(new DiffuseMethod.Lambert()); // Use the lambert method for lighting
        mBlue.setColorInfluence(0); // TODO experiment this
        Texture blueTexture = new Texture("Blue", R.drawable.blue_texture);

        // Material for silver parts (texture and more)
        Material mSilver = new Material();
        mSilver.enableLighting(true);
        mSilver.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSilver.setColorInfluence(0);
        mSilver.setSpecularMethod(new SpecularMethod.Phong());
        Texture silverTexture = new Texture("Silver", R.drawable.silver_texture);

        try {
            // Try adding the textures to the materials
            mBlue.addTexture(blueTexture);
            mSilver.addTexture(silverTexture);

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
            objects.get(R.raw.base_arm_obj).setMaterial(mSilver);
            // Base 2 of the arm
            objects.get(R.raw.waist_arm_obj).setScale(0.7 * scale_factor);
            objects.get(R.raw.waist_arm_obj).rotate(Vector3.Axis.Y, 90);
            objects.get(R.raw.waist_arm_obj).moveUp(0.43f * scale_factor);
            objects.get(R.raw.waist_arm_obj).moveRight(-0.010f);
            objects.get(R.raw.waist_arm_obj).moveForward(-0.048f);
            objects.get(R.raw.waist_arm_obj).setMaterial(mBlue);
            // Arm low
            objects.get(R.raw.arm_01_obj).setScale(1.7 * scale_factor);
            objects.get(R.raw.arm_01_obj).moveUp(0.6 * scale_factor);
            objects.get(R.raw.arm_01_obj).moveRight(0.150f);
            objects.get(R.raw.arm_01_obj).moveForward(0.05f);
            objects.get(R.raw.arm_01_obj).setMaterial(mBlue);

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
            Log.d(TAG + ".initScene", e.toString());
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
        getCurrentCamera().setLookAt(cameraX,cameraY,cameraZ);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    /**
     * The implementation of onTouch is in {@link ArmSurfaceView}
     */
    @Override
    public void onTouchEvent(MotionEvent event) { }

    /**
     * This is a work-around to the fact that mapping the Android plane to the Rajawali plane is
     * hard since we can't get the direction of any move operation, thus we have to implement memory
     * of the previous position
     * <p>
     * We don't modify the <bold>z</bold> coordinate since we don't want to modify the zoom of the
     * camera yet but this could be implemented later.
     * <p>
     * HINT: {@link ArmSurfaceView#onTouchEvent(MotionEvent)} should handle this with gestures
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public void setCameraLookAt(float x, float y, float z) {
        // TODO: Movement is not as straight forward, get direction of the finger
        cameraX += (x - canvasX < 0 ? 1 : -1) * moveSpeed;
        cameraY += (y - canvasY > 0 ? 1 : -1) * moveSpeed;
        // cameraZ = += (z - canvasZ > 0 ? 1 : -1) * moveSpeed;
        // Set new canvas positions
        canvasX = x; canvasY = y;

        // Move lookAt in camera
        getCurrentCamera().setLookAt(cameraX, cameraY, cameraZ);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        if (mostrarOBJ == true) {
            super.onRender(elapsedTime, deltaTime);
//            objects.get(R.raw.waist_arm_obj).rotate(Vector3.Axis.Y, 1.0);
        }
    }
}
