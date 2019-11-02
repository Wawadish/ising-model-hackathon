import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ColorPane extends Pane {
    public ColorPane(double width, double height){
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
    }
    public void color(){
        if(this.getStyle().equals("-fx-background-color: black")){
            this.setStyle("-fx-background-color: white");
        }else{
            this.setStyle("-fx-background-color: black");
        }
    }
}
