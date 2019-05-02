/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Will Tillett
 */
public class Customer {

    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    // Ignoring active field and setting to 1
    private final int active = 1;

    // Constructors
    public Customer() {
    }

    public Customer(int customerId, String name, int addressId,
            String address, String address2, int cityId, String postalCode,
            String phone, String city, int countryId, String country) {
        setCustomerId(customerId);
        setCustomerName(name);
        setAddressId(addressId);
    }

    public Customer(String name, int addressId, String address,
            String address2, int cityId, String postalCode, String phone,
            String city, int countryId, String country) {
        setCustomerName(name);
        setAddressId(addressId);
    }

    // Setters
    public void setCustomerId(int id) {
        this.customerId.set(id);
    }

    public void setCustomerName(String name) {
        this.customerName.set(name);
    }

    public void setAddressId(int id) {
        this.addressId.set(id);
    }

    // Getters
    public SimpleIntegerProperty getCustomerId() {
        return customerId;
    }

    public SimpleStringProperty getCustomerName() {
        return customerName;
    }

    public SimpleIntegerProperty getAddressId() {
        return addressId;
    }

}
