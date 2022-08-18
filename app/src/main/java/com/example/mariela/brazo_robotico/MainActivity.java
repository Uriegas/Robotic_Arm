package com.example.mariela.brazo_robotico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {
    RenderObj render;
    RajawaliSurfaceView surfaceView;
    Button B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        render = new RenderObj(this);

        // Get buttons
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B2);
        B3 = findViewById(R.id.B3);
        B4 = findViewById(R.id.B4);
        B5 = findViewById(R.id.B5);
        B6 = findViewById(R.id.B6);
//        B7 = findViewById(R.id.B7);
//        B8 = findViewById(R.id.B8);
        B9 = findViewById(R.id.B9);
        B10 = findViewById(R.id.B10);
        B11 = findViewById(R.id.B11);
        B12 = findViewById(R.id.B12);

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
        B2.setOnClickListener(view -> { render.rotateBase(false); });
        B3.setOnClickListener(view -> { render.rotateArmLow(true); });
        B4.setOnClickListener(view -> { render.rotateArmLow(false);});
        B5.setOnClickListener(view -> { render.rotateArmHigh(true);});
        B6.setOnClickListener(view -> { render.rotateArmHigh(false);});
//        B7.setOnClickListener(view -> { render.rotateArmWristAround(true); });
//        B8.setOnClickListener(view -> { render.rotateArmWristAround(false); });
        B9.setOnClickListener(view -> { render.rotateArmWrist(true); });
        B10.setOnClickListener(view -> { render.rotateArmWrist(false);});
        B11.setOnClickListener(view -> { render.openHand(true);});
        B12.setOnClickListener(view -> { render.openHand(false);});
    }
}
