package sami.talkaddict.infrastructure.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.stmt.QueryBuilder;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.infrastructure.utils.managers.DatabaseManager;

import java.sql.SQLException;

public class UserDao implements GenericDao<User> {
    private final Logger _logger;
    private final DatabaseManager _databaseManager;
    private final Dao<User, Integer> _dao;

    public UserDao(Logger logger, DatabaseManager databaseManager) throws ApplicationException, SQLException {
        if (logger == null) {
            throw new ApplicationException("Logger is null");
        }
        _logger = logger;
        if (databaseManager == null) {
            throw new ApplicationException("DbManager is null");
        }
        _databaseManager = databaseManager;
        _dao = DaoFactory.createDao(_databaseManager.getConnectionSource(), User.class);
    }

    @Override
    public void createOrUpdate(User entity) throws ApplicationException {
        try {
            _dao.createOrUpdate(entity);
            _logger.info("User created or updated with email: " + entity.getEmail());
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating or updating user: " + ex.getMessage());
            throw new ApplicationException("Error creating or updating user: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public void delete(User entity) throws ApplicationException {
        try {
            _dao.delete(entity);
            _logger.info("User deleted with email: " + entity.getEmail());
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user: " + ex.getMessage());
            throw new ApplicationException("Error deleting user: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public void deleteById(int id) throws ApplicationException{
        try {
            _dao.deleteById(id);
            _logger.info("User deleted with id: " + id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user by id: " + ex.getMessage());
            throw new ApplicationException("Error deleting user by id: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public User findById(Integer id) throws ApplicationException {
        try {
            _logger.info("Finding user by id: " + id);
            return _dao.queryForId(id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding user by id: " + ex.getMessage());
            throw new ApplicationException("Error finding user by id: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public Iterable<User> findAll() throws ApplicationException {
        try {
            _logger.info("Finding all users");
            return _dao.queryForAll();
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding all users: " + ex.getMessage());
            throw new ApplicationException("Error finding all users: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public Iterable<User> findByFilter(QueryBuilder<User, Integer> filter) throws ApplicationException {
        try {
            _logger.info("Finding users by filter");
            return _dao.query(filter.prepare());
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding users by filter: " + ex.getMessage());
            throw new ApplicationException("Error finding users by filter: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public QueryBuilder<User, Integer> queryBuilder() {
        return _dao.queryBuilder();
    }
}
