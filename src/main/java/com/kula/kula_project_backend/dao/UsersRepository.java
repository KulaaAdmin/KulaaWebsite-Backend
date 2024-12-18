package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * UsersRepository is a Spring Data MongoDB repository for the Users entity.
 */
@Repository
public interface UsersRepository extends MongoRepository<Users, ObjectId> {
    /**
     * Checks if a user exists by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return true if a user with the given phone number exists, false otherwise.
     */
    boolean existsByPhoneNumber(String phoneNumber);
    /**
     * Checks if a user exists by their email.
     * @param email The email of the user.
     * @return true if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);
    /**
     * Finds a user by their username.
     * @param username The username of the user.
     * @return An Optional that may contain the Users object if found.
     */
    Optional<Users> findByUsername(String username);
    /**
     * Finds a user by their email.
     * @param email The email of the user.
     * @return An Optional that may contain the Users object if found.
     */
    Optional<Users> findByEmail(String email);
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return An Optional that may contain the Users object if found.
     */
    Optional<Users> findByPhoneNumber(String phoneNumber);
    /**
     * Finds a user by their email or phone number.
     * @param emailOrPhoneNumber The email or phone number of the user.
     * @return An Optional that may contain the Users object if found.
     */
    @Query("{ $or: [ { 'email' : ?0 }, { 'phoneNumber' : ?0 } ] }")
    Optional<Users> findByEmailOrPhoneNumber(String emailOrPhoneNumber);

    // Find all users by createdAt desc
    @Query(value = "{}", fields = "{ '_id': 1, 'username': 1 }")
    Page<Users> findAllByOrderByCreatedAtDesc(Pageable pageable);
}


