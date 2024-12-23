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
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.dto.requestdto.DishesDTO;
import com.kula.kula_project_backend.service.IDishService;
import com.kula.kula_project_backend.service.impl.AzureBlobServiceImpl;

/**
 * DishesController is a REST controller that provides endpoints for managing dishes.
 * This controller is used to save, delete, update, get and search dishes.
 * The controller uses the IDishService to interact with the service layer.
 * The controller uses the DishesDTO to save the dishes.
 * The controller uses the ResponseResult to return the result of the operation.
 */
@RestController
@RequestMapping("/dishes")
public class DishesController {

    @Autowired
    private IDishService dishService;

    @Autowired
    private AzureBlobServiceImpl blobStorageService; // for handling restaurant images

    /**
     * Endpoint to save a new dish.
     *
     * @param dishDTO The dish data transfer object containing the dish details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) DishesDTO dishDTO) {
        return dishService.save(dishDTO);
    }

    /**
     * Endpoint to get a dish by its id.
     *
     * @param id The id of the dish.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable ObjectId id) {
        return dishService.getById(id);
    }

    /**
     * Endpoint to update a dish.
     *
     * @param dishDTO The dish data transfer object containing the updated dish details.
     * @return The result of the update operation.
     */
    @PutMapping("/update")
    public ResponseResult update(@RequestBody @Validated(UpdateValidator.class) DishesDTO dishDTO) {
        return dishService.update(dishDTO);
    }

    /**
     * Endpoint to delete a dish by its id.
     *
     * @param id The id of the dish.
     * @return The result of the delete operation.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable ObjectId id) {
        return dishService.deleteDish(id);
    }

    /**
     * Endpoint to search for dishes in a restaurant.
     *
     * @param restaurantId The id of the restaurant.
     * @param dishName     The name of the dish.
     * @return The result of the search operation.
     */
    @GetMapping("/search")
    public ResponseResult searchDishesInRestaurant(
            @RequestParam ObjectId restaurantId,
            @RequestParam String dishName) {
        return dishService.searchDishesInRestaurant(restaurantId, dishName);
    }

    /**
     * Endpoint to get the average rating of a dish.
     *
     * @param dishId The id of the dish.
     * @return The result of the get operation.
     */
    @GetMapping("/getAverageRating/{dishId}")
    public ResponseResult getAverageRating(@PathVariable ObjectId dishId) {
        return dishService.getAverageRating(dishId);
    }

    @GetMapping("/all")
    public ResponseResult getAllDIshes() {
        return dishService.getAll();
    }

    /**
     * Endpoint to get a dish by its restaurant id.
     *
     * @param id The id of the dish.
     * @return The result of the get operation.
     */
    @GetMapping("/restaurant/{id}")
    public ResponseResult getByRestaurantID(@PathVariable ObjectId id) {
        return dishService.getByRestaurantId(id);
    }


    /**
     * Endpoint to get a trending dish by its id.
     *
     * @param id The id of the dish.
     * @return The result of the get operation.
     */
    @GetMapping("/trending/{id}")
    public ResponseResult getTrendingDishById(@PathVariable ObjectId id) {
        return dishService.getTrendingDishById(id);
    }

    /**
     * Endpoint to get a trending dish filtered by tags.
     *
     * @param tags The tag of the dish.
     * @return The result of the get operation.
     */
    @GetMapping("/trendingbytag")
    public ResponseResult getTrendingDishByTags(@RequestParam List<String> tags) {
        return dishService.getTrendingDishByTags(tags);
    }

    /**
     * Endpoint to get a trending dish.
     *
     * @return The result of the get operation.
     */
    @GetMapping("/trending")
    public ResponseResult getTrendingDish() {
        return dishService.getTrendingDish();
    }

    /**
     * Endpoint to get a trending dish filtered by tags and location.
     *
     * @param tags The tag of the dish.
     * @return The result of the get operation.
     */

