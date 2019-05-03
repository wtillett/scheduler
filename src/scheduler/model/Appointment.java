/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Will Tillett
 */
public class Appointment {

    // IDs
    private final SimpleIntegerProperty appointmentId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty userId = new SimpleIntegerProperty();

    private final SimpleStringProperty title = new SimpleStringProperty();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final SimpleStringProperty contact = new SimpleStringProperty();
    private final SimpleStringProperty type = new SimpleStringProperty();
    private final SimpleStringProperty url = new SimpleStringProperty();

    private LocalDateTime start;
    private LocalDateTime end;

    // Constructors
    public Appointment() {
    }

    public Appointment(int appointmentId, int customerId, String title,
            String description, String location, String contact, String url,
            LocalDateTime start, LocalDateTime end, String type, int userId) {
        setAppointmentId(appointmentId);
        setCustomerId(customerId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setUrl(url);
        setStart(start);
        setEnd(end);
        setType(type);
        setUserId(userId);
    }

    public Appointment(int customerId, String title, String description,
            String location, String contact, String url, LocalDateTime start,
            LocalDateTime end, String type, int userId) {
        setCustomerId(customerId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setUrl(url);
        setStart(start);
        setEnd(end);
        setType(type);
        setUserId(userId);
    }

    // Setters
    public void setAppointmentId(int id) {
        this.appointmentId.set(id);
    }

    public void setCustomerId(int id) {
        this.customerId.set(id);
    }

    public void setUserId(int id) {
        this.userId.set(id);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    // Getters
    public SimpleIntegerProperty getAppointmentId() {
        return appointmentId;
    }

    public SimpleIntegerProperty getCustomerId() {
        return customerId;
    }

    public SimpleIntegerProperty getUserId() {
        return userId;
    }

    public SimpleStringProperty getTitle() {
        return title;
    }

    public SimpleStringProperty getDescription() {
        return description;
    }

    public SimpleStringProperty getLocation() {
        return location;
    }

    public SimpleStringProperty getContact() {
        return contact;
    }

    public SimpleStringProperty getType() {
        return type;
    }

    public SimpleStringProperty getUrl() {
        return url;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

}
