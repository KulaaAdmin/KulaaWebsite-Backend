package com.kula.kula_project_backend.entity;

import java.sql.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.experimental.Accessors;
import nonapi.io.github.classgraph.json.Id;

@Data
@Document(collection = "role")
@Accessors(chain = true)
public class Role {
    /**
     * The id of the role. It is the primary key in the "role" collection.
     */
    @Id
    private ObjectId roleId;

    /* 
     * role name
     */
    @Field("role_name")
    private String roleName;

    /* 
     * 权限名称(permissionDefinition)，isEnable
     */
    @Field("permissions")
    private HashMap<String,Boolean> permissions;

    @Field("create_time")
    private Date createTime;
    
    @Field("update_time")
    private Date updateTime;
    
    @Field("create_user_id")
    private ObjectId createUserId;
    
    @Field("update_user_id")
    private ObjectId updateUserId;
}
