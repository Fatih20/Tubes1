import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The InputFrameController class.  It controls input from the users and validates it.
 * If validation is successful, the Adjacency game screen will pop up in a different window.
 *
 * @author Jedid Ahn
 */
public class InputFrameController {

    public CheckBox isBotFirst;
    @FXML
    private ComboBox<String> player1;

    @FXML
    private ComboBox<String> player2;

    @FXML
    private ComboBox<String> numberOfRounds;


    /**
     * Initialize the dropdown ComboBox with a list of items that are allowed to be selected.
     * Select the first item in the list as the default value of the dropdown.
     */
    @FXML
    private void initialize() {
        ObservableList<String> numberOfRoundsDropdown = FXCollections.observableArrayList(
                "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
        this.numberOfRounds.setItems(numberOfRoundsDropdown);
        this.numberOfRounds.getSelectionModel().select(0);

        ObservableList<String> player1Dropdown = FXCollections.observableArrayList("Human", "Minimax Bot", "Simulated Annealing Bot", "Genetic Algorithm Bot");
        ObservableList<String> player2Dropdown = FXCollections.observableArrayList("Minimax Bot", "Simulated Annealing Bot", "Genetic Algorithm Bot");

        this.player1.setItems(player1Dropdown);
        this.player1.getSelectionModel().select(0);

        this.player2.setItems(player2Dropdown);
        this.player2.getSelectionModel().select(0);
    }


    /**
     * Reset player1 and player2 text fields and reset numberOfRounds dropdown to default value
     * if reset button is clicked.
     */
    @FXML
    private void reset() {
        this.player1.setValue(null);
        this.player2.setValue(null);
        this.numberOfRounds.getSelectionModel().select(0);
    }


    /**
     * Open OutputFrame controlled by OutputFrameController if play button is clicked and
     * all input have been successfully validated.
     *
     * @throws IOException To load the FXMLLoader to open the Adjacency game screen (output screen).
     */
    @FXML
    private void play() throws IOException {
        if (this.isInputFieldValidated()) {
            // Close primary stage/input frame.
            Stage primaryStage = (Stage) this.player1.getScene().getWindow();
            primaryStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("OutputFrame.fxml"));
            Parent root = loader.load();

            // Get controller of output frame and pass input including player names and number of rounds chosen.
            OutputFrameController outputFC = loader.getController();
            outputFC.getInput(this.player1.getValue(), this.player2.getValue(), this.numberOfRounds.getValue(), this.isBotFirst.isSelected());

            // Open the new frame.
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Game Board Display");
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setResizable(true);
            secondaryStage.show();
        }
    }


    /**
     * Return whether all input fields have been successfully validated or not.
     *
     * @return boolean
     */
    private boolean isInputFieldValidated() {
        String playerX = this.player1.getValue();
        String playerO = this.player2.getValue();
        String roundNumber = this.numberOfRounds.getValue();

        if (playerX.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Player 1 name is blank.").showAndWait();
            return false;
        }

        if (playerO.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Player 2 name is blank.").showAndWait();
            return false;
        }

//        if (playerX.equals(playerO)){
//            new Alert(Alert.AlertType.ERROR, "Player 1 and Player 2 cannot have the same name.").showAndWait();
//            return false;
//        }

        if (roundNumber.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Number of rounds dropdown menu is blank.").showAndWait();
            return false;
        }

        return true;
    }
}