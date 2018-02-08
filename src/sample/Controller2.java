package sample;

/*
Controller Class
 */

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by lqsch on  2017-10-01.
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

    @FXML
    public ToggleButton infectSW;
    @FXML
    public ToggleButton deadSW;
    @FXML
    public ToggleButton recoverySW;
    @FXML
    public ToggleButton illSW;
    @FXML
    public ToggleButton tInfectSW;
    @FXML
    public ToggleButton tDeathSW;

    /* non fxml init*/
    PrintWriter writer;
    PrintWriter tableWriter;


    //output
    private Random rand = new Random();
    private int days;
    private int deathCounter;
    private static int infectedCounter;
    private int recoverCounter;
    private int illCounter;
    private int accumInfecC;
    private int accumDeathcC;
    private int effectedCounter;
    private int resetFlag;
    private ArrayList<Integer> xAxispub = new ArrayList<>();
    private ArrayList<Integer> yAxispub = new ArrayList<>();
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
    public int stopcounter;
    @FXML
    public void start(ActionEvent actionEvent) {
        simulation(Double.parseDouble(infection.getText()));
    }


    public void simulation(double infectionprob) {
    start(infectionprob);

    }

    public void start(double infectionprob) {

            //initialization for start button
            int populationInt = Integer.parseInt(population.getText());
            outputBox.setText("Please click on the empty space on the right to choose where has the initial patient been detected. ");
        outputBox.setText(outputBox.getText() + "running"+infectionprob +" round \n ");
        //generate canvas depends on total population
            if (populationInt < 15 * 16) {
                maxX = (int) Math.sqrt(populationInt);
                maxY = populationInt / maxX;
                display = new Canvas(maxX *  50, maxY *  50);
                display.setId("display");
                gc = display.getGraphicsContext2D();
                VBox vbox = new VBox();
                HBox pbox = new HBox();
                StackPane cavasContainer = new StackPane(display);
                cavasContainer.prefWidthProperty().bind(display.widthProperty());
                cavasContainer.prefHeightProperty().bind(display.heightProperty());
                cavasContainer.setAlignment(Pos.CENTER);
                cavasContainer.getStyleClass().add("canvas");
                pbox.getChildren().add(cavasContainer);
                pbox.setAlignment(Pos.CENTER);
                pbox.prefWidthProperty().bind(cavasContainer.widthProperty());
                pbox.prefHeightProperty().bind(cavasContainer.heightProperty());
                vbox.getChildren().add(pbox);
                vbox.setAlignment(Pos.CENTER);
                vbox.prefWidth(maxX *  50);
                vbox.prefHeight(maxY *  50);
                background.setCenter(vbox);
                original = new boolean[maxX][maxY];
                originalT = new int[maxX][maxY];
            } else {
                outputBox.setText("Population size is too big. it should be less that 200 ");
                return;
            }

            int initInt = Integer.parseInt(init.getText());
            mouseRec = new int[initInt][2];
            //initialize people get sick and where are they
            final int[] xAxis = new int[1];
            final int[] yAxis = new int[1];
            int[][] record = new int[initInt][2];
            if (resetFlag == 0) {
                display.setOnMouseClicked(event -> {
                    xAxis[0] = (int) event.getX() /  50;
                    yAxis[0] = (int) event.getY() /  50;
                    xAxis[0] = xAxis[0] ;
                    yAxis[0] = yAxis[0] ;
                    xAxispub.add(xAxis[0]);
                    yAxispub.add(yAxis[0]);
                    boolean[][] copy = original;
                    infected(xAxis[0], yAxis[0], copy,1);
                    original = copy;
                    counter++;
                });
            } else {
                for(int i=0;i<initInt;i++) {
                    xAxis[0] = xAxispub.get(i);
                    yAxis[0] = yAxispub.get(i);
                    boolean[][] copy = original;
                    infected(xAxis[0], yAxis[0], copy, 1);
                    original = copy;
                    counter++;
                }
            }
            //end of initialization
            //log input value into log file
            //start simulation
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    display.setOnMouseEntered(event -> {
                        outputBox.setText("Mouse X: " + (int) event.getX() /  50 + "Mouse Y: " + (int) event.getY() /  50);
                    });
                    //if total number of initilized people are successfully been input and accepted
                    if (counter >= initInt) {
                        display.setOnMouseClicked(null);
                        display.setOnMouseEntered(null);
                        //start a new day per second
                        if (now - lastUpdate >= 1000_000_00) {
                            resetAllDCounter();

                            for (int y = 0; y < maxY; y++) {
                                for (int x = 0; x < maxX; x++) {
                                    if (original[x][y] == true) {
                                        //death
                                        if (death(x, y)) {
                                            break;
                                        }

                                        //healing
                                        if (healing(x, y)) {
                                            break;
                                        }
                                        //probability go though neighbor cell(8 of them)
                                        if (x > 0  && y > 0 && x < maxX - 1 && y < maxY - 1) {
                                            //8 neighbors
                                            boolean[][] copy = original;//copy
                                            for (double diffy = -1; diffy <= 1; diffy++) {
                                                double Ny = y + diffy;
                                                for (double diffx = -1; diffx <= 1; diffx++) {
                                                    double Nx = x + diffx;
                                                    if (originalT[(int)Nx][(int)Ny] == 0) {
                                                        //infected
                                                        stopcounter = 0;
                                                        infected((int)Nx,(int) Ny, copy, infectionprob);
                                                    }
                                                }
                                            }
                                            original = copy;
                                        }
                                    }
                                }
                            }
                            lastUpdate = now;
                            illCounter = illCounter + infectedCounter - deathCounter - recoverCounter;
                            effectedCounter = effectedCounter + infectedCounter;
                            accumDeathcC = accumDeathcC + deathCounter;

                            //output
                            outFX(populationInt);
                            days++;
                            infectedCounter = 0;
                            if (infectedCounter == 0 && deathCounter == 0 && recoverCounter == 0 && illCounter == 0) {
                                stopcounter++;//quickfix
                            }
                            //terminate
                            if (stopcounter > 1) {
                                if(infectionprob<=1) {
                                    writer.println(infectionprob + " " + accumInfecC );
                                }
                                resetAllDCounter();
                                resetAllOth();
                                this.stop();
                                resetFlag = 1;
                                if(infectionprob<=1) {
                                    Controller2.this.start(infectionprob + 0.1);
                                }else {
                                    resetAllDCounter();
                                    resetAllOth();
                                    writer.close();
                                    outputBox.setText(outputBox.getText() + "\n Simulation has done!!");
                                    this.stop();
                                    xAxispub.clear();
                                    yAxispub.clear();
                                    resetFlag=0;
                                }
                            }
                        }
                    }
                }


            };

            timer.start();
