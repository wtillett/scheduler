/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Will Tillett
 */
public interface Dao<T> {

    <T> t get(int id);

    List<T> getAll();

    int insert(T t);

    int update(T t);

    void delete(T t);
}
