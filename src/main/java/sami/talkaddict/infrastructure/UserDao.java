package sami.talkaddict.infrastructure;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.support.ConnectionSource;
import sami.talkaddict.domain.ApplicationException;
import sami.talkaddict.domain.GenericDao;
import sami.talkaddict.domain.User;

import java.sql.SQLException;

public class UserDao implements GenericDao<User> {
    private static final Logger _logger = LoggerFactory.getLogger(UserDao.class);
    private final ConnectionSource _conn;
    private final Dao<User, Integer> _dao;

    public UserDao() throws SQLException {
        _conn = DbManager.getConnectionSource();
        _dao = DaoFactory.createDao(_conn, User.class);
    }

    @Override
    public void createOrUpdate(User entity) throws ApplicationException {
        try {
            _dao.createOrUpdate(entity);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating or updating user: " + ex.getMessage());
            throw new ApplicationException("Error creating or updating user: " + ex.getMessage());
        } finally {
            closeConnectionSource();
        }
    }

    @Override
    public void delete(User entity) throws ApplicationException {
        try {
            _dao.delete(entity);
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user: " + ex.getMessage());
            throw new ApplicationException("Error deleting user: " + ex.getMessage());
        } finally {
            closeConnectionSource();
        }
    }

    @Override
    public void deleteById(int id) throws ApplicationException{
        try {
            _dao.deleteById(id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting user by id: " + ex.getMessage());
            throw new ApplicationException("Error deleting user by id: " + ex.getMessage());
        } finally {
            closeConnectionSource();
        }
    }

    @Override
    public User findById(Integer id) throws ApplicationException {
        try {
            return _dao.queryForId(id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding user by id: " + ex.getMessage());
            throw new ApplicationException("Error finding user by id: " + ex.getMessage());
        } finally {
            closeConnectionSource();
        }
    }

    @Override
    public Iterable<User> findAll() throws ApplicationException {
        try {
            return _dao.queryForAll();
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding all users: " + ex.getMessage());
            throw new ApplicationException("Error finding all users: " + ex.getMessage());
        } finally {
            closeConnectionSource();
        }
    }

    private void closeConnectionSource() throws ApplicationException {
        try {
            _conn.close();
        } catch (SQLException ex) {
            _logger.error(ex, "Error closing connection source: " + ex.getMessage());
            throw new ApplicationException("Error closing connection source: " + ex.getMessage());
        }
    }
}
