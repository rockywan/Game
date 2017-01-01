package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Color;
import android.graphics.Paint;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private PaintThread myThread;
    public GameController gc;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    public GameView(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
        init();
    }

    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
        myThread = new PaintThread(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        System.out.print("changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gc = new GameController();
        gc.start();
        myThread.isRun = true;
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myThread.isRun = false;
    }

    public void rePaint() {
        Canvas c = holder.lockCanvas();
        paintCells(c);
        holder.unlockCanvasAndPost(c);
    }

    private void paintCells(Canvas c) {
        c.drawColor(Color.BLUE);
        Paint p = new Paint();

        int h = c.getHeight();
        int w = c.getWidth();

        float cellHeight = h / GameController.ROW_NUMBER;
        float cellWidth = w / GameController.COLUMN_NUMBER;

        int[][] relativePos = gc.getCurrentShape().getRelativePos();
        int absRow = gc.getCurrentShape().getAbsRow();
        int absCol = gc.getCurrentShape().getAbsCol();
        for (int i = 0; i < 4; i++) {
            int row = relativePos[i][0] + absRow;
            int col = relativePos[i][1] + absCol;

            float left = col * cellWidth;
            float top = row * cellHeight;
            float right = left + cellWidth;
            float bot = top + cellHeight;
            p.setColor(Color.GREEN);
            p.setStrokeWidth(5);
            c.drawRect(left + 1, top + 1, right - 1, bot - 1, p);
        }

        int[][] cells = gc.getCells();
        for (int i = 0; i < GameController.ROW_NUMBER; i++) {
            for (int j = 0; j < GameController.COLUMN_NUMBER; j++) {
                if (cells[i][j] == 1) {
                    float left = j * cellWidth;
                    float top = i * cellHeight;
                    float right = left + cellWidth;
                    float bot = top + cellHeight;
                    p.setColor(Color.GREEN);
                    p.setStrokeWidth(5);
                    c.drawRect(left + 1, top + 1, right - 1, bot - 1, p);
                }
            }
        }

    }

    //inner thread class
    class PaintThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun;

        public PaintThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = false;
        }

        @Override
        public void run() {
            int count = 0;
            while (isRun) {
                Canvas c = null;
                try {
                    synchronized (holder) {
                        Thread.sleep(1000);
                        c = holder.lockCanvas();
                        paintCells(c);
                        gc.getCurrentShape().down();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}