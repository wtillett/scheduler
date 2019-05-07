/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.Database;
import scheduler.Scheduler;
import scheduler.dao.AddressDao;
import scheduler.dao.CityDao;
import scheduler.dao.CountryDao;
import scheduler.dao.CustomerDao;
import scheduler.model.Address;
import scheduler.model.City;
import scheduler.model.Country;
import scheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class CustomerListController implements Initializable {

    @FXML
    private TableView<CustomerTableRow> table;
    @FXML
    private TableColumn<CustomerTableRow, String> colName;
    @FXML
    private TableColumn<CustomerTableRow, String> colAddress;
    @FXML
    private TableColumn<CustomerTableRow, String> colCity;
    @FXML
    private TableColumn<CustomerTableRow, String> colCountry;
    @FXML
    private TableColumn<CustomerTableRow, String> colPhone;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private Button goBackBtn;
    @FXML
    private Button editBtn;

    private static Connection conn;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private CityDao cityDao;
    private CountryDao countryDao;

    private static final int GO_BACK = 0;
    private static final int EDIT_CUSTOMER = 1;
    private static final int ADD_CUSTOMER = 2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        customerDao = new CustomerDao(conn);
        addressDao = new AddressDao(conn);
        cityDao = new CityDao(conn);
        countryDao = new CountryDao(conn);

        ObservableList<Customer> allCustomers = customerDao.getAll();
        ObservableList<CustomerTableRow> customerList
                = FXCollections.observableArrayList();
        for (Customer customer : allCustomers) {
            customerList.add(new CustomerTableRow(customer));
        }

        colName.setCellValueFactory(cellData -> cellData.getValue().colName);
        colAddress.setCellValueFactory(cellData -> cellData.getValue().colAddress);
        colCity.setCellValueFactory(cellData -> cellData.getValue().colCity);
        colCountry.setCellValueFactory(cellData -> cellData.getValue().colCountry);
        colPhone.setCellValueFactory(cellData -> cellData.getValue().colPhone);
        table.setItems(customerList);
    }

    @FXML
    private void handleAddCustomerBtn(ActionEvent event) {
    }

    @FXML
    private void handleEditBtn(ActionEvent event) {
        CustomerTableRow current = table.getSelectionModel().getSelectedItem();
        int id = current.customerId;
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/scheduler/view/EditCustomer.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
            EditCustomerController controller = loader.getController();

            controller.setCustomer(id);
        } catch (IOException e) {
            Logger.getLogger(CustomerListController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleGoBackBtn(ActionEvent event) {
        handleSceneChange(GO_BACK);
    }

    private void handleSceneChange(int action) {
        String fxml = "/scheduler/view/";
        switch (action) {
            case GO_BACK:
                fxml += "Main.fxml";
                break;
            case EDIT_CUSTOMER:
                fxml += "EditCustomer.fxml";
                break;
            case ADD_CUSTOMER:
                fxml += "AddCustomer.fxml";
                break;
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(CustomerListController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    private class CustomerTableRow {

        int customerId;
        SimpleStringProperty colName = new SimpleStringProperty();
        SimpleStringProperty colAddress = new SimpleStringProperty();
        SimpleStringProperty colCity = new SimpleStringProperty();
        SimpleStringProperty colCountry = new SimpleStringProperty();
        SimpleStringProperty colPhone = new SimpleStringProperty();

        CustomerTableRow(Customer customer) {
            Address address = addressDao.get(customer.getAddressId().getValue());
            City city = cityDao.get(address.getCityId().getValue());
            Country country = countryDao.get(city.getCountryId().getValue());
            this.customerId = customer.getCustomerId().getValue();
            this.colName = customer.getCustomerName();
            this.colAddress = address.getAddress();
            this.colCity = city.getCity();
            this.colCountry = country.getCountry();
            this.colPhone = address.getPhone();
        }
    }

}
