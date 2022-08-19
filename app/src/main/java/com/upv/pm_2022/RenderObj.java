package com.upv.pm_2022;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

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
import org.rajawali3d.renderer.Renderer;
import java.util.HashMap;
import java.util.Map;

/**
 * Render for the robotic arm
 * <p>
 * NOTES: <br>
 * About the flickering issue when moving an object: <br>
 *  Looks like when calling an object lower in the hierarchy this doesn't multiply its movement
 *  matrix by the movement matrix of the higher order objects in the hierarchy therefore producing
 *  the effect of the moving piece being drawn in the initial position. <br>
 *  Temporary fix: rotate base by 0, therefore rendering the whole object again
 * <p>
 * About the possibility of a cleaner object import: <br>
 *  In the version 1.2.1970 of Rajawali it is possible to parse an object file (.obj) that contains
 *  various objects and move them independently, that is with their own centroids.
 *  @see <a href=https://github.com/Rajawali/Rajawali/issues/2262>
 *  Need LoaderOBJ to offset child objects to the centroid of their point cloud </a>
 * <p>
 *
 * TODO list:
 * <ul>
 *     <li>Remove hardcoded angle boundaries in rotate*() methods</li>
 *     <li>onDrag() move cameraPosition looking at the figure</li>
 *     <li>Move LoaderOBJ outside for loop; seems to be not possible</li>
 * </ul>
 */
public class RenderObj extends Renderer {
    private final String TAG = "Renderer";
    private final float scale_factor = 1.0f;
    public Context context;
    private HashMap<Integer, Object3D> objects; // A map from the file id and the imported obj
    public final float ANGLE_STEP = 3.0f; // Smoothness of the movements
    public Vector3 cameraPosition;

    public RenderObj(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
        cameraPosition = new Vector3(0, 4, 10); // Initialize camera position

        // Every imported object should be added into this map
        objects = new HashMap<>();
        objects.put(R.raw.base_1, null);
        objects.put(R.raw.base_2, null);
        objects.put(R.raw.arm_1, null);
        objects.put(R.raw.arm_2, null);
        objects.put(R.raw.wrist_1, null);
        objects.put(R.raw.wrist_2, null);
        objects.put(R.raw.gear_1, null);
        objects.put(R.raw.gear_2, null);
        objects.put(R.raw.link_1, null);
        objects.put(R.raw.link_2, null);
        objects.put(R.raw.gripper_1, null);
        objects.put(R.raw.gripper_2, null);
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
        mBlue.setDiffuseMethod(new DiffuseMethod.Lambert());
        mBlue.setColorInfluence(0);
        Texture blueTexture = new Texture("Blue", R.drawable.blue_texture);

        // Material for silver parts (texture and more)
        Material mSilver = new Material();
        mSilver.enableLighting(true);
        mSilver.setDiffuseMethod(new DiffuseMethod.Lambert());
        mSilver.setColorInfluence(0);
        mSilver.setSpecularMethod(new SpecularMethod.Phong());
        Texture silverTexture = new Texture("Silver", R.drawable.silver_texture);

        try {
            // Add the textures to the materials
            mBlue.addTexture(blueTexture);
            mSilver.addTexture(silverTexture);

            // Load every object file
            for(Map.Entry<Integer, Object3D> object : objects.entrySet()) {
                LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), getTextureManager(),
                                                 object.getKey());
                loader.parse();
                objects.put(object.getKey(), loader.getParsedObject());
                getCurrentScene().addChild(object.getValue());
            }

            // Add children relationships (object hierarchy)
            objects.get(R.raw.base_1).addChild(objects.get(R.raw.base_2));
            objects.get(R.raw.base_2).addChild(objects.get(R.raw.arm_1));
            objects.get(R.raw.arm_1).addChild(objects.get(R.raw.arm_2));
            objects.get(R.raw.arm_2).addChild(objects.get(R.raw.wrist_1));
            objects.get(R.raw.wrist_1).addChild(objects.get(R.raw.wrist_2));
            objects.get(R.raw.wrist_2).addChild(objects.get(R.raw.gear_1));
            objects.get(R.raw.wrist_2).addChild(objects.get(R.raw.gear_2));
            objects.get(R.raw.wrist_2).addChild(objects.get(R.raw.link_1));
            objects.get(R.raw.wrist_2).addChild(objects.get(R.raw.link_2));
            objects.get(R.raw.gear_1).addChild(objects.get(R.raw.gripper_1));
            objects.get(R.raw.gear_2).addChild(objects.get(R.raw.gripper_2));

