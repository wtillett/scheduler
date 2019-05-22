/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view.report;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
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
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> months = new ArrayList<>(Arrays.asList("Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
                "Nov", "Dec"));
        Set<String> types = new HashSet<>();
        
        months.forEach(month -> {
            appointments.forEach((Appointment a) -> {
                if (month.equals(extractMonth(a))) {
                    types.add(a.getType().getValue());
                }
            });
            map.put(month, types.size());
            types.clear();
        });

        XYChart.Series data = new XYChart.Series();
        
        map.forEach((month, count) -> {
            data.getData().add(
                    new XYChart.Data(month, count));
        });

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

    private String extractMonth(Appointment a) {
        LocalDate date = a.getStart().toLocalDate();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM");
        String month = date.format(dtf);
        return month;
    }

}
