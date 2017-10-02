//redundant class

package sample;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class ControllerBackup implements Initializable {
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
    public Canvas display;


    public GraphicsContext gc;
    public int maxY;
    public int maxX;
    public boolean Xover;
    public boolean Yover;
    public AnimationTimer spawn;
    public int initInt;
    public int counter;

    private ArrayList<int[]> FramUpgrade;
    private boolean[][] record;

    @FXML
    public void start(ActionEvent actionEvent){
        //calculations
        counter=0;
        int populationInt= Integer.parseInt(population.getText());
        initInt= Integer.parseInt(init.getText());
        populationInt=populationInt-initInt;
        if (populationInt < 70 * 77) {
            maxX = (int) Math.floor(Math.sqrt(populationInt* 100));
            maxY = populationInt* 100 / maxX;
            record=new boolean[maxX][maxY];
        } else {
            outputBox.setText("Population size is too big. it should be less that 70*77 ");
            return;
        }

        display.setOnMouseClicked(event -> {
            int xAxis=(int) Math.floor(event.getX());
            int yAxis=(int) Math.floor(event.getY());
            FramUpgrade.add(new int[]{xAxis,yAxis});
            // System.out.println(event.getSceneX());
            //System.out.println(event.getSceneY());
            counter++;
        });


            spawn = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (counter>initInt-1){
                        display.setOnMouseClicked(null);
                    }
                      //animated action
                    if(FramUpgrade.size()>0) {
                        for (int i = FramUpgrade.size() - 1; i >= 0; i--) {
                            // System.out.println(FramUpgrade.size());
                            int[] coord = FramUpgrade.get(i);
                            if(coord!=null){
                            updateCanvas(coord[0] / 10, coord[1] / 10);
                            sick(coord[0] / 10, coord[1] / 10);
                            }
                            if(i<FramUpgrade.size()) {
                                FramUpgrade.remove(i);
                            }
                        }
                    }

                    }


            };
            spawn.start();

    }
    public void recover(int x, int y){
        if(x>maxX){
            System.err.println("ERROR X VALUE IS TOO BIG");
            Xover=true;
            return;
        }
        if (y>maxY){
            System.err.println("ERROR Y VALUE IS TOO BIG");
            Yover=true;
            return;
        }
        int xReal=x*10 ;
        int yReal=y*10;
        gc.clearRect(xReal,yReal,10,10);
    }
    public void updateCanvas(int x,int y){

            final int xReal = x * 10;
            final int yReal = y * 10;
            this.gc.setFill(Color.RED);
            this.gc.fillRect(xReal, yReal, 10, 10);
            record[x][y] = true;

    }
 public void sick(int x, int y){

         final int xReal = x * 10;
         final int yReal = y * 10;
      /*  //System.out.println(xReal+ ", "+yReal);
     this.gc.setFill(Color.RED);
     this.gc.fillRect(xReal,yReal,10,10);*/


         Task<Void> cell = new Task<Void>() {
             @Override
             protected Void call() throws Exception {

                 for (int i = 0; i < 8; i++) {
                     Random rand = new Random();
                     double test = rand.nextDouble();
                     if (test < Double.parseDouble(infection.getText())) {
                         int xAxis = xReal;
                         int yAxis = yReal;
                         if (i == 0) {
                             xAxis = xAxis - 10;
                             yAxis = yAxis - 10;
                         } else if (i == 1) {
                             yAxis = yAxis - 10;
                         } else if (i == 2) {
                             xAxis = xAxis + 10;
                             yAxis = yAxis - 10;
                         } else if (i == 3) {
                             xAxis = xAxis + 10;
                         } else if (i == 4) {
                             xAxis = xAxis + 10;
                             yAxis = yAxis + 10;
                         } else if (i == 5) {
                             yAxis = yAxis + 10;
                         } else if (i == 6) {
                             xAxis = xAxis - 10;
                             yAxis = yAxis + 10;
                         } else if (i == 7) {
                             xAxis = xAxis - 10;
                         }
                         if (xAxis < maxX && yAxis < maxY) {
                             if (record[xAxis][yAxis] != true) {
                                 int[] coord = new int[]{xAxis, yAxis};
                                 FramUpgrade.add(coord);
                             }
                         }
                     }
                 }

                 return null;
             }

         };
         new Thread(cell).start();


 }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     counter =0;
        gc = display.getGraphicsContext2D();
         FramUpgrade= new ArrayList();

         Xover=false;
         Yover=false;

    }
}