    @GetMapping("/trending/filter")
    public ResponseResult getTrendingDishByTagsAndLocation(@RequestParam List<String> tags,
                                                           @RequestParam String location) {
        return dishService.getTrendingDishByTagsAndLocation(tags, location);

    }
        /**
         * Endpoint to upload dish images by its id.
         *
         * @param id    The id of the dish.
         * @param files The array of files to be uploaded.
         * @return The result of the upload, filenames if success or 400 if failure.
         */
        @PostMapping("/uploadImages/{id}")
        public ResponseResult uploadDishImages (@PathVariable ObjectId id, @RequestBody MultipartFile[]files){
            return dishService.uploadImagesById(id, files);
        }

        /**
         * Endpoint to delete all dish images by dish id.
         *
         * @param id The id of the dish.
         * @return The result of deletion.
         */
        @DeleteMapping("/deleteImages/{id}")
        public ResponseResult deleteALLImages (@PathVariable ObjectId id){
            return dishService.deleteAllImages(id);
        }

        /**
         * Endpoint to filter for dishes by tag.
         *
         * @param tagName The tag of the offer.
         * @return The result of the filter operation.
         */
        @GetMapping("/filter")
        public ResponseResult filterDishesByTag (@RequestParam String tagName){
            return dishService.filterDishesByTag(tagName);
        }

        /**
         * Endpoint to get the offers with top n (default 5) highest ratings.
         *
         * @return The result of the get operation.
         */
        @GetMapping("/top")
        public ResponseResult getTopDishes ( @RequestParam(defaultValue = "5") int topNum){
            return dishService.getTopDishes(topNum);
        }

        /**
         * Endpoint to get the offers with top n (default 5) highest ratings.
         *
         * @return The result of the get operation.
         */
        @GetMapping("/top/filter")
        public ResponseResult getTopDishesBasedOnTag (@RequestParam String tagName,
        @RequestParam(defaultValue = "10") int topNum){
            return dishService.getTopDishesBasedOnTag(tagName, topNum);
        }

        /**
         * Endpoint to get the dishes with top n (default 8) highest ratings based on location.
         *
         * @return The result of the get operation.
         */
        @GetMapping("/top/location")
        public ResponseResult getTopDishesBasedOnLocation (@RequestParam String location,
        @RequestParam(defaultValue = "8") int topNum){
            return dishService.getTopDishesBasedOnLocation(location, topNum);
        }

        /**
         * //     * Endpoint to get the dishes with top n (default 8) highest ratings based on tag and location.
         * //     * @return The result of the get operation.
         * //
         */
        @GetMapping("/top/filter/location")
        public ResponseResult getTopDishesBasedOnTagAndLocation (@RequestParam String tagName, @RequestParam String
        location,@RequestParam(defaultValue = "8") int topNum){
            return dishService.getTopDishesBasedOnTagAndLocation(tagName, location, topNum);
        }


        @GetMapping("/searchByKeywordLocation")
        public ResponseResult searchDishes (@RequestParam String keyword, @RequestParam String location){
            return dishService.searchDishes(keyword, location);
        }

        @GetMapping("/searchByKeyword")
        public ResponseResult findByDishNameContainingIgnoreCase (String name){
            return dishService.searchDishes(name);
        }

        @GetMapping("/searchByKeywordLocationTop")
        public ResponseResult searchTopDishes (@RequestParam String keyword, @RequestParam String
        location, @RequestParam String tagName,@RequestParam(defaultValue = "10") int topNum){
            return dishService.searchTopDishes(keyword, location, tagName, topNum);
        }

        @GetMapping("/searchTrending")
        public ResponseResult searchTrendingDishes (@RequestParam String keyword, @RequestParam String
        location, @RequestParam String tagName){
            return dishService.searchTrendingDishes(keyword, location, tagName);
        }

        /**
         * Endpoint to get dishes within a restaurant
         * @param id of restaurant
         * @return The result of the get operation
         * */
        @GetMapping("/namesInRestaurant/{id}")
        public ResponseResult getDishNames(@PathVariable  ObjectId id){
            return dishService.getDishesInRestaurant(id);
        }
    }

