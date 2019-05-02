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
public class Address {

    private final SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private final SimpleStringProperty address2 = new SimpleStringProperty();
    private final SimpleIntegerProperty cityId = new SimpleIntegerProperty();
    private final SimpleStringProperty postalCode = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();

    // Constructors
    public Address() {
    }
    
    public Address(int addressId, String address, String address2, 
            int cityId, String postalCode, String phone) {
        setAddressId(addressId);
        setAddress(address);
        setAddress2(address2);
        setCityId(cityId);
        setPostalCode(postalCode);
        setPhone(phone);
    }
    
    public Address(String address, String address2, int cityId, 
            String postalCode, String phone) {
        setAddress(address);
        setAddress2(address2);
        setCityId(cityId);
        setPostalCode(postalCode);
        setPhone(phone);
    }
    
    // Setters
    public void setAddressId(int id) {
        this.addressId.set(id);
    }

    public void setAddress(String address) {
        this.address.set(address);
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
    
    // Getters

    public SimpleIntegerProperty getAddressId() {
        return addressId;
    }

    public SimpleStringProperty getAddress() {
        return address;
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
    
}