            // Initial setup for each object; positions are relative according to hierarchy
            // Base of the robot arm
            objects.get(R.raw.base_1).setScale(scale_factor);
            objects.get(R.raw.base_1).setMaterial(mSilver);

            // Movable base part of the arm
            objects.get(R.raw.base_2).setScale(scale_factor);
            objects.get(R.raw.base_2).setMaterial(mBlue);

            // Lower part of the arm
            objects.get(R.raw.arm_1).setScale(scale_factor);
            objects.get(R.raw.arm_1).setMaterial(mBlue);
            objects.get(R.raw.arm_1).moveUp(1.25);
            objects.get(R.raw.arm_1).moveRight(0.170);
            objects.get(R.raw.arm_1).moveForward(-0.245);

            // Higher part of the arm
            objects.get(R.raw.arm_2).setScale(scale_factor);
            objects.get(R.raw.arm_2).setMaterial(mBlue);
            objects.get(R.raw.arm_2).moveUp(1.4);
            objects.get(R.raw.arm_2).moveRight(-0.50);
            objects.get(R.raw.arm_2).moveForward(0.12);

            // Movable part of the wrist on X
            objects.get(R.raw.wrist_1).setScale(scale_factor);
            objects.get(R.raw.wrist_1).setMaterial(mBlue);
            objects.get(R.raw.wrist_1).moveRight(1.344);
            objects.get(R.raw.wrist_1).moveForward(-0.021);

            // Movable part of the wrist on Z
            objects.get(R.raw.wrist_2).setScale(scale_factor);
            objects.get(R.raw.wrist_2).setMaterial(mSilver);
            objects.get(R.raw.wrist_2).moveForward(0.125);
            objects.get(R.raw.wrist_2).moveRight(0.415);

            // Parts of Finger 1
            objects.get(R.raw.gear_1).setScale(scale_factor);
            objects.get(R.raw.gear_1).setMaterial(mSilver);
            objects.get(R.raw.gear_1).moveRight(0.595);
            objects.get(R.raw.gear_1).moveForward(0.226);
            objects.get(R.raw.link_1).setScale(scale_factor);
            objects.get(R.raw.link_1).setMaterial(mSilver);
            objects.get(R.raw.link_1).moveRight(0.911);
            objects.get(R.raw.link_1).moveForward(0.102);
            objects.get(R.raw.gripper_1).setScale(scale_factor);
            objects.get(R.raw.gripper_1).setMaterial(mSilver);
            objects.get(R.raw.gripper_1).moveRight(0.446);
            objects.get(R.raw.gripper_1).moveForward(0.183);

            // Parts of Finger 2
            objects.get(R.raw.gear_2).setScale(scale_factor);
            objects.get(R.raw.gear_2).setMaterial(mSilver);
            objects.get(R.raw.gear_2).moveRight(0.595);
            objects.get(R.raw.gear_2).moveForward(-0.195);
            objects.get(R.raw.link_2).setScale(scale_factor);
            objects.get(R.raw.link_2).setMaterial(mSilver);
            objects.get(R.raw.link_2).moveRight(0.911);
            objects.get(R.raw.link_2).moveForward(-0.056);
            objects.get(R.raw.gripper_2).setScale(scale_factor);
            objects.get(R.raw.gripper_2).setMaterial(mSilver);
            objects.get(R.raw.gripper_2).moveRight(0.446);
            objects.get(R.raw.gripper_2).moveForward(-0.172);

            // Set skybox
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy,
                                        R.drawable.negy, R.drawable.posz, R.drawable.negz);

        } catch (ParsingException e) {
            Log.d(TAG + ".initScene", "Couldn't parse file\n" + e.toString());
        } catch (ATexture.TextureException e ) {
            Log.d(TAG + ".initScene", e.toString());
        } catch (NullPointerException e) {
            Log.d(TAG + ".initScene","An object file haven't been initialized correctly");
        }

        // Set camera position
        // For debugging
        // Top down view
//        getCurrentCamera().setPosition(3, 7, 0);
//        getCurrentCamera().setLookAt(0, 0, 0);
        // Bottom up view
//        getCurrentCamera().setPosition(2, 0, 0);
//        getCurrentCamera().setLookAt(2, 5, 0);
        // Side view
