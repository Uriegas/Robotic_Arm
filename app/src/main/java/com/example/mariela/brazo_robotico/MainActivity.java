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
        render = new RenderObj(this);

        // Get buttons
        B1 = findViewById(R.id.brazoArriba);
        B2 = findViewById(R.id.brazoAbajo);
        B3 = findViewById(R.id.munecaIzquierda);
        B4 = findViewById(R.id.munecaDerecha);
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
        B1.setOnClickListener(view -> { render.rotateBase(true); });
        B2.setOnClickListener(view -> { render.rotateArmLow(true); });
        B3.setOnClickListener(view -> { render.rotateArmHigh(true); });
        B4.setOnClickListener(view -> { render.rotateArmWristAround(true);});
        B5.setOnClickListener(view -> { render.rotateArmWrist(true);});
        B6.setOnClickListener(view -> { render.openHand(true);});
    }
}
