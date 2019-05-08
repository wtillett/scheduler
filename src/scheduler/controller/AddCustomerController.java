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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
public class AddCustomerController implements Initializable {

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private static Connection conn;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private CityDao cityDao;
    private CountryDao countryDao;
    private static Customer customer;
    private static Address address;
    private static City city;
    private static Country country;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        customerDao = new CustomerDao(conn);
        addressDao = new AddressDao(conn);
        cityDao = new CityDao(conn);
        countryDao = new CountryDao(conn);
        customer = new Customer();
        address = new Address();
        city = new City();
        country = new Country();
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) {
        if (isEveryInputValid()) {
            getFieldsFromInput();
            handleCountry();
            handleCity();
            handleAddress();
            handleCustomer();
            handleSceneChange();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Customer not valid");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        handleSceneChange();
    }

    private void handleSceneChange() {
        String fxml = "/scheduler/view/CustomerList.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(AddCustomerController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    private boolean isEveryInputValid() {
        if (customerNameField.getText().isEmpty()
                || addressField.getText().isEmpty()
                || cityField.getText().isEmpty()
                || postalCodeField.getText().isEmpty()
                || countryField.getText().isEmpty()
                || phoneField.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void getFieldsFromInput() {
        customer.setCustomerName(customerNameField.getText());
        address.setAddress(addressField.getText());
        if (address2Field.getText().isEmpty()) {
            address.setAddress2(address2Field.getText());
        }
        address.setPostalCode(postalCodeField.getText());
        address.setPhone(phoneField.getText());
        city.setCity(cityField.getText());
        country.setCountry(countryField.getText());
    }

    private void handleCountry() {
        countryDao.insert(country);
        country.setCountryId(countryDao
                .getId(country.getCountry().getValue()));
    }

    private void handleCity() {
        city.setCountryId(country.getCountryId().getValue());
        cityDao.insert(city);
        city.setCityId(cityDao.getId(city.getCity().getValue()));
    }

    private void handleAddress() {
        address.setCityId(city.getCityId().getValue());
        addressDao.insert(address);
        address.setAddressId(addressDao
                .getId(address.getAddress().getValue()));
    }

    private void handleCustomer() {
        customer.setAddressId(address.getAddressId().getValue());
        customerDao.insert(customer);
        customer.setCustomerId(customerDao
                .getId(customer.getCustomerName().getValue()));
    }

}
