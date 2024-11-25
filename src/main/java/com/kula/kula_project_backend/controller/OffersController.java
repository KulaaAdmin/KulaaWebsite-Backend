package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.OffersDTO;
import com.kula.kula_project_backend.service.IOfferService;
import com.kula.kula_project_backend.service.impl.AzureBlobServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/offers")
public class    OffersController {
    @Autowired
    private IOfferService offerService;

    @Autowired
    private AzureBlobServiceImpl blobStorageService; // for handling offer images

    /**
     * Endpoint to get all offers.
     * @return The result of the get operation.
     */
    @GetMapping("/all")
    public ResponseResult getAllOffers() {
        return offerService.getAll();
    }

    /**
     * Endpoint to get an offer by its id.
     * @param id The id of the offer.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult getOfferById(@PathVariable ObjectId id) {
        return offerService.getById(id);
    }

    /**
     * Endpoint to get random N offers by restaurant id.
     * @param restaurantId The id of the restaurant.
     * @return The random N offers by restaurant id.
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseResult getNOffersBasedOnRestaurantId(@PathVariable ObjectId restaurantId, @RequestParam(defaultValue = "5") int topNum){
        return offerService.getTopNOffersBasedOnRestaurantId(restaurantId,topNum);
    }

    /**
     * Endpoint to filter offers by tag.
     * @param tagName The tag of the offer.
     * @param randomNum The number of random offers to get if no offers found in specific tag (default 3).
     * @return The result of the filter operation if no offers found in specific tag (default 3).
     */
    @GetMapping("/filter")
    public ResponseResult filterOffersByTag(@RequestParam String tagName, @RequestParam(defaultValue ="3") int randomNum) {
        return offerService.filterByTag(tagName, randomNum);
    }

    /**
     * Endpoint to get the offers with top-N highest ratings.
     * @return The result of the get operation.
     */
    @GetMapping("/top")
    public ResponseResult getTopOffers(@RequestParam(defaultValue ="5") int topNum){
        return offerService.getTopOffers(topNum);
    }

    /**
     * Endpoint to get the offers with top n (default 5) highest ratings based on specific tag.
     * @param tagName The tag of the offer.
     * @param topNum The number of top offers to get (default 5).
     * @return The result of the get operation.
     */
    @GetMapping("/top/filter")
    public ResponseResult getTopOffersBasedOnTag(@RequestParam String tagName, @RequestParam(defaultValue ="5") int topNum) {
        return offerService.getTopOffersBasedOnTag(tagName, topNum);
    }

    /**
     * Endpoint to upload offer image by its id.
     * @param id The id of the offer.
     * @param file The file to be uploaded.
     * @return The result of the upload, filenames if success or 400 if failure.
     */
    @PostMapping("/uploadImage/{id}")
    public ResponseResult uploadOfferImage(@PathVariable ObjectId id, @RequestBody MultipartFile file) {
        return offerService.uploadImageById(id,file);
    }

    /**
     * Endpoint to delete offer image by Offer id.
     * @param id The id of the offer.
     * @return The result of deletion.
     */
    @DeleteMapping("/deleteImage/{id}")
    public ResponseResult deleteImage(@PathVariable ObjectId id) {
        return offerService.deleteImage(id);
    }

    /**
     * Endpoint to save a new offer.
     * @param offerDTO The offer data transfer object containing the offer details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult saveOffer(@RequestBody @Validated(SaveValidator.class) OffersDTO offerDTO) {
        return offerService.save(offerDTO);
    }

    /**
     * Endpoint to update an offer.
     * @param offerDTO The offer data transfer object containing the updated offer details.
     * @return The result of the update operation.
     */
    @PutMapping("/update")
    public ResponseResult updateOffer(@RequestBody @Validated OffersDTO offerDTO) {
        return offerService.update(offerDTO);
    }

    /**
     * Endpoint to delete an offer by its id.
     * @param id The id of the offer.
     * @return The result of the delete operation.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteOffer(@PathVariable ObjectId id) {
        return offerService.deleteOffer(id);
    }

    @GetMapping("/search")
    public ResponseResult searchOffers(@RequestParam(defaultValue ="") String  keyword, @RequestParam(defaultValue ="") String location, @RequestParam(defaultValue ="") String tagName) {
        return offerService.searchOffers(keyword, location, tagName);
    }

    /**
     * Endpoint to filter the offers with based on specific location.
     * @param location The location of the offer restaurant.
     * @param randomNum The number of random offers to get if no tag found in specific location (default 3).
     * @return The offers based on specific location (region or area)
     * if no offers found in specific location return random num (default randomNum = 3) of offers.
     * if location is not in areas or regions return error message.
     */
    @GetMapping("/location")
    public ResponseResult filterByLocation(@RequestParam String location, @RequestParam(defaultValue ="3") int randomNum){
        return offerService.filterByLocation(location, randomNum);
    }

    /**
     * Endpoint to filter the offers with based on specific tag and location.
     * @param tagName The tagName of the offer.
     * @param location The location of the offer restaurant.
     * @param randomNum The number of random offers to get if no offers found in specific tag and location (default 3).
     * @return The offers based on specific tag and location (area or region)
     * if no offers found in specific tag and location return random num (default randomNum = 3) of offers.
     * if location is not in areas or regions return error message.
     */
    @GetMapping("/tag/location")
    public ResponseResult filterByTagAndLocation(@RequestParam String tagName,  @RequestParam String location, @RequestParam(defaultValue ="3") int randomNum){
        return offerService.filterByTagAndLocation(tagName,  location, randomNum);
    }

    /**
     * Endpoint to random get specific num (default 3) of offers.
     * @param randomNum The number of random offers to get (default 3).
     * @return The result of the get operation.
     */
    @GetMapping("/randomGet")
    public ResponseResult getRandomOffers(@RequestParam(defaultValue ="3") int randomNum) {
        return offerService.getRandomOffers(randomNum);
    }
}
