package com.mv.schelokov.car_rent.model.db.dao;

import com.mv.schelokov.car_rent.model.db.dao.interfaces.AbstractSqlDao;
import com.mv.schelokov.car_rent.model.db.dao.interfaces.Criteria;
import com.mv.schelokov.car_rent.model.db.dao.interfaces.SqlCriteria;
import com.mv.schelokov.car_rent.model.entity.Car;
import com.mv.schelokov.car_rent.model.entity.builders.CarBuilder;
import com.mv.schelokov.car_rent.model.entity.builders.CarMakeBuilder;
import com.mv.schelokov.car_rent.model.entity.builders.CarModelBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public class CarDao extends AbstractSqlDao<Car> {
    
    public interface ReadCriteria extends SqlCriteria {}
    
    public interface DeleteCriteria extends SqlCriteria {}
    
    public static final Criteria SELECT_ALL = new SelectAll();
    public static final Criteria SELECT_AVAILABLE = new SelectAvailable();
    
    public static class SelectAll implements ReadCriteria {

        private static final String QUERY = "SELECT car_id,license_plate,"
                + "year_of_make,price,model,name,make,make_name,available "
                + "FROM cars_full";

        @Override
        public String toSqlQuery() {
            return QUERY;
        }

        @Override
        public void setStatement(PreparedStatement ps) throws SQLException {}
    }
    
    public static class SelectAvailable extends SelectAll {
        private static final String QUERY = " WHERE available=b'1'";
        @Override
        public String toSqlQuery() {
            return super.toSqlQuery() + QUERY;
        }
    }
    
    public static class FindById extends SelectAll {

        private static final String QUERY = " WHERE car_id=?";
        private static final int CAR_ID = 1;

        private final int id;

        // TODO: Add constructors for Invoice and Defect entities!
        public FindById(int id) {
            this.id = id;
        }

        @Override
        public String toSqlQuery() {
            return super.toSqlQuery() + QUERY;
        }

        @Override
        public void setStatement(PreparedStatement ps) throws SQLException {
            ps.setInt(CAR_ID, id);
        }
    }
    
    private static final String CREATE_QUERY = "INSERT INTO cars (model,"
            + "license_plate,year_of_make,price,available) VALUES (?,?,?,?,?)";
    private static final String REMOVE_QUERY = "DELETE FROM cars WHERE"
            + " car_id=?";
    private static final String UPDATE_QUERY = "UPDATE cars SET model=?,"
            + "license_plate=?,year_of_make=?,price=?,available=? "
            + "WHERE car_id=?";
    
    /**
     * The Field enum has column names for read methods and number of column for
     * the update method and the add method (in the NUMBER attribute)
     */
    enum Fields {
        CAR_ID(6), LICENSE_PLATE(2), YEAR_OF_MAKE(3), PRICE(4), MODEL(1), NAME, 
        MAKE, MAKE_NAME, AVAILABLE(5);
        
        int NUMBER;
        
        Fields(int number) {
            this.NUMBER = number;
        }
        Fields() {
        }
    }
    
    public CarDao(Connection connection) {
        super(connection);
    }

        @Override
    protected String getCreateQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected String getRemoveQuery() {
        return REMOVE_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected Car createItem(ResultSet rs) throws SQLException {
        return new CarBuilder()
                .setId(rs.getInt(Fields.CAR_ID.name()))
                .setLicensePlate(rs.getString(Fields.LICENSE_PLATE.name()))
                .setYearOfMake(rs.getInt(Fields.YEAR_OF_MAKE.name()))
                .setPrice(rs.getInt(Fields.PRICE.name()))
                .setAvailable(rs.getBoolean(Fields.AVAILABLE.name()))
                .setModel(new CarModelBuilder()
                        .setId(rs.getInt(Fields.MODEL.name()))
                        .setName(rs.getString(Fields.NAME.name()))
                        .setCarMake(new CarMakeBuilder()
                                .setId(rs.getInt(Fields.MAKE.name()))
                                .setName(rs.getString(Fields.MAKE_NAME.name()))
                                .getCarMake())
                        .getCarModel())
                .getCar();
    }

    @Override
    protected void setStatement(PreparedStatement ps, Car item, 
            boolean isUpdateStatement) throws SQLException {
        ps.setInt(Fields.MODEL.NUMBER, item.getCarModel().getId());
        ps.setString(Fields.LICENSE_PLATE.NUMBER, item.getLicensePlate());
        ps.setInt(Fields.YEAR_OF_MAKE.NUMBER, item.getYearOfMake());
        ps.setInt(Fields.PRICE.NUMBER, item.getPrice());
        ps.setBoolean(Fields.AVAILABLE.NUMBER, item.isAvailable());
        if (isUpdateStatement)
            ps.setInt(Fields.CAR_ID.NUMBER, item.getId());
    }

    @Override
    protected boolean checkCriteriaInstance(Criteria criteria, 
            boolean isDeleteCriteria) {
        if (isDeleteCriteria) {
            if (criteria instanceof DeleteCriteria)
                return true;
        } else if (criteria instanceof ReadCriteria)
            return true;
        return false;
    }
}