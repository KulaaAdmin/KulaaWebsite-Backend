package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.BookMarks;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
/**
 * BookMarksRepository is a Spring Data MongoDB repository for the BookMarks entity.
 */
public interface BookMarksRepository extends MongoRepository<BookMarks, ObjectId> {
    /**
     * Finds a bookmark by its user id.
     * @param userId The id of the user.
     * @return An Optional that may contain the BookMarks object if found.
     */
    Optional<BookMarks> findByUserId(ObjectId userId);
}
