package models;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.data.repository.CrudRepository;

/**
 * Provides CRUD functionality for accessing people. Spring Data auto-magically takes care of many standard
 * operations here.
 */
@Named
@Singleton
public interface DocumentRepository extends CrudRepository<Document, Long> {
    
}