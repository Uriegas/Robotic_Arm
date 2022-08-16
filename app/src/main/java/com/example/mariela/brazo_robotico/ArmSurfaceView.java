package com.example.mariela.brazo_robotico;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class ArmSurfaceView extends RajawaliSurfaceView {
    private RenderObj render;

    public ArmSurfaceView(Context context, RenderObj render) {
        super(context);
        this.setSurfaceRenderer(render);
        this.render = render;
        this.setFrameRate(60.0);
        this.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
    }

    /**
     * Move camera look_at when user drags over the screen
     * @param event the given event
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        float x = event.getX(), y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: // Move camera
                render.setCameraLookAt(x, y, 0);
//                System.out.println(render.cameraX + ", " + render.cameraY + ", " + render.cameraZ);
                break;
        }
        return true;
    }
}
