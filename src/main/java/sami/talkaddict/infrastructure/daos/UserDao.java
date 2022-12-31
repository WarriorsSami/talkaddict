package sami.talkaddict.infrastructure.daos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.stmt.QueryBuilder;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.di.Config;
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
    public synchronized void createOrUpdate(User entity) throws ApplicationException {
        try {
            _dao.createOrUpdate(entity);
            _logger.info("User created or updated with email: " + entity.getEmail());
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating or updating user: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error creating or updating user: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized void delete(User entity) throws ApplicationException {
        try {
            _dao.delete(entity);
            _logger.info("User deleted with email: " + entity.getEmail());
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error deleting user: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized void deleteById(int id) throws ApplicationException{
        try {
            _dao.deleteById(id);
            _logger.info("User deleted with id: " + id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user by id: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error deleting user by id: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized User findById(Integer id) throws ApplicationException {
        try {
            _logger.info("Finding user by id: " + id);
            return _dao.queryForId(id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding user by id: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding user by id: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized Iterable<User> findAll() throws ApplicationException {
        try {
            _logger.info("Finding all users");
            return _dao.queryForAll();
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding all users: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding all users: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized Iterable<User> findByFilter(QueryBuilder<User, Integer> filter) throws ApplicationException {
        try {
            _logger.info("Finding users by filter");
            return _dao.query(filter.prepare());
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding users by filter: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding users by filter: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized QueryBuilder<User, Integer> queryBuilder() {
        return _dao.queryBuilder();
    }

    public synchronized User findByName(String name) throws ApplicationException {
        try {
            _logger.info("Finding user by name: " + name);
            QueryBuilder<User, Integer> queryBuilder = _dao.queryBuilder();
            queryBuilder.where().eq(Config.Database.USERNAME_COLUMN_NAME, name);
            return _dao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding user by name: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding user by name: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    public synchronized Iterable<User> findByNameLike(String name) throws ApplicationException {
        try {
            _logger.info("Finding users by name like: " + name);
            QueryBuilder<User, Integer> queryBuilder = _dao.queryBuilder();
            queryBuilder.where().like(Config.Database.USERNAME_COLUMN_NAME, "%" + name + "%");
            return _dao.query(queryBuilder.prepare());
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding users by name like: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding users by name like: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    public synchronized User findByEmail(String email) throws ApplicationException {
        try {
            _logger.info("Finding user by email: " + email);
            QueryBuilder<User, Integer> queryBuilder = _dao.queryBuilder();
            queryBuilder.where().eq(Config.Database.EMAIL_COLUMN_NAME, email);
            return _dao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding users by email: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding users by email: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }
}
