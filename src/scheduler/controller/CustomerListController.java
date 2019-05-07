/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.Database;
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

    private static Connection conn;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private CityDao cityDao;
    private CountryDao countryDao;

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
    private void handleGoBackBtn(ActionEvent event) {
    }
    
    private void handleSceneChange(int destination) {
        
    }

    private class CustomerTableRow {

        SimpleStringProperty colName = new SimpleStringProperty();
        SimpleStringProperty colAddress = new SimpleStringProperty();
        SimpleStringProperty colCity = new SimpleStringProperty();
        SimpleStringProperty colCountry = new SimpleStringProperty();
        SimpleStringProperty colPhone = new SimpleStringProperty();

        CustomerTableRow(Customer customer) {
            Address address = addressDao.get(customer.getAddressId().getValue());
            City city = cityDao.get(address.getCityId().getValue());
            Country country = countryDao.get(city.getCountryId().getValue());
            this.colName = customer.getCustomerName();
            this.colAddress = address.getAddress();
            this.colCity = city.getCity();
            this.colCountry = country.getCountry();
            this.colPhone = address.getPhone();
        }
    }
    
    
}
