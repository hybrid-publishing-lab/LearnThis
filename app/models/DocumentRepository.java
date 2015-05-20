package models;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides CRUD functionality for accessing people. Spring Data auto-magically takes care of many standard
 * operations here.
 */
@Named
@Singleton
public interface DocumentRepository extends CrudRepository<Document, Long> {
    
    @Modifying
    @Transactional
    @Query("update Document set visits = visits+1 where id = ?1")
    void incrementVisits(Long id);

    List<Document> findByIdNotNullOrderByVisitsDesc();

}