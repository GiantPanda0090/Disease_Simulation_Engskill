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
    public int stopcounter;
    @FXML
    public void start(ActionEvent actionEvent) {
        //initialization for start button
        int populationInt= Integer.parseInt(population.getText());
        outputBox.setText("Please click on the empty space on the right to choose where has the initial patient been detected. ");
        //generate canvas depends on total population
        if (populationInt < 74 * 80) {
            maxX = (int) Math.floor(Math.sqrt(populationInt));
            maxY = populationInt / maxX;
            display=new Canvas(maxX*10,maxY*10);
            display.setId("display");
            gc = display.getGraphicsContext2D();
            VBox vbox= new VBox();
            HBox pbox=new HBox();
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
            vbox.prefWidth(maxX*10);
            vbox.prefHeight(maxY*10);
            background.setCenter(vbox);
            original =new boolean[maxX][maxY];
            originalT=new int[maxX][maxY];
        } else {
            outputBox.setText("Population size is too big. it should be less that 70*77 ");
            return;
        }
        int initInt= Integer.parseInt(init.getText());
        mouseRec= new int[initInt][2];
        //initialize people get sick and where are they
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
        //end of initialization
        //log input value into log file
        writer.print("Result for Testing value: ");
        writer.println("Total Population: "+ population.getText() +" Infected Probability: "+ infection.getText() + " Days of sickness duration: From "+ Min.getText()+" till "+ Max.getText() +" days.");
        writer.println("Death Probability: "+ death.getText()+" Initial Sickness Porpulation: "+ init.getText());
        //start simulation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                display.setOnMouseEntered(event -> {
                    outputBox.setText("Mouse X: "+(int) event.getX()/10+"Mouse Y: "+(int) event.getY()/10);
                });
                //if total number of initilized people are successfully been input and accepted
                if (counter>=initInt) {
                    display.setOnMouseClicked(null);
                    display.setOnMouseEntered(null);
                    //start a new day per second
                    if (now - lastUpdate >= 1000_000_000) {
                        resetAllDCounter();
                        for (int y = 0; y < maxY; y++) {
                        for (int x = 0; x < maxX; x++) {
                            if (original[x][y] == true) {

                                //death
                                if(death(x,y)){
                                    break;
                                }

                                //healing
                               if (healing(x,y)){
                                    break;
                               }
                                //probability go though neighbor cell(8 of them)
                                if (x > 0 + 1 && y > 0 + 1 && x < maxX - 1 && y < maxY - 1) {
                                    //8 neighbors
                                    boolean[][] copy = original;//copy
                                    for (int diffy = -1; diffy <= 1; diffy++) {
                                        int Ny = y + diffy;
                                        for (int diffx = -1; diffx <= 1; diffx++) {
                                            int Nx = x + diffx;
                                            if (originalT[Nx][Ny] >= 0) {
                                                //infected
                                                stopcounter=0;
                                                infected(Nx,Ny,copy);
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
                        //output
                        outFX();
                        days++;
                        if(infectedCounter==0&&deathCounter==0&&recoverCounter==0&&illCounter==0) {
                            stopcounter++;//quickfix
                        }
                        if (stopcounter> 10){
                            writer.close();
                            tableWriter.close();
                            resetAllDCounter();
                            resetAllOth();
                            this.stop();
                            outputBox.setText(outputBox.getText()+"\n Simulation has done!!");
                        }
                }
                    }
                }


        };

            timer.start();

    }
    public void outFX(){
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
        outputBox.setText(outputBox.getText()+"Red = Infected,Yello = Death, Green = Recovered \n");
        //log output data
        writer.println(outputBox.getText());
        writer.println("======================================================================================");
    }
    //infected method
    public boolean infected(int Nx,int Ny,boolean[][]copy){
        Random rand = new Random();
        double test = rand.nextDouble();
        if (test < Double.parseDouble(infection.getText())) {
            infectedCounter++;
            updateCanvas(Nx * 10, Ny * 10, copy);
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
            gc.fillRect(x * 10, y * 10, 10, 10);
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
            gc.fillRect(x * 10, y * 10, 10, 10);
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
        stopcounter=0;
        days=0;
        deathCounter=0;
        infectedCounter=0;
        recoverCounter=0;
        illCounter=0;
        accumInfecC=0;
        accumDeathcC=0;
        lastUpdate = 0;
        infectSW.setSelected(true);
        deadSW.setSelected(true);
        recoverySW.setSelected(true);
        illSW.setSelected(true);
        tInfectSW.setSelected(true);
        tDeathSW.setSelected(true);
        try {
            writer = new PrintWriter("final_data.txt", "UTF-8");
            tableWriter= new PrintWriter("final_data_table.txt", "UTF-8");
        }catch(Exception e ){
            System.err.println(e);
        }
        //default value
        population.setText("5000");
        infection.setText("0.5");
        Min.setText("2");
        Max.setText("4");
        death.setText("0.2");
        init.setText("1");
        //user mistake elimination
        outputBox.setText("Please input the initial value for each box. \n"+"For probability please use decimal number instead of percentage.\nFor example 0.75 instead of 75 percent. ");
    }
    public void resetAllOth(){
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
            gc.fillRect(xReal, yReal, 10, 10);
            copy[x / 10][y / 10] = true;
       int minT= Integer.parseInt(Min.getText());
       int maxT=Integer.parseInt(Max.getText());
       Random durationR= new Random();
        originalT[x/10][y/10]=durationR.nextInt(maxT-minT) + minT;

    }

}
