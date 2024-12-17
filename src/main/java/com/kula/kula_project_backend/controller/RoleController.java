package com.kula.kula_project_backend.controller;

import java.util.ArrayList;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kula.kula_project_backend.common.ResponseResult;

@RestController
@RequestMapping("/role")
public class RoleController {

    public ResponseResult create(String Role){
		return new ResponseResult(0, Role);
        //return RoleRepo.Create(Role,new Role());	
	}
	public ResponseResult get(String Role,String PermissionID){
        return new ResponseResult(0, Role);
		//return RoleRepo.Query(Role).Permissions.get(PermissionID);	
	}
	public ResponseResult set(String Role,String PermissionID,Boolean Value){
        return new ResponseResult(0, Role);
		//RoleRepo.Query(Role).Permissions.put(PermissionID,Value);	
	}
	
	public ArrayList<String> List(String Role){
        return new ArrayList<>();
		//return new ArrayList<String>(RoleRepo.Query(Role).Permissions.listKeys());	
	}
}
