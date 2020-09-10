/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itemsizecheck;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author jack1
 */
public class FXMLDocumentController implements Initializable {
    //create a new alert
    Alert a = new Alert(Alert.AlertType.NONE);
    double inputLength;
    double inputWidth;
    double inputHeight;
    final double margin = 50;
    ArrayList<Double> arr = new ArrayList<>();
    
    @FXML 
    private Button checkButton;
    
    @FXML
    private Line l1;
    @FXML
    private Line w1;
    @FXML
    private Line w2;
    @FXML
    private Line l2;
    @FXML
    private Line h2;
    @FXML
    private Line h1;
    @FXML
    private Line l3;
    @FXML
    private Line w3;
    @FXML
    private Line l4;
    @FXML
    private Line h3;
    @FXML
    private Line w4;
    @FXML
    private TextField lengthInput;
    @FXML
    private TextField widthInput;
    @FXML
    private Line h4;
    @FXML
    private TextField heightInput;
    @FXML
    private AnchorPane measurePane;
    @FXML
    private Label lengthWarning;
    @FXML
    private Label widthWarning;
    @FXML
    private Label heightWarning;
    
    @FXML
    private AnchorPane resultPane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ToggleButton history;
    @FXML
    private AnchorPane historyPane;
    @FXML
    private TableColumn<Histories, Integer> fid;
    @FXML
    private TableColumn<Histories, String> flength;
    @FXML
    private TableColumn<Histories, String> fwidth;
    @FXML
    private TableColumn<Histories, String> fheight;
    @FXML
    private TableColumn<Histories, String> fresult;
    @FXML
    private TableView<Histories> fhistories;
    
    static int count = 0;
    ObservableList<Histories>list = FXCollections.observableArrayList();
    String checkResult;
    
    @FXML
    private AnchorPane noresultPane;
    @FXML
    private StackPane stackPane;
    
    boolean runOnce = true;
    @FXML
    private Label bagName;
    @FXML
    private AnchorPane demoPane;

    @FXML
    private ImageView BagImage;
    
