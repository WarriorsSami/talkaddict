package sami.talkaddict.infrastructure.daos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.stmt.QueryBuilder;
import sami.talkaddict.di.Config;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.managers.DatabaseManager;

import java.sql.SQLException;
import java.util.Comparator;

public class DirectMessageDao implements GenericDao<DirectMessage> {
    private final Logger _logger;
    private final DatabaseManager _databaseManager;
    private final Dao<DirectMessage, Integer> _dao;

    public DirectMessageDao(Logger logger, DatabaseManager databaseManager) throws ApplicationException, SQLException {
         if (logger == null) {
            throw new ApplicationException("Logger is null");
        }
        _logger = logger;
        if (databaseManager == null) {
            throw new ApplicationException("DbManager is null");
        }
        _databaseManager = databaseManager;
        _dao = DaoFactory.createDao(_databaseManager.getConnectionSource(), DirectMessage.class);
    }

    @Override
    public synchronized void createOrUpdate(DirectMessage entity) throws ApplicationException {
        try {
            _dao.createOrUpdate(entity);
            _logger.info("DirectMessage created or updated with id: " + entity.getId());
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating or updating directMessage: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error creating or updating directMessage: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized void delete(DirectMessage entity) throws ApplicationException {
        try {
            _dao.delete(entity);
            _logger.info("DirectMessage deleted with id: " + entity.getId());
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting directMessage: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error deleting directMessage: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized void deleteById(int id) throws ApplicationException {
        try {
            _dao.deleteById(id);
            _logger.info("DirectMessage deleted with id: " + id);
        } catch (SQLException ex) {
            _logger.error(ex, "Error deleting directMessage: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error deleting directMessage: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized DirectMessage findById(Integer id) throws ApplicationException {
        try {
            DirectMessage directMessage = _dao.queryForId(id);
            _logger.info("DirectMessage found with id: " + id);
            return directMessage;
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding directMessage: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding directMessage: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized Iterable<DirectMessage> findAll() throws ApplicationException {
        try {
            Iterable<DirectMessage> directMessages = _dao.queryForAll();
            _logger.info("DirectMessages found");
            return directMessages;
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding directMessages: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding directMessages: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized Iterable<DirectMessage> findByFilter(QueryBuilder<DirectMessage, Integer> filter) throws ApplicationException {
        try {
            Iterable<DirectMessage> directMessages = _dao.query(filter.prepare());
            _logger.info("DirectMessages found");
            return directMessages;
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding directMessages: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding directMessages: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }

    @Override
    public synchronized QueryBuilder<DirectMessage, Integer> queryBuilder() {
        return _dao.queryBuilder();
    }

    public synchronized Iterable<DirectMessage> findByReceiverIdAndSenderIdAndViceVersa(int receiverId, int senderId) throws ApplicationException {
        try {
            QueryBuilder<DirectMessage, Integer> directMessageQb = _dao.queryBuilder();
            directMessageQb
                    .where()
                    .eq(Config.Database.RECEIVER_ID_COLUMN_NAME, receiverId)
                    .and()
                    .eq(Config.Database.SENDER_ID_COLUMN_NAME, senderId);

            var reverseQb = _dao.queryBuilder();
            reverseQb
                    .where()
                    .eq(Config.Database.RECEIVER_ID_COLUMN_NAME, senderId)
                    .and()
                    .eq(Config.Database.SENDER_ID_COLUMN_NAME, receiverId);

            var directMessages = _dao.query(directMessageQb.prepare());
            var reverseDirectMessages = _dao.query(reverseQb.prepare());
            directMessages.addAll(reverseDirectMessages);

            // sort directMessages by date
            directMessages.sort(Comparator.comparing(DirectMessage::getCreatedAt));

            _logger.info("DirectMessages found");
            return directMessages;
        } catch (SQLException ex) {
            _logger.error(ex, "Error finding directMessages: " + ex.getMessage(), ex.getStackTrace());
            throw new ApplicationException("Error finding directMessages: " + ex.getMessage());
        } finally {
            _databaseManager.closeConnectionSource();
        }
    }
}
