package sami.talkaddict.domain;

public interface GenericDao<TEntity extends BaseEntity> {
    void createOrUpdate(TEntity entity) throws ApplicationException;
    void delete(TEntity entity) throws ApplicationException;
    void deleteById(int id) throws ApplicationException;
    TEntity findById(Integer id) throws ApplicationException;
    Iterable<TEntity> findAll() throws ApplicationException;
}
