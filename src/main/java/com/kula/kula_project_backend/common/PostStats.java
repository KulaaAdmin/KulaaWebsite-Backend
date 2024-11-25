package com.kula.kula_project_backend.common;

import java.io.Serializable;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class PostStats implements Serializable {

	private ObjectId entityId;

	private int postCount;
	private double avgRating;
}