//        getCurrentCamera().setPosition(5, 5, 0);
//        getCurrentCamera().setLookAt(objects.get(R.raw.arm_2).getPosition());
        // Front view
        getCurrentCamera().setPosition(cameraPosition); // Update on drag
        getCurrentCamera().setLookAt(0,2,0);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
                                 int xPixelOffset, int yPixelOffset) { }

    /**
     * This method is not getting called
     */
    @Override
    public void onTouchEvent(MotionEvent event) {
        System.out.println("onTouchEvent() called");
    }

    /**
     * Given a vector calculate the new position of the camera.
     * <p>
     * This method gets called onTouch drag
     * <p>
     * HINT: Vector3 implements various math operations over vectors
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public void setCameraLookAt(@NonNull Vector3 vec) {
        cameraPosition.x = vec.x; cameraPosition.y = vec.y; cameraPosition.z = vec.z;
        getCurrentCamera().setPosition(cameraPosition);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
    }

    /**
     * Rotation of the base of the arm
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateBase(boolean isPositive) {
        objects.get(R.raw.base_2).rotate(Vector3.Axis.Y, isPositive ? ANGLE_STEP : -ANGLE_STEP);
    }

    /**
     * Rotate the low part of the arm
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateArmLow(boolean isPositive) {
        System.out.println(objects.get(R.raw.arm_1).getRotZ());
        if((isPositive && objects.get(R.raw.arm_1).getRotZ() < 51) ||
                (!isPositive && objects.get(R.raw.arm_1).getRotZ() > -10.2) )
            objects.get(R.raw.arm_1).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP : -ANGLE_STEP);
        objects.get(R.raw.base_1).rotate(Vector3.Axis.Z, 0.0);
    }

    /**
     * Rotate the high part of the arm
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateArmHigh(boolean isPositive) {
        if( (isPositive && objects.get(R.raw.arm_2).getRotZ() < 21.21) ||
                !isPositive && objects.get(R.raw.arm_2).getRotZ() > -40.8)
            objects.get(R.raw.arm_2).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP : -ANGLE_STEP);
        objects.get(R.raw.base_1).rotate(Vector3.Axis.Z, 0.0);
    }

    /**
     * Rotate wrist around the X-axis
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateArmWristAround(boolean isPositive) {
        objects.get(R.raw.wrist_1).rotate(Vector3.Axis.X, isPositive ? ANGLE_STEP : -ANGLE_STEP);
        objects.get(R.raw.base_1).rotate(Vector3.Axis.Z, 0.0);
    }

    /**
     * Rotate wrist around the Z-axis
     * @param isPositive whether the rotation is positive or negative
     */
    public void rotateArmWrist(boolean isPositive) {
        if((isPositive && objects.get(R.raw.wrist_2).getRotZ() < 48.96) ||
                (!isPositive && objects.get(R.raw.wrist_2).getRotZ() > -48.96))
            objects.get(R.raw.wrist_2).rotate(Vector3.Axis.Z, isPositive ? ANGLE_STEP :-ANGLE_STEP);
        objects.get(R.raw.base_1).rotate(Vector3.Axis.Z, 0.0);
    }

    /**
     * Opens the hand of the robot
     * @param isPositive whether the hand will be opened or closed
     */
    public void openHand(boolean isPositive) {
        // No need to check all objects in the hand, only one gear
        if( (isPositive && objects.get(R.raw.gear_1).getRotY() < 36.72) ||
                (!isPositive && objects.get(R.raw.gear_1).getRotY()>-10.2) ) {
            // Move gears
            objects.get(R.raw.gear_1).rotate(Vector3.Axis.Y, isPositive ? ANGLE_STEP : -ANGLE_STEP);
            objects.get(R.raw.gear_2).rotate(Vector3.Axis.Y, isPositive ? -ANGLE_STEP : ANGLE_STEP);
            // Move links
            objects.get(R.raw.link_1).rotate(Vector3.Axis.Y, isPositive ? ANGLE_STEP : -ANGLE_STEP);
            objects.get(R.raw.link_2).rotate(Vector3.Axis.Y, isPositive ? -ANGLE_STEP : ANGLE_STEP);
            // Move grippers
            objects.get(R.raw.gripper_1).rotate(Vector3.Axis.Y, isPositive ?-ANGLE_STEP:ANGLE_STEP);
            objects.get(R.raw.gripper_2).rotate(Vector3.Axis.Y, isPositive ?ANGLE_STEP:-ANGLE_STEP);
            // Quick fix
            objects.get(R.raw.base_1).rotate(Vector3.Axis.Z, 0.0);
        }
    }
}
