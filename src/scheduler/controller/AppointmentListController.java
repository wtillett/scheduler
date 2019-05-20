/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.Database;
import scheduler.Scheduler;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.UserDao;
import scheduler.model.Appointment;
import scheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class AppointmentListController implements Initializable {

    @FXML
    private TableView<AppointmentTableRow> table;
    @FXML
    private TableColumn<AppointmentTableRow, String> colCustomerName;
    @FXML
    private TableColumn<AppointmentTableRow, String> colType;
    @FXML
    private TableColumn<AppointmentTableRow, String> colDate;
    @FXML
    private TableColumn<AppointmentTableRow, String> colStart;
    @FXML
    private TableColumn<AppointmentTableRow, String> colEnd;
    @FXML
    private ChoiceBox<Object> cb;
    @FXML
    private Button newAppointmentBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button goBackBtn;

    private static Connection conn;
    private AppointmentDao aDao;
    private CustomerDao cDao;
    private UserDao uDao;

    private static final int ADD_APPOINTMENT = 0;
    private static final int GO_BACK = 1;

    private static final int ALL_APPOINTMENTS = 0;
    private static final int WEEK = 2;
    private static final int MONTH = 3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        aDao = new AppointmentDao(conn);
        cDao = new CustomerDao(conn);
        uDao = new UserDao(conn);

        LocalDate today = LocalDate.now();
        LocalDate oneMonth = today.plusMonths(1);
        LocalDate oneWeek = today.plusWeeks(1);

        List<Appointment> allAppointments = aDao.getAll();
        List<Appointment> thisMonth = FXCollections.observableArrayList();
        List<Appointment> thisWeek = FXCollections.observableArrayList();
        allAppointments.forEach((Appointment a) -> {
            if (a.getStart().toLocalDate().isBefore(oneWeek)) {
                thisWeek.add(a);
                thisMonth.add(a);
            } else if (a.getStart().toLocalDate().isBefore(oneMonth)) {
                thisMonth.add(a);
            }
        });

        colCustomerName.setCellValueFactory(cellData
                -> cellData.getValue().colCustomerName);
        colType.setCellValueFactory(cellData
                -> cellData.getValue().colType);
        colDate.setCellValueFactory(cellData
                -> cellData.getValue().colDate);
        colStart.setCellValueFactory(cellData
                -> cellData.getValue().colStart);
        colEnd.setCellValueFactory(cellData
                -> cellData.getValue().colEnd);

        cb.setItems(FXCollections.observableArrayList(
                "All Appointments", new Separator(),
                "This Week", "This Month"));
        cb.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        switch (newValue.intValue()) {
                            case ALL_APPOINTMENTS:
                                refreshTable(allAppointments);
                                break;
                            case WEEK:
                                refreshTable(thisWeek);
                                break;
                            case MONTH:
                                refreshTable(thisMonth);
                                break;
                            default:
                                refreshTable(allAppointments);
                        }
                    }
                });
        cb.getSelectionModel().select(ALL_APPOINTMENTS);
    }

    @FXML
    private void handleNewAppointmentBtn(ActionEvent event) {
        handleSceneChange(ADD_APPOINTMENT);
    }

    @FXML
    private void handleEditBtn(ActionEvent event) {
        AppointmentTableRow current = table.getSelectionModel().getSelectedItem();
        int id = current.appointmentId;
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/scheduler/view/EditAppointment.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
            EditAppointmentController controller = loader.getController();

            controller.setAppointment(id);
        } catch (IOException e) {
            Logger.getLogger(AppointmentListController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleDeleteBtn(ActionEvent event) {
        AppointmentTableRow current = table.getSelectionModel().getSelectedItem();
        int id = current.appointmentId;
        Appointment appointment = aDao.get(id);
        aDao.delete(appointment);
        refreshTable(aDao.getAll());
    }

    @FXML
    private void handleGoBackBtn(ActionEvent event) {
        handleSceneChange(GO_BACK);
    }

    private void handleSceneChange(int action) {
        String fxml = "/scheduler/view/";
        switch (action) {
            case ADD_APPOINTMENT:
                fxml += "AddAppointment.fxml";
                break;
            case GO_BACK:
                fxml += "Main.fxml";
                break;
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(AppointmentListController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    private void refreshTable(List<Appointment> list) {
        ObservableList<AppointmentTableRow> appointmentList
                = FXCollections.observableArrayList();
        populateList(list, appointmentList);
        table.setItems(appointmentList);
    }

    private void populateList(List<Appointment> appointments,
            ObservableList<AppointmentTableRow> list) {
        appointments.forEach((Appointment a)
                -> list.add(new AppointmentTableRow(a)));
    }

    private class AppointmentTableRow {

        int appointmentId;
        SimpleStringProperty colCustomerName = new SimpleStringProperty();
        SimpleStringProperty colType = new SimpleStringProperty();
        SimpleStringProperty colDate;
        SimpleStringProperty colStart;
        SimpleStringProperty colEnd;

        AppointmentTableRow(Appointment appointment) {
            this.appointmentId = appointment.getAppointmentId().getValue();
            Customer customer = cDao.get(appointment
                    .getCustomerId().getValue());
            this.colCustomerName = customer.getCustomerName();
            this.colType = appointment.getType();
            this.colDate = new SimpleStringProperty(appointment
                    .getStart().toLocalDate().toString());
            this.colStart = new SimpleStringProperty(appointment
                    .getStart().toLocalTime().toString());
            this.colEnd = new SimpleStringProperty(appointment
                    .getEnd().toLocalTime().toString());
        }
    }
}
