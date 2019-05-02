/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Will Tillett
 */
public class CityDAO implements Dao<City> {
    
    private final Connection conn;
    private static Timestamp now;
        
    public CityDAO(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public Optional<City> get(int id) {
    }

    @Override
    public List<City> getAll() {
    }

    @Override
    public int insert(City t) {
    }

    @Override
    public int update(City t) {
    }

    @Override
    public void delete(City t) {
    }
    
    
}