    @FXML
    private void linkRedirect(ActionEvent event) throws URISyntaxException{
        if(Desktop.isDesktopSupported())
    {
        try {
            Desktop.getDesktop().browse(new URI("https://www.google.co.nz"));
        } catch (IOException | URISyntaxException e1) {
        }
    }
    }
    
    
    @FXML
    private void checkSize(ActionEvent event) throws IOException{
        
        if (!lengthInput.getText().isEmpty() && !heightInput.getText().isEmpty() && !widthInput.getText().isEmpty()){
            clearWarnings();
            clearRedLines();
            
            //run the animation once only
            if (runOnce){
                 //result pane eases in
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue (stackPane.translateXProperty(),demoPane.getWidth(),Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame (Duration.seconds(1),kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();               
                runOnce = false;
            }
            
           
            
            inputLength = Double.parseDouble(lengthInput.getText());
            inputWidth = Double.parseDouble(widthInput.getText());
            inputHeight = Double.parseDouble(heightInput.getText());
            arr.add(inputLength);
            arr.add(inputWidth);
            arr.add(inputHeight);
            Collections.sort(arr);
            
            Box b = new Box (inputLength, inputWidth, inputHeight);
            //found result    
            if (isFit(b)){
                
                 resultPane.setVisible(true);
                 noresultPane.setVisible(false);
            }
            //no result found
            else{
                resultPane.setVisible(false);
                noresultPane.setVisible(true);
            }
            
             //add the data into the history tableview
                count++;
                String l = lengthInput.getText();
                String w = widthInput.getText();
                String ht = heightInput.getText();
                Histories h = new Histories(count,l,w,ht,checkResult);
                
                //sort in reverse order.
                list.add(h);                  
                Collections.sort(list, (left, right) -> right.getID()-left.getID() );
                  
                fid.setCellValueFactory(new PropertyValueFactory<>("ID"));
                flength.setCellValueFactory(new PropertyValueFactory<>("length"));
                fheight.setCellValueFactory(new PropertyValueFactory<>("height"));
                fwidth.setCellValueFactory(new PropertyValueFactory<>("width"));
                fresult.setCellValueFactory(new PropertyValueFactory<>("result"));
                fhistories.setItems(list);
                
            //clear arrayList
            arr.clear();
        }
        
        //input not valid. Alert pops up
        else{
            a.setAlertType(Alert.AlertType.ERROR);
            a.setTitle("Error occurs");
            a.setContentText("Please complete all the fields!");
            a.show();
        }
    }
    
    @FXML 
    private void exportCSV(ActionEvent event){
         try {
                writeExcel();
              }
              catch (IOException ex) {
        }
    }
    
    void writeExcel() throws IOException{
        Writer writer = null;
        try{
            
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Export csv");
            filechooser.setInitialFileName("Export search result");
            FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("CSV files(*.csv)","*.csv");
            filechooser.getExtensionFilters().add(extension);
            File file = filechooser.showSaveDialog(historyPane.getScene().getWindow()); 
            if (file !=null){
            writer = new BufferedWriter(new FileWriter(file));
            String header = "ID, Height(cm), Width(cm), Length(cm), Result \n";
            writer.write(header);
            for (Histories h : list){
                String text = h.getID()+", "+h.getHeight()+", "+h.getWidth()
                        +", "+h.getLength()+", "+checkResult+"\n";
                writer.write(text);
            }
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("CSV exported");
            a.setContentText("csv file has been saved");
            a.show();
            }
        }
        catch(IOException e){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setTitle("Error occurred");
            a.setContentText("Please close your open csv file, thank you!");
            a.show();
        }
        finally{
            writer.flush();
            writer.close();
            
        }
    }
    
    @FXML 
    private void clearAll(ActionEvent event){
        list.clear();
        fhistories.setItems(list); 
        //refresh count
        count=0;
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("All cleared");
        a.setContentText("Table cleared");
        a.show();
        
        
    }
    
    @FXML
    private void hideShowHistory(ActionEvent event) throws IOException{
        
        //show the history pane
        if (history.isSelected()){
        historyPane.setVisible(true);
        TranslateTransition t = new TranslateTransition (Duration.seconds(1),historyPane);       
        t.setToY(-500);
        
        FadeTransition fd = new FadeTransition ();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(historyPane);
        fd.setFromValue(0);
        fd.setToValue(1);
        
        ParallelTransition seqT = new ParallelTransition (t,fd);  
        seqT.play();
        history.setText("Hide History");
        }
        
        //hide history pane
        else{
        TranslateTransition t = new TranslateTransition (Duration.seconds(1),historyPane);    
        t.setToY(500);
        
        FadeTransition fd = new FadeTransition ();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(historyPane);
        fd.setFromValue(1);
        fd.setToValue(0);
        
        ParallelTransition seqT = new ParallelTransition (t,fd);  
        seqT.play();
        history.setText("Show History");
        }
    }
    
    public boolean isFit(Box box){
        
        for (BagSize bs: BagSize.values()){
            //vertically check andhorizontally check
            if (arr.get(2)+arr.get(0)+margin<bs.getLongLength() &&
                arr.get(1)+arr.get(0)+margin<bs.getShortLength())
            {
                
                bagName.setText(bs.name());
                checkResult = bs.name();
                BagImage.setImage(new Image("/resource/"+bs.name()+".png"));
             
                return true;
                
            }
            
            
        }
        checkResult = "No result";
        return false;
      
    }
    
    @FXML
    private void exit(ActionEvent event) throws InterruptedException{     
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure to exit?"); 
        a.setTitle("Exit now?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(okButton, noButton);
        Optional<ButtonType> result = a.showAndWait();
            if (result.get() == okButton){
                
                //fade out effect. If the animation finishes, exit the program.
                //use platform.exit rather than system.exit for javafx project.
                FadeTransition fd = new FadeTransition ();
                fd.setDuration(Duration.millis(1000));
                fd.setNode(mainPane);
                fd.setFromValue(1);
                fd.setToValue(0);
                fd.setOnFinished((ae)->Platform.exit());
                fd.play();                              
            } 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      fadeIn(measurePane);
      mainPane.setBackground(Background.EMPTY);
     
     
      
      lengthInput.focusedProperty().addListener((ov, oldV, newV)->{         
          //if lost focus on the textarea field
          if(!newV){
              
              if (isValid(lengthInput.getText())){
                  this.lengthInput.setText(lengthInput.getText());
                   lengthInput.setStyle("-fx-text-box-border: #FAEBD7");
                   this.lengthWarning.setVisible(false);
              }
             
              else{
                  this.lengthInput.setStyle("-fx-text-box-border: #ff0000");
                  this.lengthWarning.setVisible(true);
                  this.lengthInput.setText("");
              }      
          }
          
          //on focus 
          else{
              clearRedLines();
              l1.setStroke(RED);
              l2.setStroke(RED);
              l3.setStroke(RED);
              l4.setStroke(RED);
              l1.setStrokeWidth(3);
              l2.setStrokeWidth(3);
              l3.setStrokeWidth(3);
              l4.setStrokeWidth(3);
          }
      });
      
      widthInput.focusedProperty().addListener((ov, oldV, newV)->{         
          //if lost focus on the textarea field
          if(!newV){
              
              if (isValid(widthInput.getText())){
                  this.widthInput.setText(widthInput.getText());
                  widthInput.setStyle("-fx-text-box-border: #FAEBD7");
                  this.widthWarning.setVisible(false);
              }
              
              else{
                  this.widthInput.setStyle("-fx-text-box-border: #ff0000");
                  this.widthWarning.setVisible(true);
                  this.widthInput.setText("");
              }      
          }
          
          else{
              clearRedLines();
              w1.setStroke(RED);
              w2.setStroke(RED);
              w3.setStroke(RED);
              w4.setStroke(RED);
              w1.setStrokeWidth(3);
              w2.setStrokeWidth(3);
              w3.setStrokeWidth(3);
              w4.setStrokeWidth(3);
          }
      });
      
      heightInput.focusedProperty().addListener((ov, oldV, newV)->{         
          //if lost focus on the textarea field
          if(!newV){
              
              if (isValid(heightInput.getText())){
                  this.heightInput.setText(heightInput.getText());
                  heightInput.setStyle("-fx-text-box-border: #FAEBD7");
                  this.heightWarning.setVisible(false);
              }
              
              else{
                  this.heightInput.setStyle("-fx-text-box-border: #ff0000");
                  this.heightWarning.setVisible(true);
                  this.heightInput.setText("");
              }      
          }
          
          
          else{
              clearRedLines();
              h1.setStroke(RED);
              h2.setStroke(RED);
              h3.setStroke(RED);
              h4.setStroke(RED);
              h1.setStrokeWidth(3);
              h2.setStrokeWidth(3);
              h3.setStrokeWidth(3);
              h4.setStrokeWidth(3);
          }
          
      });
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
    }
    
    private boolean isValid(String value) {
     
      if (value.isEmpty()){
          return true;
      }
      try {
         Double.parseDouble(value);
         return true;
      } catch (NumberFormatException ex) {
         return false;
      }
   }
    
    public void clearWarnings(){
        heightInput.setStyle("-fx-text-box-border: #FAEBD7");
        widthInput.setStyle("-fx-text-box-border: #FAEBD7");
        lengthInput.setStyle("-fx-text-box-border: #FAEBD7");
        this.heightWarning.setVisible(false);
        this.widthWarning.setVisible(false);
        this.lengthWarning.setVisible(false);
        
    }
    public void clearRedLines(){
        w2.setStroke(BLACK);
        w3.setStroke(BLACK);
        w1.setStroke(BLACK);
        w4.setStroke(BLACK);
        h2.setStroke(BLACK);
        h1.setStroke(BLACK);
        h3.setStroke(BLACK);
        h4.setStroke(BLACK);
        l2.setStroke(BLACK);
        l3.setStroke(BLACK);
        l1.setStroke(BLACK);
        l4.setStroke(BLACK);
        h1.setStrokeWidth(1);
        h2.setStrokeWidth(1);
        h3.setStrokeWidth(1);
        h4.setStrokeWidth(1);
        w1.setStrokeWidth(1);
        w2.setStrokeWidth(1);
        w3.setStrokeWidth(1);
        w4.setStrokeWidth(1);
        l1.setStrokeWidth(1);
        l2.setStrokeWidth(1);
        l3.setStrokeWidth(1);
        l4.setStrokeWidth(1);
        
    }


    public void fadeIn(AnchorPane Pane){
        FadeTransition fd = new FadeTransition ();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(Pane);
        fd.setFromValue(0);
        fd.setToValue(1);
        fd.play();
    } 
    
    public void fadeOut(AnchorPane Pane){
        FadeTransition fd = new FadeTransition ();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(Pane);
        fd.setFromValue(1);
        fd.setToValue(0);
        fd.play();
    }  
    
    
}
