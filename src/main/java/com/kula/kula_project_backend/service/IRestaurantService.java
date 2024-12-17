package com.kula.kula_project_backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.RestaurantDTO;

public interface IRestaurantService {
    ResponseResult getAll();
    ResponseResult save(RestaurantDTO restaurantDTO);
    ResponseResult getById(ObjectId id);
    ResponseResult update(RestaurantDTO restaurantDTO);
    ResponseResult deleteRestaurant(ObjectId id);
    ResponseResult searchByName(String name);
	ResponseResult getTrendingById(ObjectId id);

	ResponseResult getTrendingById(ObjectId id, double avgRating);
    ResponseResult uploadLogoById(ObjectId id, MultipartFile file);
    ResponseResult uploadImagesById(ObjectId id,MultipartFile[] files);
    ResponseResult deleteImages(ObjectId id, String type);
    ResponseResult getTopRestaurants(int n);
    ResponseResult getTopRestaurantsByTag(String tags, int n);
    ResponseResult searchRestaurants(String keyword, String location);

	ResponseResult getTrendingRestaurant();

	ResponseResult getTrendingRestaurantByTags(List<String> tags);

    ResponseResult searchTrendingRestaurants(String keyword, String location, String tagName);

    ResponseResult searchTopRestaurants(String keyword, String location, String tagName, int topNum);

    ResponseResult getTopRestaurantsBasedOnLocation(String location, int topNum);

//    ResponseResult getTopRestaurantsBasedOnLocation(String regionName, String areaName, int topNum);

    ResponseResult getTopRestaurantsBasedOnTagAndLocation(String tagName, String location, int topNum);
//    ResponseResult getTopRestaurantsBasedOnTagAndLocation(String tagName, String regionName, String areaName, int topNum);
	ResponseResult getTrendingRestaurantByTagsAndLocation(List<String> tags, String location);
    ResponseResult getRestaurantNames();
}