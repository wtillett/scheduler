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
public class City {

    private final SimpleIntegerProperty cityId = new SimpleIntegerProperty();
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleIntegerProperty countryId = new SimpleIntegerProperty();

    // Constructors
    public City() {
    }

    public City(int cityId, String city, int countryId) {
        setCityId(cityId);
        setCity(city);
        setCountryId(countryId);
    }

    public City(String city, int countryId) {
        setCity(city);
        setCountryId(countryId);
    }

    // Setters
    public void setCityId(int id) {
        this.cityId.set(id);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setCountryId(int id) {
        this.countryId.set(id);
    }

    // Getters
    public SimpleIntegerProperty getCityId() {
        return cityId;
    }

    public SimpleStringProperty getCity() {
        return city;
    }

    public SimpleIntegerProperty getCountryId() {
        return countryId;
    }

}
