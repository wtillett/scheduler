/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view.report;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import scheduler.Database;
import scheduler.Scheduler;
import scheduler.dao.AppointmentDao;
import scheduler.model.Appointment;

/**
 * FXML Controller class
 *
 * @author wtill
 */
public class ApptTypeByMonthController implements Initializable {

    @FXML
    private BarChart<String, Integer> bc;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Button cancelBtn;

    private static Connection conn;
    private AppointmentDao aDao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        aDao = new AppointmentDao(conn);

        List<Appointment> appointments = aDao.getAll();
        Map map = new LinkedHashMap();
        
        map.put("Jan", 0);
        map.put("Feb", 0);
        map.put("Mar", 0);
        map.put("Apr", 0);
        map.put("May", 0);
        map.put("Jun", 0);
        map.put("Jul", 0);
        map.put("Aug", 0);
        map.put("Sep", 0);
        map.put("Oct", 0);
        map.put("Nov", 0);
        map.put("Dec", 0);

        XYChart.Series data = new XYChart.Series();
        data.getData().add(new XYChart.Data("Jan", 1));
        data.getData().add(new XYChart.Data("Feb", 2));
        data.getData().add(new XYChart.Data("Mar", 3));
        data.getData().add(new XYChart.Data("Apr", 6));
        data.getData().add(new XYChart.Data("May", 0));
        data.getData().add(new XYChart.Data("Jun", 2));
        data.getData().add(new XYChart.Data("Jul", 5));
        data.getData().add(new XYChart.Data("Aug", 1));
        data.getData().add(new XYChart.Data("Sep", 10));
        data.getData().add(new XYChart.Data("Oct", 0));
        data.getData().add(new XYChart.Data("Nov", 2));
        data.getData().add(new XYChart.Data("Dec", 0));
        
        bc.getData().add(data);
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        String fxml = "/scheduler/view/report/Main.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ApptTypeByMonthController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

}
