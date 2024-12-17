package com.kula.kula_project_backend.dao;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.google.common.base.Optional;
import com.kula.kula_project_backend.entity.Role;

public interface RoleRepository extends MongoRepository<Role, ObjectId>{

@Query(value = "{ '_id': ?0 }", fields = "{ 'permissions': 1 }")

    HashMap<String, Boolean> findPermissionsByRoleId(ObjectId roleId); 

    Optional<Role> findByRoleName(String roleName);

   // Optional<Role> findById(ObjectId roleId);

       
}
