package sami.talkaddict.domain.interfaces;

import com.j256.ormlite.stmt.QueryBuilder;
import sami.talkaddict.domain.entities.BaseEntity;
import sami.talkaddict.domain.exceptions.ApplicationException;

public interface GenericDao<TEntity extends BaseEntity> {
    void createOrUpdate(TEntity entity) throws ApplicationException;
    void delete(TEntity entity) throws ApplicationException;
    void deleteById(int id) throws ApplicationException;
    TEntity findById(Integer id) throws ApplicationException;
    Iterable<TEntity> findAll() throws ApplicationException;
    Iterable<TEntity> findByFilter(QueryBuilder<TEntity, Integer> filter) throws ApplicationException;
    QueryBuilder<TEntity, Integer> queryBuilder();
}
