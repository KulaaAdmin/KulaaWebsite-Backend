package com.kula.kula_project_backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.DishesDTO;

public interface IDishService {
    ResponseResult getAll();

    ResponseResult save(DishesDTO dishDTO);

    ResponseResult getById(ObjectId id);

	ResponseResult getByRestaurantId(ObjectId id);

    ResponseResult update(DishesDTO dishDTO);

    ResponseResult deleteDish(ObjectId id);

    ResponseResult searchDishesInRestaurant(ObjectId restaurantId, String dishName);

    ResponseResult getAverageRating(ObjectId dishId);

	ResponseResult getTrendingDishById(ObjectId id);

	ResponseResult getTrendingDishById(ObjectId id, double avgRating);

    ResponseResult uploadImagesById(ObjectId id, MultipartFile[] files);

    ResponseResult deleteAllImages(ObjectId id);

    ResponseResult filterDishesByTag(String tag);

    ResponseResult getTopDishes(int topNum);

    ResponseResult getTopDishesBasedOnTag (String tag, int topNum);

    ResponseResult searchDishes(String keyword, String location);
    ResponseResult searchDishes(String name);
    ResponseResult searchTopDishes(String keyword, String location, String tagName, int topNum);
	ResponseResult getTrendingDish();

	ResponseResult getTrendingDishByTags(List<String> tags);

    ResponseResult searchTrendingDishes(String keyword, String location, String tagName);

    ResponseResult getTopDishesBasedOnLocation(String location, int topNum);

    ResponseResult getTopDishesBasedOnTagAndLocation(String tagName,  String location, int topNum);

	ResponseResult getTrendingDishByTagsAndLocation(List<String> tags, String location);

    ResponseResult getDishesInRestaurant(ObjectId restaurantId);
}
