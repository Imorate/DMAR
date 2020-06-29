package ir.imorate;

import ir.imorate.Utils.DataSetFile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PrimaryController {

    public FileChooser fileChooser;
    @FXML
    public HBox dataSetHBox;
    public Button openDataSetButton;
    public File dataSetFile;
    public TextField dataSetTextField;
    public Label dataSetRecordCount;
    public TextFlow dataSetRecordTextFlow;
    public Button calculateButton;
    public TextField minSupportTextField;
    public TextField minConfidenceTextField;
    public Label statusLabel;
    public ListView<String> resultListView;

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void chooseDataSetFile() {
        dataSetFile = initiateDataSetFile();
        if (dataSetFile != null) {
            if (dataSetFile.canRead()) {
                dataSetTextField.setText(dataSetFile.getName());
                dataSetRecordsHandler();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, dataSetFile.getName() + " file has not read permission.", ButtonType.CLOSE);
                alert.setHeaderText("File read permission error!");
                alert.show();
            }
        }
    }

    @FXML
    public void calculate() {
        if (minConfidenceTextField.getText().isEmpty() || minSupportTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Min support or Min confidence cant be empty", ButtonType.CLOSE);
            alert.setHeaderText("Apriori calculation error");
            alert.show();
        } else if (dataSetFile != null) {
            resultListView.getItems().clear();
            statusLabel.setText("Parsing DataSet...");
            DataSetParser dataSetParser = new DataSetParser(dataSetFile);
            statusLabel.setText("Initializing Apriori algorithm...");
            Apriori apriori = new Apriori(dataSetParser.toList(), Integer.parseInt(minSupportTextField.getText()));
            statusLabel.setText("Generating item sets...");
            Map<List<List<String>>, Double> resultMap = apriori.calculate();
            for (Map.Entry<List<List<String>>, Double> entry : resultMap.entrySet()) {
                if (entry.getValue() >= Integer.parseInt(minConfidenceTextField.getText())) {
                    resultListView.getItems().add(String.join(", ", entry.getKey().get(0)) + " → " +
                            String.join(", ", entry.getKey().get(1)));
                }
            }
            statusLabel.setText("Calculation has been successfully done!");
        }
    }

    private File initiateDataSetFile() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Dataset");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return fileChooser.showOpenDialog(dataSetHBox.getScene().getWindow());
    }

    private void dataSetRecordsHandler() {
        try {
            int lineCount = DataSetFile.countLines(dataSetFile);
            dataSetRecordCount.setText(String.valueOf(lineCount));
            for (Node node : dataSetRecordTextFlow.getChildren()) {
                if (lineCount != 0) {
                    node.setStyle("-fx-text-fill: green");
                    dataSetTextField.setStyle("-fx-border-color: green");
                } else {
                    node.setStyle("-fx-text-fill: red");
                    dataSetTextField.setStyle("-fx-border-color: red");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
