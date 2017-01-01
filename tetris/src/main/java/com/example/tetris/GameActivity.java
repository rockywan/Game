package com.example.tetris;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

//        LinearLayout gameField = (LinearLayout)findViewById(R.id.game_field);
//        gameField.invalidate();

        final GameView gameView = (GameView)findViewById(R.id.game_view);

        findViewById(R.id.left).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    gameView.gc.getCurrentShape().moveLeft();
                    gameView.rePaint();
                }

                return true;
            }
        });

        findViewById(R.id.right).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    gameView.gc.getCurrentShape().moveRight();
                    gameView.rePaint();
                }

                return true;
            }
        });

        findViewById(R.id.up).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    gameView.gc.getCurrentShape().rotate();
                    gameView.rePaint();
                }

                return true;
            }
        });

        findViewById(R.id.down).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    gameView.gc.getCurrentShape().fall();
                    gameView.rePaint();
                }

                return true;
            }
        });
    }
}
