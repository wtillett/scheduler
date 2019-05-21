/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view.report;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.Scheduler;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class ReportController implements Initializable {

    private static final int APPT_TYPE_BY_MONTH = 0;
    private static final int CONSULTANT_SCHEDULE = 1;
    private static final int TBD = 2;
    private static final int CANCEL = 3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void handleApptTypeBtn(ActionEvent event) {
        handleSceneChange(APPT_TYPE_BY_MONTH);
    }

    @FXML
    void handleConsultantBtn(ActionEvent event) {
        handleSceneChange(CONSULTANT_SCHEDULE);
    }

    @FXML
    void handleCancelBtn(ActionEvent event) {
        handleSceneChange(CANCEL);
    }

    private void handleSceneChange(int action) {
        String fxml = "/scheduler/view/report/";
        switch (action) {
            case APPT_TYPE_BY_MONTH:
                fxml += "ApptTypeByMonth.fxml";
                break;
            case CONSULTANT_SCHEDULE:
                fxml += "ScheduleByConsultant.fxml";
                break;
            case TBD:
                break;
            case CANCEL:
                fxml = "/scheduler/view/Main.fxml";
                break;
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ReportController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

}
