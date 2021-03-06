package models;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
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

    @Modifying
    @Transactional
    @Query("update Document set learnCount = learnCount+1 where id = ?1")
    void incrementLearnCount(Long id);

    @Query("select d from Document d order by RAND()")
    List<Document> findRandom(Pageable pageable);

    List<Document> findByIdNotNullOrderByVisitsDesc();

    @Cacheable(value = "documents")
    List<Document> findByIdNotNullOrderByChangedAtDesc(Pageable page);
    
    @Override
    @Cacheable(value = "document")
    Document findOne(Long id);

    @Override
    @Caching(evict = { @CacheEvict(value="documents", allEntries=true), @CacheEvict(value="document", keyGenerator="documentKeyGenerator") })
    Document save(Document savedDoc);

    @Override
    @Caching(evict = { @CacheEvict(value="documents", allEntries=true), @CacheEvict(value="document", keyGenerator="documentKeyGenerator") })
    void delete(Document savedDoc);

    @Override
    @Caching(evict = { @CacheEvict(value="documents", allEntries=true), @CacheEvict(value="document") })
    void delete(Long id);
}