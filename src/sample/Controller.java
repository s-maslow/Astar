package sample;


import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.canvas.Canvas;
import java.util.Vector;

/**
 * \brief Данный класс отвечает за обработку клавиш в интерфейсе и дальнейщей отрисовке с помощью метода drawField()
 *  Интерфейс содержит в себе два варианта: самогенерирующаяся рандомная матрица для дальнейщего запуска её решения, или же
 *  вводится самостоятельно двумерный массив, и после чего запускается решение.
 */
public class Controller {

    private Field currentField;
    private Solver solver;
    private Vector<Board> solution;
    private int currentStatement;

    @FXML public Canvas mainCan;
    @FXML public TextField dimension;
    @FXML public TextArea matrix;
    @FXML public Label text;

    /**
     * \brief Данный метод отвечает за обработку клафиши в интерфейсе, которая по сути приводит весь алгоритм решения в действие
     * Всё отрисовывается на canvas.
     */
    public void doIT() {
        currentStatement = 0;
        GraphicsContext gc = mainCan.getGraphicsContext2D();
        gc.clearRect(0, 0, mainCan.getWidth(), mainCan.getHeight());
        String temp = dimension.getText();
        if (temp.length() != 0) {
            int n = Integer.parseInt(temp);
            if (2 <= n && n <= 20) {
                currentField = new Field(n);
                solver = new Solver(currentField);
                if (solver.isSolvable()){
                    text.setText("number of moves: " + solver.moves());
                } else {
                    text.setText("NO SOLUTION");
                }
                solution = new Vector(0);
                for(Board board : solver.solution()) {
                    solution.add(board);
                }
                int pxlSize = 300;
                currentField.drawField(pxlSize, mainCan, ((int) mainCan.getWidth() - pxlSize) / 2, ((int) mainCan.getHeight() - pxlSize) / 2, gc);
            }
        }
    }

    /**
     * \brief Данный метод используется когда мы задаём собственную матрицу board,
     * т.е. мы сами заполняет числами двумерный массив, для дальнейшего его решения.
     *
     */
    public void makeFieldFromMatrix() {
        currentStatement = 0;
        GraphicsContext gc = mainCan.getGraphicsContext2D();
        gc.clearRect(0, 0, mainCan.getWidth(), mainCan.getHeight());
        String temp = matrix.getText();
        if (temp.length() != 0) {
            String strArr[] = temp.split("\\p{P}?[ \\t\\n\\r]+");
            int size = (int) Math.sqrt(strArr.length);
            int matrix[][] = new int[size][size];
            int k = 0;
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = Integer.parseInt(strArr[k]);
                    k++;
                }
            }
            currentField = new Field(matrix);
            solver = new Solver(currentField);
            if (solver.isSolvable()){
                text.setText("number of moves: " + solver.moves());
            } else {
                text.setText("NO SOLUTION");
            }
            if(solver.isSolvable()) {
                solution = new Vector(0);
                for (Board board : solver.solution()) {
                    solution.add(board);
                }
            }
            int pxlSize = 300;
            currentField.drawField(pxlSize, mainCan, ((int) mainCan.getWidth() - pxlSize) / 2, ((int) mainCan.getHeight() - pxlSize) / 2, gc);
        }
    }

    /**
     * \brief Данный метод, отрабатывает клавишу "вперед", чтобы не решать заново каждый раз board, создаётся
     * вектор содержащий в себе последовательность board которые выводятся на canvas
     */
    public void nextNode() {
        if (solver.isSolvable()) {
            if (0 <= currentStatement && currentStatement < solution.size() - 1) {
                currentStatement += 1;
                    GraphicsContext gc = mainCan.getGraphicsContext2D();
                    gc.clearRect(0, 0, mainCan.getWidth(), mainCan.getHeight());
                    currentField.board = solution.get(currentStatement).board;
                    int pxlSize = 300;
                    currentField.drawField(pxlSize, mainCan, ((int) mainCan.getWidth() - pxlSize) / 2, ((int) mainCan.getHeight() - pxlSize) / 2, gc);
            }
        }
    }

    /**
     * \briefДанный метод, обрабатывает клафишу "назад".
      */
    public void prevNode() {
        if (solver.isSolvable()) {
            if (currentStatement > 0)
                currentStatement -= 1;
                GraphicsContext gc = mainCan.getGraphicsContext2D();
                gc.clearRect(0, 0, mainCan.getWidth(), mainCan.getHeight());
                currentField.board = solution.get(currentStatement).board;
                int pxlSize = 300;
                currentField.drawField(pxlSize, mainCan, ((int) mainCan.getWidth() - pxlSize) / 2, ((int) mainCan.getHeight() - pxlSize) / 2, gc);
        }
    }
}