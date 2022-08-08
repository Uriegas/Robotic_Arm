package com.example.mariela.brazo_robotico;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {
//    private SurfaceView mGLSurfaceView;
//    private Render render;
//    View mVictimContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mGLSurfaceView = findViewById(R.id.gl_surface_view);
//        mVictimContainer = findViewById(R.id.hidecontainer);
//        // Get buttons
//        Button B1 = findViewById(R.id.brazoAbajo);
//        Button B2 = findViewById(R.id.brazoArriba);
//        Button B3 = findViewById(R.id.munecaDerecha);
//        Button B4 = findViewById(R.id.munecaIzquierda);
//        Button B5 = findViewById(R.id.dedosAbrir);
//        Button B6 = findViewById(R.id.dedosCerrar);
//
//        B2.setOnClickListener(view -> { // rotate arm clockwise
//            if (render.angle_1 < 135.0) render.angle_1 += render.ANGLE_STEP;
//        });
//        B1.setOnClickListener(view -> { // rotate arm counter-clockwise
//            if (render.angle_1 > -135.0) render.angle_1 -= render.ANGLE_STEP;
//        });
//        B3.setOnClickListener(view -> { // rotate hand clockwise
//            render.angle_2 = (render.angle_2 + render.ANGLE_STEP) % 360;
//        });
//        B4.setOnClickListener(view -> { // rotate hand counter-clockwise
//            render.angle_2 = (render.angle_2 - render.ANGLE_STEP) % 360;
//        });
//        B5.setOnClickListener(view -> { // Open hand
//            if (render.angle_3 < 8.0) render.angle_3 = (render.angle_3 + render.ANGLE_STEP) % 360;
//        });
//        B6.setOnClickListener(view -> { // Close hand
//            if (render.angle_3 > -8.0) render.angle_3 = (render.angle_3 - render.ANGLE_STEP) % 360;
//        });
//
//        // Request a context compatible with OpenGL ES 2.0.
//        mGLSurfaceView.setEGLContextClientVersion(2);
//
//        final DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        // Establish render
//        render = new Render(this);
//        mGLSurfaceView.setRenderer(render, displayMetrics.density);
//    }

    RenderObj render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // Add mSurface to your root view
        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        render = new RenderObj(this, true);
        surface.setSurfaceRenderer(render);
    }
}
