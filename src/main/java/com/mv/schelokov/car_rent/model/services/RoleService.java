
package com.mv.schelokov.car_rent.model.services;

import com.mv.schelokov.car_rent.model.db.dao.exceptions.DaoException;
import com.mv.schelokov.car_rent.model.db.dao.exceptions.DbException;
import com.mv.schelokov.car_rent.model.db.dao.factories.CriteriaFactory;
import com.mv.schelokov.car_rent.model.db.dao.factories.DaoFactory;
import com.mv.schelokov.car_rent.model.db.dao.interfaces.Criteria;
import com.mv.schelokov.car_rent.model.db.dao.interfaces.Dao;
import com.mv.schelokov.car_rent.model.services.exceptions.ServiceException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public class RoleService {
    
    private static final Logger LOG = Logger.getLogger(RoleService.class);
    private static final String ROLE_DAO_ERROR = "Failed to get roles "
            + "list from the dao";
    private static final String INSTANCE_ERROR = "Failed to get instance";
    private static volatile RoleService instance;

    public static RoleService getInstance() throws ServiceException {
        RoleService localInstance = instance;
        if (localInstance == null) {
            synchronized (RoleService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RoleService();
                }
            }
        }
        if (localInstance == null) {
            LOG.error(INSTANCE_ERROR);
            throw new ServiceException(INSTANCE_ERROR);
        }
        return localInstance;
    }
    
    public List getAllRoles() throws ServiceException {
        try (DaoFactory daoFactory = new DaoFactory()) {
            Dao roleDao = daoFactory.getRoleDao();
            Criteria criteria = CriteriaFactory.getAllRoles();
            return roleDao.read(criteria);
        }
        catch (DaoException | DbException ex) {
            LOG.error(ROLE_DAO_ERROR, ex);
            throw new ServiceException(ROLE_DAO_ERROR, ex);
        }
    }
    
    private RoleService() {}
}