return;
    }
    public void outFX(int populationInt){
        outputBox.setText("Day "+ days+" - \n");
        tableWriter.println(" - ");
        tableWriter.print(" | ");
        tableWriter.println(days+" | ");

        if(infectSW.isSelected()){
            outputBox.setText(outputBox.getText()+infectedCounter+" people has been infected today; \n");
            tableWriter.println(" - ");
            tableWriter.println(infectedCounter+" | ");
        }
        if(deadSW.isSelected()){
            outputBox.setText(outputBox.getText()+deathCounter+" people has dead today; \n");
            tableWriter.println(" - ");
            tableWriter.println(deathCounter+" | ");
        }
        if(recoverySW.isSelected()){
            outputBox.setText(outputBox.getText()+recoverCounter+" people has been recovered today; \n");
            tableWriter.println(" - ");
            tableWriter.println(recoverCounter+" | ");

        }
        if(illSW.isSelected()){
            outputBox.setText(outputBox.getText()+illCounter+" people are still ill today; \n" );
            tableWriter.println(" - ");
            tableWriter.println(illCounter+" | ");


        }
        if(tInfectSW.isSelected()){
            outputBox.setText(outputBox.getText()+accumInfecC+" total amount of people has infected until today \n" );
            tableWriter.println(" - ");
            tableWriter.println(accumInfecC+" | ");

        }
        if(tDeathSW.isSelected()){
            outputBox.setText(outputBox.getText()+accumDeathcC+" total amount of people has dead until today \n");
            tableWriter.println(" - ");
            tableWriter.println(accumDeathcC+" | ");

        }
        if (accumInfecC>populationInt/2){
            outputBox.setText(outputBox.getText()+"EPIDEMIC!!!!!!!!!\n");
        }
        outputBox.setText(outputBox.getText()+effectedCounter+" people effected until today\n");
        outputBox.setText(outputBox.getText()+"Red = Infected,Yello = Death, Green = Recovered \n");
        //log output data
    }
    //infected method
    public boolean infected(int Nx,int Ny,boolean[][]copy,double infectionprob){
        double test = rand.nextDouble();
        if (test < infectionprob) {
            infectedCounter++;
            accumInfecC++;
            updateCanvas(Nx *  50, Ny *  50, copy);
            return true;
        }
        return false;
    }

    //death method
    public boolean death(int x,int y){
        //death
        Random dRand = new Random();
        double dTest = dRand.nextDouble();
        if (dTest < Double.parseDouble(death.getText())) {
            originalT[x][y] = -2;
            original[x][y] = false;
            deathCounter++;
            gc.setFill(Color.YELLOW);
            gc.fillRect(x *  50, y *  50,  50,  50);
            return true;
        }
        return false;
    }

    //healing method
    public boolean healing(int x, int y){
        originalT[x][y] = originalT[x][y] - 1;
        if (originalT[x][y] == 0) {
            original[x][y] = false;
            originalT[x][y] = -1;
            recoverCounter++;
            gc.setFill(Color.GREEN);
            gc.fillRect(x *  50, y *  50,  50,  50);
            //gc.clearRect(x* 10,y* 10,10,10);
            return true;
        }
        return false;
    }

    //before pressing the start button
    //initial state
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //geneneral initialization for overall application
        resetFlag=0;
        stopcounter=0;
        days=0;
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
        illCounter=0;
        accumInfecC=0;
        accumDeathcC=0;
        lastUpdate = 0;
        effectedCounter=0;
        infectSW.setSelected(true);
        deadSW.setSelected(true);
        recoverySW.setSelected(true);
        illSW.setSelected(true);
        tInfectSW.setSelected(true);
        tDeathSW.setSelected(true);
        try {
            writer = new PrintWriter("final_data.dat", "UTF-8");
            tableWriter= new PrintWriter("final_data_table.txt", "UTF-8");
        }catch(Exception e ){
            System.err.println(e);
        }
        //default value
        population.setText("200");
        infection.setText("0");
        Min.setText("2");
        Max.setText("4");
        death.setText("0.2");
        init.setText("1");
        //user mistake elimination
        outputBox.setText("Please input the initial value for each box. \n"+"For probability please use decimal number instead of percentage.\nFor example 0.75 instead of 75 percent. ");
    }
    public void resetAllOth(){
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
       illCounter=0;
        accumInfecC=0;
        accumDeathcC=0;
        counter=0;

       lastUpdate=0;
        original=null;
        originalT=null;
        Canvas display=null;
        stopcounter=0;
        original=null;
        originalT=null;
        mouseRec=null;
        maxX=0;
        maxY=0;
        lastUpdate=0;
        counter=0;
        stopcounter=0;
        accumDeathcC=0;
        accumInfecC=0;
        effectedCounter=0;
        days=0;


    }
    //reset the counters for yesterday
    public void resetAllDCounter(){
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
        illCounter=0;
    }

    //gui update per change
    public void updateCanvas(int x,int y,boolean[][]copy){
            final int xReal = x;
            final int yReal = y;
            gc.setFill(Color.RED);
            gc.fillRect(xReal, yReal,  50,  50);
            copy[x /  50][y /  50] = true;
       int minT= Integer.parseInt(Min.getText());
       int maxT=Integer.parseInt(Max.getText());
       Random durationR= new Random();
        originalT[x/ 50][y/ 50]=durationR.nextInt(maxT-minT) + minT;

    }

}
