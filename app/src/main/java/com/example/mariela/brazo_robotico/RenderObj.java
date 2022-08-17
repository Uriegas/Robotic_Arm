package com.example.mariela.brazo_robotico;

import android.content.Context;
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
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.HashMap;
import java.util.Map;

public class RenderObj extends RajawaliRenderer {
    private final String TAG = "Renderer";
    private float scale_factor = 1.0f;
    public float cameraX = 0, cameraY = 2, cameraZ = 0; // Initial values of the camera look at
    public float canvasX = 0, canvasY = 0, canvasZ = 0;
    private float moveSpeed = 0.02f;
    public Context context;
    private boolean mostrarOBJ;
    private HashMap<Integer, Object3D> objects; // A map from the file name of an obj and the obj

    // Angle parameters to move the arm
    public float g_arm1Angle = 3.0f, g_joint1Angle, g_joint2Angle, g_joint3Angle, g_joint4Angle;
    public final float ANGLE_STEP = 3.0f;
    public Matrix4 rotate_matrix;

    // Matrix used for movement

    public RenderObj(Context context, boolean mostrarOBJ) {
        super(context);
        this.context = context;
        this.mostrarOBJ = mostrarOBJ;
        setFrameRate(60);

        // Add all objects to draw into a map
        objects = new HashMap<>();
        objects.put(R.raw.base_1, null);
        objects.put(R.raw.base_2, null);
        objects.put(R.raw.arm_1, null);
        objects.put(R.raw.arm_2, null);
        objects.put(R.raw.wrist_1, null);
        objects.put(R.raw.wrist_2, null);
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

            // Add children relationships
            objects.get(R.raw.base_1).addChild(objects.get(R.raw.base_2));
            objects.get(R.raw.base_2).addChild(objects.get(R.raw.arm_1));
            objects.get(R.raw.arm_1).addChild(objects.get(R.raw.arm_2));
            objects.get(R.raw.arm_2).addChild(objects.get(R.raw.wrist_1));
            objects.get(R.raw.wrist_1).addChild(objects.get(R.raw.wrist_2));

            // Initial setup for each object; positions are relative according to hierarchy
            // Base of the robot arm
//            objects.get(R.raw.base_1).setScale(scale_factor);
            objects.get(R.raw.base_1).setMaterial(mSilver);

            // Movable base part of the arm
//            objects.get(R.raw.base_2).setScale(scale_factor);
            objects.get(R.raw.base_2).setMaterial(mBlue);

            // Lower part of the arm
//            objects.get(R.raw.arm_1).setScale(scale_factor);
            objects.get(R.raw.arm_1).setMaterial(mBlue);
            objects.get(R.raw.arm_1).moveUp(1.25 * scale_factor);
            objects.get(R.raw.arm_1).moveRight(0.170 * scale_factor);
            objects.get(R.raw.arm_1).moveForward(-0.245 * scale_factor);

            // Higher part of the arm
//            objects.get(R.raw.arm_2).setScale(scale_factor);
            objects.get(R.raw.arm_2).setMaterial(mBlue);
            objects.get(R.raw.arm_2).moveUp(1.4 * scale_factor);
            objects.get(R.raw.arm_2).moveRight(-0.50 * scale_factor);
            objects.get(R.raw.arm_2).moveForward(0.12 * scale_factor);

            // Movable part of the wrist on X
//            objects.get(R.raw.wrist_1).setScale(scale_factor);
            objects.get(R.raw.wrist_1).setMaterial(mBlue);
            objects.get(R.raw.wrist_1).moveRight(1.344 * scale_factor);
            objects.get(R.raw.wrist_1).moveForward(-0.021 * scale_factor);

            // Movable part of the wrist on Z
//            objects.get(R.raw.wrist_2).setScale(scale_factor);
            objects.get(R.raw.wrist_2).setMaterial(mSilver);
            objects.get(R.raw.wrist_2).moveForward(0.125);
            objects.get(R.raw.wrist_2).moveRight(0.415);


            // Set skybox
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy,
                                        R.drawable.negy, R.drawable.posz, R.drawable.negz);

            // Set initial x, y, z centers
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
//        getCurrentCamera().setPosition(0,8,0);
//        getCurrentCamera().setLookAt(objects.get(R.raw.arm_02_v3_obj).getPosition());
        // Bottom up view
        // Side view
//        getCurrentCamera().setPosition(5, 5, 0);
//        getCurrentCamera().setLookAt(objects.get(R.raw.arm_2).getPosition());
        // Front view
        getCurrentCamera().setPosition(0, 4, 8); // Update on drag
        getCurrentCamera().setLookAt(cameraX,cameraY,cameraZ);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
                                 int xPixelOffset, int yPixelOffset) { }

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
     * TODO: Instead if changing look at, change camera position to rotate over the arm
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
//            objects.get(R.raw.base_2).rotate(Vector3.Axis.Y, 1.0);
//            objects.get(R.raw.arm_2).rotate(Vector3.Axis.Z, 1.0);
//            getCurrentCamera().rotate(Vector3.Axis.Y, 1.0);
        }
    }

    // TODO: Looks like when calling an object lower in the hierarchy this doesn't multiply its
    //       movement matrix by the movement matrix of the higher order objects in the hierarchy
    //       therefore producing the effect of the moving piece being drawn in the start position

    /**
     * Rotation of the base of the arm
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateBase(boolean isPositive) {
        objects.get(R.raw.base_2).rotate(Vector3.Axis.Y, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    public void rotateArmLow(boolean isPositive) {
        objects.get(R.raw.arm_1).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    public void rotateArmHigh(boolean isPositive) {
        objects.get(R.raw.arm_2).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    public void rotateArmWristAround(boolean isPositive) {
        objects.get(R.raw.wrist_1).rotate(Vector3.Axis.X, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    public void rotateArmWrist(boolean isPositive) {
        objects.get(R.raw.wrist_2).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    /**
     * Opens the hand of the robot
     * @param isPositive if negative is provided the hand will close
     */
    public void openHand(boolean isPositive) {
        // TODO: Several movements; move 4 pieces
    }
}
