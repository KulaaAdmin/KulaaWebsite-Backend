package com.kula.kula_project_backend.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.RestaurantDTO;
import com.kula.kula_project_backend.service.IRestaurantService;

/**
 * RestaurantController is a REST controller that provides endpoints for managing restaurants.
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    /**
     * Endpoint to get all restaurants.
     * @return The result of the get operation.
     */
    @GetMapping("/all")
    public ResponseResult getAllRestaurants() {
        return restaurantService.getAll();
    }
    /**
     * Endpoint to save a new restaurant.
     * @param restaurantDTO The restaurant data transfer object containing the restaurant details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult saveRestaurant(@RequestBody @Validated RestaurantDTO restaurantDTO) {
        return restaurantService.save(restaurantDTO);
    }
    /**
     * Endpoint to get a restaurant by its id.
     * @param id The id of the restaurant.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult getRestaurantById(@PathVariable ObjectId id) {
        return restaurantService.getById(id);
    }
    /**
     * Endpoint to update a restaurant.
     * @param restaurantDTO The restaurant data transfer object containing the updated restaurant details.
     * @return The result of the update operation.
     */
    @PutMapping("/update")
    public ResponseResult updateRestaurant(@RequestBody @Validated RestaurantDTO restaurantDTO) {
        return restaurantService.update(restaurantDTO);
    }
    /**
     * Endpoint to delete a restaurant by its id.
     * @param id The id of the restaurant.
     * @return The result of the delete operation.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteRestaurant(@PathVariable ObjectId id) {
        return restaurantService.deleteRestaurant(id);
    }
    /**
     * Endpoint to search for restaurants by name.
     * @param name The name of the restaurant.
     * @return The result of the search operation.
     */
//    @GetMapping("/search")
//    public ResponseResult searchRestaurants(@RequestParam String name) {
//        return restaurantService.searchByName(name);
//    }

	/**
	 * Endpoint to get trending restaurant.
	 * 
	 * @return The result of the get operation.
	 */
	@GetMapping("/trending")
	public ResponseResult getTrendingRestaurant() {
		return restaurantService.getTrendingRestaurant();
	}

	/**
	 * Endpoint to get trending restaurant by tags.
	 * 
	 * @return The result of the get operation.
	 */
	@GetMapping("/trendingbytag")
	public ResponseResult getTrendingRestaurant(@RequestParam List<String> tags) {
		return restaurantService.getTrendingRestaurantByTags(tags);
	}

	/**
	 * Endpoint to get trending restaurant by its id.
	 * 
	 * @param id The id of the restaurant.
	 * @return The result of the get operation.
	 */
	@GetMapping("/trending/{id}")
	public ResponseResult getTrendingRestaurantById(@PathVariable ObjectId id) {
		return restaurantService.getTrendingById(id);
	}

	/**
	 * Endpoint to get trending restaurant by its id.
	 *
	 * @return The result of the get operation.
	 */
	@GetMapping("/trending/filter")
	public ResponseResult getTrendingRestaurantByTagsAndLocation(@RequestParam List<String> tags,
			@RequestParam String location) {
		return restaurantService.getTrendingRestaurantByTagsAndLocation(tags, location);
	}

    /**
     * Endpoint to upload restaurant logo by its id.
     * @param id The id of the offer.
     * @param file The file to be uploaded.
     * @return The result of the upload, filenames if success or 400 if failure.
     */
    @PostMapping("/uploadLogo/{id}")
    public ResponseResult uploadRestaurantLogo(@PathVariable ObjectId id, @RequestBody MultipartFile file) {
        return restaurantService.uploadLogoById(id,file);
    }

    /**
     * Endpoint to upload restaurant images by its id.
     * @param id The id of the restaurant.
     * @param files The array of files to be uploaded.
     * @return The result of the upload, filenames if success or 400 if failure.
     */
    @PostMapping("/uploadImages/{id}")
    public ResponseResult uploadRestaurantImages(@PathVariable ObjectId id, @RequestBody MultipartFile[] files) {
        return restaurantService.uploadImagesById(id,files);
    }


    /**
     * Endpoint to delete restaurant logo by Restaurant id.
     *
     * @param id The id of the restaurant.
     * @return The result of deletion.
     */
    @DeleteMapping("/deleteLogo/{id}")
    public ResponseResult deleteLogoImage(@PathVariable ObjectId id) {
        return restaurantService.deleteImages(id, "logo");
    }

    /**
     * Endpoint to delete all restaurant images by Restaurant id.
     *
     * @param id The id of the restaurant.
     * @return The result of deletion.
     */
    @DeleteMapping("/deleteImages/{id}")
    public ResponseResult deleteALLImages(@PathVariable ObjectId id) {
        return restaurantService.deleteImages(id, "images");
    }

    // API to get top n restaurants (defaults to 3)
    @GetMapping("/top")
    public ResponseResult getTopRestaurants(@RequestParam(defaultValue = "3") int topNum) {
        return restaurantService.getTopRestaurants(topNum);
    }

    // API to get top n restaurants filtered by tags (defaults to 5)
    @GetMapping("/top/filter")
    public ResponseResult getTopRestaurantsByTag(@RequestParam String tagName, @RequestParam(defaultValue = "5") int topNum) {
        return restaurantService.getTopRestaurantsByTag(tagName, topNum);
    }

    @GetMapping("/search")
    public ResponseResult searchRestaurants(@RequestParam String keyword, @RequestParam String location) {
        return restaurantService.searchRestaurants(keyword, location);
    }

    @GetMapping("/searchTrending")
    public ResponseResult searchTrendingRestaurants(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "") String location, @RequestParam(defaultValue = "") String tagName) {
        return restaurantService.searchTrendingRestaurants(keyword, location, tagName);
    }

    @GetMapping("/searchTop")
    public ResponseResult searchTopRestaurants(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "") String location, @RequestParam(defaultValue = "") String tagName, @RequestParam(defaultValue ="10") int topNum) {
        return restaurantService.searchTopRestaurants(keyword, location, tagName, topNum);
    }

    /**
     * Endpoint to get the restaurants with top n (default 6) highest ratings based on location.
     * @return The result of the get operation.
     */
    @GetMapping("/top/location")
    public ResponseResult getTopRestaurantsBasedOnLocation(@RequestParam String location, @RequestParam(defaultValue ="6") int topNum){
        return restaurantService.getTopRestaurantsBasedOnLocation(location, topNum);
    }

    /**
     * Endpoint to get the restaurants with top n (default 6) highest ratings based on tag and location.
     * @return The result of the get operation.
     */
    @GetMapping("/top/filter/location")
    public ResponseResult getTopRestaurantsBasedOnTagAndLocation(@RequestParam String tagName,  @RequestParam String location, @RequestParam(defaultValue ="6") int topNum){
        return restaurantService.getTopRestaurantsBasedOnTagAndLocation(tagName, location, topNum);
    }
}
