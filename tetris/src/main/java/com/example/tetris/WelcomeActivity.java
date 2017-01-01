package com.example.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {

    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        mContentView = findViewById(R.id.main_content);

        /*
        Upon interacting with UI controls, delay any scheduled hide()
        operations to prevent the jarring behavior of controls going away
        while interacting with the UI.
        */
        findViewById(R.id.go_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(mContentView.getContext(), GameActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }
}
