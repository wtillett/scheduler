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

    // From customer table - ignoring the active field
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleIntegerProperty addressId = new SimpleIntegerProperty();

    // Address table
    private final SimpleStringProperty address1 = new SimpleStringProperty();
    private final SimpleStringProperty address2 = new SimpleStringProperty();
    private final SimpleIntegerProperty cityId = new SimpleIntegerProperty();
    private final SimpleStringProperty postalCode = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();

    // City table
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleIntegerProperty countryId = new SimpleIntegerProperty();

    // Country table
    private final SimpleStringProperty country = new SimpleStringProperty();

    // Constructors
    public Customer() {
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

    public void setAddress1(String address) {
        this.address1.set(address);
    }

    public void setAddress2(String address) {
        this.address2.set(address);
    }

    public void setCityId(int id) {
        this.cityId.set(id);
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setCountryId(int id) {
        this.countryId.set(id);
    }

    public void setCountry(String country) {
        this.country.set(country);
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

    public SimpleStringProperty getAddress1() {
        return address1;
    }

    public SimpleStringProperty getAddress2() {
        return address2;
    }

    public SimpleIntegerProperty getCityId() {
        return cityId;
    }

    public SimpleStringProperty getPostalCode() {
        return postalCode;
    }

    public SimpleStringProperty getPhone() {
        return phone;
    }

    public SimpleStringProperty getCity() {
        return city;
    }

    public SimpleIntegerProperty getCountryId() {
        return countryId;
    }

    public SimpleStringProperty getCountry() {
        return country;
    }

}
