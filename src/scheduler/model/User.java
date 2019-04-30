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
 * @author wtill
 */
public class User {

    private final SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private final SimpleStringProperty userName = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    // Ignoring active field and setting to 1
    private final int active = 1;

    // Constructors
    public User() {
    }

    public User(int userId, String userName, String password) {
        setUserId(userId);
        setUserName(userName);
        setPassword(password);
    }

    public User(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    // Setters
    public void setUserId(int id) {
        this.userId.set(id);
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Getters
    public SimpleIntegerProperty getUserId() {
        return userId;
    }

    public SimpleStringProperty getUserName() {
        return userName;
    }

    public SimpleStringProperty getPassword() {
        return password;
    }

}
