package com.example.tetris;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/19.
 */

public class GameController {
    public static final int ROW_NUMBER = 20;
    public static final int COLUMN_NUMBER = 10;

    public int[][] cells = new int[ROW_NUMBER][COLUMN_NUMBER];
    private float cellWidth;
    private float cellHeight;

    private Shape currentShape;

    /*
    * arguments: size of the canvas
     */
    public GameController() {
    }

    public void clearCells() {
        for (int i = 0; i < ROW_NUMBER; i++) {
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                cells[i][j] = 0;
            }
        }
    }

    public void start() {
        generateShape();
    }

    private void generateShape() {
//        Class<?>[] allShapes = {ShapeL.class, ShapeM.class, ShapeS.class};
//
//        try {
//            currentShape = (Shape) allShapes[(int)(Math.random()*3)].newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        int no = (int) (Math.random() * 7);
        switch (no) {
            case 0:
                currentShape = new ShapeL();
                break;
            case 1:
                currentShape = new ShapeM();
                break;
            case 2:
                currentShape = new ShapeS();
                break;
            case 3:
                currentShape = new ShapeF();
                break;
            case 4:
                currentShape = new ShapeNF();
                break;
            case 5:
                currentShape = new ShapeZ();
                break;
            case 6:
                currentShape = new ShapeNZ();
                break;
        }
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public int[][] getCells() {
        return cells;
    }

    private void flushCells() {
        for (int i = GameController.ROW_NUMBER - 1; i > 0; ) {
            boolean clearRow = true;
            for (int j = 0; j < GameController.COLUMN_NUMBER; j++) {
                if (cells[i][j] == 0) {
                    clearRow = false;
                    break;
                }
            }
            if (clearRow) {
                for (int j = 0; j < GameController.COLUMN_NUMBER; j++) {
                    cells[0][j] = 0;
                }

                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < GameController.COLUMN_NUMBER; j++) {
                        cells[k][j] = cells[k - 1][j];
                    }
                }
            } else {
                i--;
            }
        }
    }

    abstract class Shape {
        protected int[][] relativePos;
        protected int absRow = 0;
        protected int absCol = 0;

        public int getAbsRow() {
            return absRow;
        }

        public int getAbsCol() {
            return absCol;
        }

        public int[][] getRelativePos() {
            return relativePos;
        }

        public void rotate() {
            if (allowRotate()) {
                doRotate();
                if (allowDown())
                {
                    toBeFrozen = false;
                }
            }
        }

        protected boolean allowRotate() {
            int[][] nextPos = getNextPos();

            for (int i = 0; i < 4; i++) {
                if ((nextPos[i][0] + absRow > ROW_NUMBER - 1)
                        || (nextPos[i][0] + absRow < 0)
                        || (nextPos[i][1] + absCol > COLUMN_NUMBER - 1)
                        || (nextPos[i][1] + absCol < 0)) {
                    return false;
                }
                if (cells[nextPos[i][0] + absRow][nextPos[i][1] + absCol] == 1) {
                    return false;
                }
            }

            return true;
        }

        public void doRotate() {
            relativePos = getNextPos();
        }

        public abstract int[][] getNextPos();

        private boolean toBeFrozen = false;

        public void down() {
            if (toBeFrozen) {
                frozen();
                toBeFrozen = false;
                return;
            }
            if (allowDown()) {
                absRow++;
            } else {
                //frozen after one interval unit
                toBeFrozen = true;
            }
        }

        public void fall() {
            while (allowDown()) {
                absRow++;
            }
        }

        private void frozen() {
            for (int i = 0; i < 4; i++) {
                cells[relativePos[i][0] + absRow][relativePos[i][1] + absCol] = 1;
            }

            flushCells();
            generateShape();
        }

        public void moveLeft() {
            if (allowLeft()) absCol--;
        }

        public void moveRight() {
            if (allowRight()) absCol++;
        }

        private boolean allowLeft() {
            //detect collision
            for (int i = 0; i < 4; i++) {
                if (relativePos[i][1] + absCol <= 0) {
                    return false;
                }
                if (cells[relativePos[i][0] + absRow][relativePos[i][1] + absCol - 1] == 1) {
                    return false;
                }
            }
            return true;
        }

        private boolean allowRight() {
            //detect collision
            for (int i = 0; i < 4; i++) {
                if (relativePos[i][1] + absCol >= COLUMN_NUMBER - 1) {
                    return false;
                }
                if (cells[relativePos[i][0] + absRow][relativePos[i][1] + absCol + 1] == 1) {
                    return false;
                }
            }
            return true;
        }

        private boolean allowDown() {
            //detect collision
            for (int i = 0; i < 4; i++) {
                if (relativePos[i][0] + absRow >= ROW_NUMBER - 1) {
                    return false;
                }
                if (cells[relativePos[i][0] + absRow + 1][relativePos[i][1] + absCol] == 1) {
                    return false;
                }
            }
            return true;
        }
    }

    class ShapeL extends Shape {
        public final int[][] zero = {{0, 4}, {1, 4}, {2, 4}, {3, 4}};
        public final int[][] ninety = {{0, 3}, {0, 4}, {0, 5}, {0, 6}};

        public ShapeL() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else {
                return zero;
            }
        }
    }

    class ShapeS extends Shape {
        public final int[][] zero = {{0, 4}, {1, 4}, {0, 5}, {1, 5}};

        public ShapeS() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            return zero;
        }
    }

    class ShapeM extends Shape {
        public final int[][] zero = {{0, 5}, {1, 4}, {1, 5}, {1, 6}};
        public final int[][] ninety = {{0, 5}, {1, 6}, {1, 5}, {2, 5}};
        public final int[][] oneEighty = {{1, 4}, {1, 5}, {1, 6}, {2, 5}};
        public final int[][] twoSeventy = {{0, 5}, {1, 4}, {1, 5}, {2, 5}};

        public ShapeM() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else if (relativePos == ninety) {
                return oneEighty;
            } else if (relativePos == oneEighty) {
                return twoSeventy;
            } else {
                return zero;
            }
        }
    }

    class ShapeF extends Shape {
        public final int[][] zero = {{0, 4}, {1, 4}, {2, 4}, {2, 5}};
        public final int[][] ninety = {{1, 4}, {2, 4}, {1, 5}, {1, 6}};
        public final int[][] oneEighty = {{0, 4}, {0, 5}, {1, 5}, {2, 5}};
        public final int[][] twoSeventy = {{2, 4}, {2, 5}, {2, 6}, {1, 6}};

        public ShapeF() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else if (relativePos == ninety) {
                return oneEighty;
            } else if (relativePos == oneEighty) {
                return twoSeventy;
            } else {
                return zero;
            }
        }
    }

    class ShapeNF extends Shape {
        public final int[][] zero = {{0, 5}, {1, 5}, {2, 4}, {2, 5}};
        public final int[][] ninety = {{1, 3}, {2, 3}, {2, 4}, {2, 5}};
        public final int[][] oneEighty = {{0, 4}, {0, 5}, {1, 4}, {2, 4}};
        public final int[][] twoSeventy = {{1, 3}, {1, 4}, {1, 5}, {2, 5}};

        public ShapeNF() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else if (relativePos == ninety) {
                return oneEighty;
            } else if (relativePos == oneEighty) {
                return twoSeventy;
            } else {
                return zero;
            }
        }
    }

    class ShapeZ extends Shape {
        public final int[][] zero = {{0, 4}, {1, 4}, {1, 5}, {2, 5}};
        public final int[][] ninety = {{1, 4}, {1, 5}, {2, 3}, {2, 4}};

        public ShapeZ() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else {
                return zero;
            }
        }
    }

    class ShapeNZ extends Shape {
        public final int[][] zero = {{0, 5}, {1, 5}, {1, 4}, {2, 4}};
        public final int[][] ninety = {{1, 4}, {1, 5}, {2, 6}, {2, 5}};

        public ShapeNZ() {
            super();
            relativePos = zero;
        }

        public int[][] getNextPos() {
            if (relativePos == zero) {
                return ninety;
            } else {
                return zero;
            }
        }
    }
}
