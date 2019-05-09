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
public class EditCustomerController implements Initializable {

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
    private Customer customer;
    private Address address;
    private City city;
    private Country country;

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
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) {
        if (isEveryInputValid()) {
            getFieldsFromInput();
            handleCountry();
            handleCity();
            handleAddress();
            updateCustomer();
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
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(EditCustomerController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    public void setCustomer(int customerId) {
        customer = customerDao.get(customerId);
        address = addressDao.get(customer.getAddressId().getValue());
        city = cityDao.get(address.getCityId().getValue());
        country = countryDao.get(city.getCountryId().getValue());

        customerNameField.setText(customer.getCustomerName().getValue());
        addressField.setText(address.getAddress().getValue());
        address2Field.setText(address.getAddress2().getValue());
        cityField.setText(city.getCity().getValue());
        postalCodeField.setText(address.getPostalCode().getValue());
        countryField.setText(country.getCountry().getValue());
        phoneField.setText(address.getPhone().getValue());
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
        if (!address2Field.getText().isEmpty()) {
            address.setAddress2(address2Field.getText());
        }
        address.setPostalCode(postalCodeField.getText());
        address.setPhone(phoneField.getText());
        city.setCity(cityField.getText());
        country.setCountry(countryField.getText());
    }

    private void handleCountry() {
        if (countryDao.getId(country.getCountry().getValue()) == -1) {
            countryDao.insert(country);
            country.setCountryId(countryDao
                    .getId(country.getCountry().getValue()));
        } else {
            countryDao.update(country);
        }

    }

    private void handleCity() {
        city.setCountryId(country.getCountryId().getValue());
        if (cityDao.getId(city.getCity().getValue()) == -1) {
            cityDao.insert(city);
            city.setCityId(cityDao.getId(city.getCity().getValue()));
        } else {
            cityDao.update(city);
        }
    }

    private void handleAddress() {
        address.setCityId(city.getCityId().getValue());
        if (addressDao.getId(address.getAddress().getValue()) == -1) {
            addressDao.insert(address);
            address.setAddressId(addressDao
                    .getId(address.getAddress().getValue()));
        } else {
            addressDao.update(address);
        }
    }

    private void updateCustomer() {
        customer.setAddressId(address.getAddressId().getValue());
        customerDao.update(customer);
    }

}
