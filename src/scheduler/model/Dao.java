/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import java.util.List;

/**
 *
 * @author Will Tillett
 */
public interface Dao<T> {

    T get(int id);
    
    int getId(String name);

    List<T> getAll();

    int insert(T t);

    int update(T t);

    void delete(T t);
}
