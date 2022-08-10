package com.example.mariela.brazo_robotico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {
    RenderObj render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get buttons
        Button B1 = findViewById(R.id.brazoAbajo);
        Button B2 = findViewById(R.id.brazoArriba);
        Button B3 = findViewById(R.id.munecaDerecha);
        Button B4 = findViewById(R.id.munecaIzquierda);
        Button B5 = findViewById(R.id.dedosAbrir);
        Button B6 = findViewById(R.id.dedosCerrar);

        // Set surfaceview
        final RajawaliSurfaceView surface;
        surface = findViewById(R.id.gl_surface_view);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        render = new RenderObj(this, true);
        surface.setSurfaceRenderer(render);

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
