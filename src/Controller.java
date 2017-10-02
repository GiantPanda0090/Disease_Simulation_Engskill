/*
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
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

*/
/**
 * Created by lqsch on 2017-10-01.
 *//*

public class Controller implements Initializable {
    @FXML
    public TextField init;

    @FXML
    public TextField death;

    @FXML
    public TextField duration;

    @FXML
    public TextField infection;

    @FXML
    public TextField population;

    @FXML
    public TextArea outputBox;

    @FXML
    public BorderPane background;


    public GraphicsContext gc;
    public int maxY;
    public int maxX;
    public boolean Xover;
    public boolean Yover;

    public int initInt;
    public int counter;

    Task<Boolean>[][] mapping;
    boolean[][] index;
    Canvas display;
    int[][] mouseRec;

//probability for each cell
    Task<Integer[]> cell (int x, int y){
            Random rand = new Random();
            double test = rand.nextDouble();
            if(test < Double.parseDouble(infection.getText())){
                updateCanvas(x, y);
                active(x, y);
                return null;
            }else{
                cell(x,y);
            }
        updateCanvas(x, y);
        active(x, y);
        return null;//infected
        }
private AnimationTimer timer;
    @FXML
    public void start(ActionEvent actionEvent){
        int populationInt= Integer.parseInt(population.getText());
        if (populationInt < 74 * 80) {
            maxX = (int) Math.floor(Math.sqrt(populationInt));
            maxY = populationInt / maxX;
            display=new Canvas(maxX*10,maxY*10);
            display.setId("display");
            gc = display.getGraphicsContext2D();
            background.setCenter(display);
            mapping = new Task[maxX][maxY];
            index =new boolean[maxX][maxY];


        } else {
            outputBox.setText("Population size is too big. it should be less that 70*77 ");
            return;
        }
       int initInt= Integer.parseInt(init.getText());
        mouseRec= new int[initInt][2];

        display.setOnMouseClicked(event -> {
            int xAxis=(int) Math.floor(event.getX());
            int yAxis=(int) Math.floor(event.getY());
            updateCanvas(xAxis,yAxis);
            mouseRec[counter][0]=xAxis/10*10;
            mouseRec[counter][1]=yAxis/10*10;
            counter++;
            if (counter==initInt) {
                display.setOnMouseClicked(null);
                for (int i = 0; i < counter; i++) {
                    active(mouseRec[i][0],mouseRec[i][1]);
                }

            };
        });
                //inital


        }

public void active(int x,int y) {
    int testCounter =0;
    int loopY = y/10 - 1;
    int loopX = x/10 - 1;
    final int limitX=loopX+2;
    final int limitY=loopY+2;
    //loop though the cell (8 of them) around the current active cell
    Thread myThreads[] = new Thread[8];
    while (loopY <= limitY) {
        loopX=limitX-2;
        while (loopX <= limitX) {
            if (index[loopX][loopY] != true){
            Task<Integer[]> currentT = cell(loopX*10,loopY*10);
                myThreads[testCounter] = new Thread(currentT);
                myThreads[testCounter].start();
            */
/*currentT.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (newValue) {
                        updateCanvas(finalLoopX , finalLoopY );
                        active(finalLoopX , finalLoopY );
                    }
                    System.out.println("Result for cell "+finalLoopX+", "+finalLoopY+" at coordinate "+finalLoopX*10+", "+finalLoopY*10 +" is :"+ newValue);
                }

            });*//*

            }
            testCounter++;
            loopX++;
        }
        loopY++;
    }

    for(testCounter=0;testCounter<myThreads.length;testCounter++){
        try {
            myThreads[testCounter].join();
        }catch (Exception e){
            System.out.println(e);
        }
    }
System.out.println("active has looped "+ testCounter+" times");
   */
/* final Boolean[] result = new Boolean[1];
    int finalLoopX = loopX;
    int finalLoopY = loopY;
    currentT.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent t) {
            result[0] = currentT.getValue();
            if (result[0]) {
                updateCanvas(finalLoopX*10, finalLoopY*10);
            }
        }
    });*//*

}
    public void updateCanvas(int x,int y){
        final int xReal = x ;
        final int yReal = y ;
        gc.setFill(Color.RED);
        gc.fillRect(xReal, yReal, 10, 10);
        index[x/10][y/10]=true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        counter = 0;
        Xover = false;
        Yover = false;

    }
}*/
