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
public class Country {

    private final SimpleIntegerProperty countryId = new SimpleIntegerProperty();
    private final SimpleStringProperty country = new SimpleStringProperty();

    // Constructors
    public Country() {
    }

    public Country(int countryId, String country) {
        setCountryId(countryId);
        setCountry(country);
    }

    public Country(String country) {
        setCountry(country);
    }

    // Setters
    public void setCountryId(int id) {
        this.countryId.set(id);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    // Getters
    public SimpleIntegerProperty getCountryId() {
        return countryId;
    }

    public SimpleStringProperty getCountry() {
        return country;
    }

}
