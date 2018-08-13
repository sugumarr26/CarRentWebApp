package com.mv.schelokov.carent.model.db.dao;

import com.mv.schelokov.carent.model.db.dao.exceptions.DbException;
import com.mv.schelokov.carent.model.entity.User;
import com.mv.schelokov.carent.model.entity.builders.RoleBuilder;
import com.mv.schelokov.carent.model.entity.builders.UserBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public class UserDaoTest {
    
    private Connection connection;
    private UserDao ur;
    
    public UserDaoTest() {
    }
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException, 
            InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/car_rent_test?autoReconnect=true"
                        + "&useSSL=false&characterEncoding=utf-8",
                "car_rent_app", "Un3L41NoewVA");
        ur = new UserDao(connection);
    }

    @Test
    public void findLoginPasswordUser() throws DbException {
        
        List<User> ul = ur.read(new UserDao.FindLoginPasswordCriteria("boss@mail.com", 
                "admin"));
        assertEquals(ul.size(), 1);
    }
    
    @Test
    public void createNewUser() throws DbException {
        assertTrue(ur.add(new UserBuilder()
                .setLogin("Dronchik")
                .setPassword("228")
                .setRole(new RoleBuilder()
                        .setId(2)
                        .getRole()).getUser()));
    }
    
    @Test
    public void findAndRemoveUser() throws DbException {
        List<User> ul = ur.read(UserDao.SELECT_ALL_CRITERIA);
        assertTrue(ur.remove(ul.get(ul.size()-1)));
    }
    
    @Test
    public void findLoginPasswordUserNotFount() throws DbException {
        List<User> ul = ur.read(new UserDao.FindLoginPasswordCriteria("bogdan", 
                "admin"));
        assertEquals(ul.size(), 0);
    }
    
    @Test
    public void findLoginAndUpdateUser() throws DbException {
        User user = ur.read(new UserDao.FindLoginCriteria("Dronchik")).get(0);
        user.setPassword(Integer.toString(Integer.parseInt(user.getPassword()) + 1));
        assertTrue(ur.update(user));
    }
    
    @After
    public void close() throws SQLException {
       connection.close();
    }
    
}