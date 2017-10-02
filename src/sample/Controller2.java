package sample;

/*
Controller Class
 */

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by lqsch on 2017-10-01.
 */
public class Controller2 implements Initializable {
    @FXML
    public TextField init;

    @FXML
    public TextField death;

    @FXML
    public TextField Min;
    @FXML
    public TextField Max;

    @FXML
    public TextField infection;

    @FXML
    public TextField population;

    @FXML
    public TextArea outputBox;

    @FXML
    public BorderPane background;

    private int days;
    private int deathCounter;
    private int infectedCounter;
    private int recoverCounter;
    private int illCounter;
    private int accumInfecC;
    private int accumDeathcC;
    public GraphicsContext gc;
    public int maxY;
    public int maxX;
    public boolean Xover;
    public boolean Yover;

    public int initInt;
    public int counter;

    private long lastUpdate;
    boolean[][] original;
    int[][] originalT;
    Canvas display;
    int[][] mouseRec;
    @FXML
    public void start(ActionEvent actionEvent) {
        int populationInt= Integer.parseInt(population.getText());
        if (populationInt < 74 * 80) {
            maxX = (int) Math.floor(Math.sqrt(populationInt));
            maxY = populationInt / maxX;
            display=new Canvas(maxX*10,maxY*10);
            display.setId("display");
            gc = display.getGraphicsContext2D();
            background.setCenter(display);
            original =new boolean[maxX][maxY];
            originalT=new int[maxX][maxY];
        } else {
            outputBox.setText("Population size is too big. it should be less that 70*77 ");
            return;
        }
        int initInt= Integer.parseInt(init.getText());
        mouseRec= new int[initInt][2];

        final int[] xAxis = new int[1];
        final int[] yAxis = new int[1];
        int[][] record=new int[initInt][2];
        display.setOnMouseClicked(event -> {
             xAxis[0] =(int) event.getX()/10;
             yAxis[0] =(int) event.getY()/10;
             xAxis[0]=xAxis[0]*10;
             yAxis[0]=yAxis[0]*10;
            boolean[][] copy =original;
            infectedCounter++;
            updateCanvas(xAxis[0], yAxis[0],copy);
            original=copy;
            counter++;
        });
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (counter>=initInt) {
                    display.setOnMouseClicked(null);
                    //probability go though neighbor cell
                    if (now - lastUpdate >= 1000_000_000) {
                        resetAllDCounter();
                        for (int y = 0; y < maxY; y++) {
                        for (int x = 0; x < maxX; x++) {
                            if (original[x][y] == true) {
                                //death
                                Random dRand = new Random();
                                double dTest = dRand.nextDouble();
                                if (dTest < Double.parseDouble(death.getText())) {
                                    originalT[x][y] = -2;
                                    original[x][y] = false;
                                    deathCounter++;
                                    gc.setFill(Color.YELLOW);
                                    gc.fillRect(x * 10, y * 10, 10, 10);
                                    break;
                                }

                                //healing
                                originalT[x][y] = originalT[x][y] - 1;
                                if (originalT[x][y] == 0) {
                                    original[x][y] = false;
                                    originalT[x][y] = -1;
                                    recoverCounter++;
                                    gc.setFill(Color.GREEN);
                                    gc.fillRect(x * 10, y * 10, 10, 10);
                                    //gc.clearRect(x* 10,y* 10,10,10);
                                    break;
                                }
                                if (x > 0 + 1 && y > 0 + 1 && x < maxX - 1 && y < maxY - 1) {
                                    //8 neighbors
                                    boolean[][] copy = original;//copy
                                    for (int diffy = -1; diffy <= 1; diffy++) {
                                        int Ny = y + diffy;
                                        for (int diffx = -1; diffx <= 1; diffx++) {
                                            int Nx = x + diffx;
                                            if (originalT[Nx][Ny] >= 0) {
                                                Random rand = new Random();
                                                double test = rand.nextDouble();
                                                if (test < Double.parseDouble(infection.getText())) {
                                                    infectedCounter++;
                                                    updateCanvas(Nx * 10, Ny * 10, copy);
                                                }
                                            }
                                        }
                                    }
                                    original = copy;
                                }
                            }
                        }
                    }
                        lastUpdate = now ;
                        illCounter=illCounter+infectedCounter-deathCounter-recoverCounter;
                        accumInfecC=accumInfecC+infectedCounter;
                        accumDeathcC=accumDeathcC+deathCounter;
                        outputBox.setText("Day "+ days +" - \n "+ deathCounter+" people has dead; \n"+infectedCounter+" people has been infected today; \n"+ recoverCounter+" people has been recovered today; \n"+illCounter+" people are still ill today; \n" +accumInfecC+" total amount of people has infected until today \n"+accumDeathcC+" total amount of people has dead until today \n");
                        days++;
                }
                    }
                }


        };

            timer.start();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        days=0;
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
        illCounter=0;
        accumInfecC=0;
        accumDeathcC=0;
        lastUpdate = 0;
    }
    public void resetAllDCounter(){
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
        illCounter=0;
    }

    public void updateCanvas(int x,int y,boolean[][]copy){
            final int xReal = x;
            final int yReal = y;
            gc.setFill(Color.RED);
            gc.fillRect(xReal, yReal, 10, 10);
            copy[x / 10][y / 10] = true;
       int minT= Integer.parseInt(Min.getText());
       int maxT=Integer.parseInt(Max.getText());
       Random durationR= new Random();
        originalT[x/10][y/10]=durationR.nextInt(maxT-minT) + minT;

    }

}
