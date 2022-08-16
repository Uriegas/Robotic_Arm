package com.example.mariela.brazo_robotico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {
    RenderObj render;
    RajawaliSurfaceView surfaceView;
    Button B1, B2, B3, B4, B5, B6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        render = new RenderObj(this, true);

        // Get buttons
        B1 = findViewById(R.id.brazoAbajo);
        B2 = findViewById(R.id.brazoArriba);
        B3 = findViewById(R.id.munecaDerecha);
        B4 = findViewById(R.id.munecaIzquierda);
        B5 = findViewById(R.id.dedosAbrir);
        B6 = findViewById(R.id.dedosCerrar);

        // Traditional surfaceView
        surfaceView = findViewById(R.id.gl_surface_view);
        surfaceView.setFrameRate(60.0);
        surfaceView.setSurfaceRenderer(render);
        surfaceView.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // SurfaceView with onTouch over skybox
        // TODO: Change this to inflate, so we can get the buttons of the xml
//        render = new RenderObj(this, true);
//        surfaceView = new ArmSurfaceView(this, render);
//        setContentView(surfaceView);

        // Set buttons actions
        B2.setOnClickListener(view -> { // rotate arm clockwise
//            if (render.angle_1 < 135.0) render.angle_1 += render.ANGLE_STEP;
        });
        B1.setOnClickListener(view -> { // rotate arm counter-clockwise
//            if (render.angle_1 > -135.0) render.angle_1 -= render.ANGLE_STEP;
        });
        B3.setOnClickListener(view -> { // rotate hand clockwise
//            render.angle_2 = (render.angle_2 + render.ANGLE_STEP) % 360;
        });
        B4.setOnClickListener(view -> { // rotate hand counter-clockwise
//            render.angle_2 = (render.angle_2 - render.ANGLE_STEP) % 360;
        });
        B5.setOnClickListener(view -> { // Open hand
//            if (render.angle_3 < 8.0) render.angle_3 = (render.angle_3 + render.ANGLE_STEP) % 360;
        });
        B6.setOnClickListener(view -> { // Close hand
//            if (render.angle_3 > -8.0) render.angle_3 = (render.angle_3 - render.ANGLE_STEP) % 360;
        });
    }
}
