package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<Users, ObjectId> {
    /**
     * Check if a user exists by phone number.
     *
     * @param phoneNumber The phone number to check.
     * @return True if a user with the given phone number exists, false otherwise.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Check if a user exists by email.
     *
     * @param email The email to check.
     * @return True if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by username.
     *
     * @param username The username to find.
     * @return Optional containing the user if found.
     */
    Optional<Users> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email The email to find.
     * @return Optional containing the user if found.
     */
    Optional<Users> findByEmail(String email);

    /**
     * Find a user by phone number.
     *
     * @param phoneNumber The phone number to find.
     * @return Optional containing the user if found.
     */
    Optional<Users> findByPhoneNumber(String phoneNumber);

    /**
     * Find a user by email or phone number.
     *
     * @param emailOrPhoneNumber The email or phone number to find.
     * @return Optional containing the user if found.
     */
    Optional<Users> findByEmailOrPhoneNumber(String emailOrPhoneNumber);
}
