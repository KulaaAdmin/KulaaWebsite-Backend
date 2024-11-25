package com.kula.kula_project_backend.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.OffersResponseDTOConverter;
import com.kula.kula_project_backend.common.converter.TopOffersResponseDTOConverter;
import com.kula.kula_project_backend.dao.*;

import com.kula.kula_project_backend.dto.responsedto.OffersResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.TopOffersResponseDTO;
import com.kula.kula_project_backend.entity.*;
import com.kula.kula_project_backend.service.IAzureBlobService;
import com.kula.kula_project_backend.service.IOfferService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.kula.kula_project_backend.dto.requestdto.OffersDTO;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.multipart.MultipartFile;


@Service
public class OffersServiceImpl implements IOfferService {
    @Autowired
    private OffersRepository offersRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private AreasRepository areasRepository;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private IAzureBlobService azureBlobService;
    @Autowired
    private OffersResponseDTOConverter offersResponseDTOConverter;
    @Autowired
    private TopOffersResponseDTOConverter topOffersResponseDTOConverter;
    // BlobServiceClient is the main interface for interacting with the Azure Blob Storage service.
    @Autowired
    private BlobServiceClient blobServiceClient;
    // The container name is used to create a container client
    @Value("${azure.blob.container-name}")
    private String containerName;
    // The endpoint is used to create a blob client
    @Value("${azure.blob.container-endpoint}")
    private String endPoint;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Retrieves all offers.
     * @return ResponseResult object containing status, message and all restaurants.
     */
    @Override
    public ResponseResult getAll() {
        List<Offers> offers = offersRepository.findAll();
        List<OffersResponseDTO> offerList = new ArrayList<>();

        if (!offers.isEmpty()) {
            for (Offers offer : offers) {
                OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer);
                offerList.add(dto);
            }
            return new ResponseResult(200, "success", offerList);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Retrieves an offer by its ID.
     * @param id The ID of the offer.
     * @return ResponseResult object containing status, message and the offer.
     */
    @Override
    public ResponseResult getById(ObjectId id) {      //Retrieves a offer by its ID.
        Optional<Offers> offer = offersRepository.findById(id);

        if(offer.isPresent()) {
            OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Filter offers by tag name.
     * @param tagName The tag of the offer.
     * @return ResponseResult object containing status, message and the offers.
     *  if no offers are found, return random num (default randomNum = 3) of offers.
     */
    @Override
    public ResponseResult filterByTag(String tagName, int randomNum) {
        // Step 1: Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {return new ResponseResult(400, "Tag not found");}
        // Step 2: Get the tag ID
        ObjectId tagId = tagOptional.get().getId();
        List<Offers> offers = offersRepository.findByTagsContaining(tagId);
        List<OffersResponseDTO> offerList = new ArrayList<>();

        if (!offers.isEmpty()) {
            for (Offers offer : offers) {
                OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer);
                offerList.add(dto);
            }
            return new ResponseResult(200, "success", offerList);
        }
        return getRandomOffers(randomNum);
    }


    /**
     * Retrieves top offers (default top 5).
     * @return ResponseResult object containing status, message and all restaurants.
     */
    @Override
    public ResponseResult getTopOffers(int topNum) {
        List<Offers> sortedOffers = offersRepository.sortedOffersByRestaurantRating();
//        int topNum = 5;
        List<Offers> topOffers = sortedOffers.subList(0, Math.min(topNum, sortedOffers.size()));
        List<TopOffersResponseDTO> offerList = new ArrayList<>();

        if (!topOffers.isEmpty()) {
            for (Offers offer : topOffers) {
                Optional<Restaurant> restaurant = restaurantRepository.findById(offer.getRestaurantId());
                TopOffersResponseDTO dto = topOffersResponseDTOConverter.convertToResponseDTO(offer);
                offerList.add(dto);
            }
            return new ResponseResult(200, "success", offerList);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Retrieves the top offers based on a given tag and the restaurant rating.
     * @param tagName The tag name used to filter offers.
     * @param topNum The number of top offers to return.
     * @return A `ResponseResult` containing a list of top offers as `TopOffersResponseDTO` objects if successful, or a failure status if no offers are found.
     */
    @Override
    public ResponseResult getTopOffersBasedOnTag(String tagName, int topNum) {
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {
            return new ResponseResult(400, "Tag not found");
        }
        ObjectId tagId = tagOptional.get().getId();
        List<Offers> sortedOffers = offersRepository.sortedOffersByRestaurantRatingBasedOnTag(tagId);
        List<Offers> topOffers = sortedOffers.subList(0, Math.min(topNum, sortedOffers.size()));

        if (!topOffers.isEmpty()) {
            List<OffersResponseDTO> offerList = topOffers.stream()
                    .map(offer -> offersResponseDTOConverter.convertToResponseDTO(offer))
                    .collect(Collectors.toList());
            return new ResponseResult(200, "success", offerList);
        }
        return new ResponseResult(400, "No offers found for the given tag");
    }

    /**
     * Retrieves the top N offers based on restaurant id.
     * @param restaurantId The id of restaurant used to filter offers.
     * @param topNum The number of offers to return.
     */
    @Override
    public ResponseResult getTopNOffersBasedOnRestaurantId(ObjectId restaurantId, int topNum){
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (!restaurant.isPresent()){return new ResponseResult(400,"Restaurant not found");}
        List<Offers> offers = offersRepository.findByRestaurantId(restaurantId);
        if (offers.isEmpty()){return new ResponseResult(400, "No restaurant found for the given restaurant id");}
        Collections.shuffle(offers);
        List<Offers> topOffers = offers.subList(0, Math.min(topNum, offers.size()));
        List<OffersResponseDTO> offerList = topOffers.stream()
                .map(offer -> offersResponseDTOConverter.convertToResponseDTO(offer))
                .collect(Collectors.toList());
        return new ResponseResult(200, "success", offerList);
    }

    /**
     * Upload an image for offer by its ID.
     *
     * @param id The ID of the restaurant.
     * @param file is the Object-classed filename.
     * @return ResponseResult object containing status, message and image filenames.
     */
    @Override
    public ResponseResult uploadImageById(ObjectId id, MultipartFile file) {
        Optional<Offers> offer = offersRepository.findById(id);
        if (offer.isPresent()) {
            ResponseResult uploadResult = azureBlobService.uploadFile(file);
            if (uploadResult.getCode() != 200) {return uploadResult;}
            String filename = uploadResult.getData().toString();
            deleteImage(id);  // Delete previous image
            offer.get().setImage(filename); // Set offer Image
            offersRepository.save(offer.get());  // Store modified offer back into database

            return new ResponseResult(200, "success add image to offer id: " + id, filename);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Delete the image for an offer by its ID.
     *
     * @param id The ID of the offer.
     * @return ResponseResult object containing status, message and image filename.
     */
    @Override
    public ResponseResult deleteImage(ObjectId id) {
        Optional<Offers> offer = offersRepository.findById(id);
        if (offer.isPresent()) {
            String filename = offer.get().getImage();
            // If there are images bind --> Delete from database and from Azure
            if (filename != null){
                // Step 1: Delete from database
                offer.get().setImage(null);
                offersRepository.save(offer.get());
                // Step 2: Delete from Azure
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                BlobClient blobClient = containerClient.getBlobClient(filename);
                // check if the blob exists
                if (blobClient.exists()) {
                    blobClient.delete();
                    System.out.println("Blob " + filename + " deleted successfully.");
                }
            }
            // If there is no image bind --> return 200 directly (idempotent)
            return new ResponseResult(200, "success delete image for offer id: " + id, filename);
        }
        return new ResponseResult(400, "fail finding offer");
    }

    /**
     * Saves a new offer.
     * @param offerDTO The DTO containing the offer details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(OffersDTO offerDTO){
        if (offerDTO.getId() != null) {    // Checks if the offer ID is null (indicating a new offer).
            return new ResponseResult(400, "ID should be null for a new offer");
        }
        Offers offer = convertToEntity(offerDTO, false);     // Converts the DTO to an entity and saves it to the database.
        offersRepository.save(offer);
        return new ResponseResult(200, "Offer saved successfully: " + offer.getId().toString());
    }

    /**
     * Updates an offer.
     * @param offerDTO The DTO containing the updated offer details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult update(OffersDTO offerDTO) {     //Checks if the offer exists by ID.
        if (offerDTO.getId() == null) {
            return new ResponseResult(400, "ID cannot be null for updating an offer");
        }
        Optional<Offers> offer = offersRepository.findById(offerDTO.getId());
        if (offer.isPresent()) {       //Updates the offer if it exists
            Offers updatedOffer = convertToEntity(offerDTO, true);
            updatedOffer.setId(offer.get().getId());
            offersRepository.save(updatedOffer);
            return new ResponseResult(200, "Offer updated successfully: " + updatedOffer.getId().toString());
        } else {
            return new ResponseResult(404, "Offer not found");
        }
    }

    /**
     * Deletes an offer by its ID.
     * @param id The ID of the offer.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteOffer(ObjectId id) {     //Deletes a restaurant by ID if it exists
        if (offersRepository.existsById(id)) {
            offersRepository.deleteById(id);
            return new ResponseResult(200, "Offer deleted successfully " + id);
        } else {
            return new ResponseResult(404, "Offer not found");
        }
    }

    // Helper method to convert DTO to Entity
    private Offers convertToEntity(OffersDTO offerDTO, Boolean is_update) {
        Offers offer = new Offers();
        if (!is_update) {
            offer.setId(offerDTO.getId());
            offer.setRestaurantId(offerDTO.getRestaurantId());
            offer.setOfferName(offerDTO.getOfferName());

            // Check if description is null, set default description if it is
            if (offerDTO.getDescription() != null) {
                offer.setDescription(offerDTO.getDescription());
            } else {
                offer.setDescription("No provided description");
            }

            /* Transfer tagName to tagId*/
            ArrayList<ObjectId> tagIds = new ArrayList<>();
            /* transfer tag id to tag name when expose */
            for (String tagName : offerDTO.getTags()){
                Optional<Tags> tag = tagsRepository.findByTagName(tagName);
                tag.ifPresent(tags -> tagIds.add(tags.getId()));
            }
            offer.setTags(tagIds);
            offer.setDishes(offerDTO.getDishes());
            offer.setOriginalPrice(offerDTO.getOriginalPrice());
            offer.setDiscountedPrice(offerDTO.getDiscountedPrice());
            offer.setTermsAndConditions(offerDTO.getTermsAndConditions());
            offer.setAvailable(offerDTO.getAvailable());
            offer.setBooking(offerDTO.getBooking());
            offer.setStartDate(offerDTO.getStartDate());
            offer.setEndDate(offerDTO.getEndDate());
        }else{
            Optional<Offers> exsiting_offer = offersRepository.findById(offerDTO.getId());
            offer.setId(offerDTO.getId());

            if (offerDTO.getRestaurantId() != null){
                offer.setRestaurantId(offerDTO.getRestaurantId());
            }else{
                offer.setRestaurantId(exsiting_offer.get().getRestaurantId());
            }

            if (offerDTO.getOfferName() != null){
                offer.setOfferName(offerDTO.getOfferName());
            }else{
                offer.setOfferName(exsiting_offer.get().getOfferName());
            }

            if (offerDTO.getDescription() != null) {
                offer.setDescription(offerDTO.getDescription());
            } else {
                offer.setDescription(exsiting_offer.get().getDescription());
            }

            if (offerDTO.getDishes() != null) {
                offer.setDishes(offerDTO.getDishes());
            } else {
                offer.setDishes(exsiting_offer.get().getDishes());
            }

            if (offerDTO.getOriginalPrice() != null) {
                offer.setOriginalPrice(offerDTO.getOriginalPrice());
            } else {
                offer.setOriginalPrice(exsiting_offer.get().getOriginalPrice());
            }

            if (offerDTO.getDiscountedPrice() != null) {
                offer.setDiscountedPrice(offerDTO.getDiscountedPrice());
            } else {
                offer.setDiscountedPrice(exsiting_offer.get().getDiscountedPrice());
            }

            if (offerDTO.getTermsAndConditions() != null) {
                offer.setTermsAndConditions(offerDTO.getTermsAndConditions());
            } else {
                offer.setTermsAndConditions(exsiting_offer.get().getTermsAndConditions());
            }

            if (offerDTO.getAvailable() != null) {
                offer.setAvailable(offerDTO.getAvailable());
            } else {
                offer.setAvailable(exsiting_offer.get().getAvailable());
            }

            if (offerDTO.getBooking() != null) {
                offer.setBooking(offerDTO.getBooking());
            } else {
                offer.setBooking(exsiting_offer.get().getBooking());
            }

//            if (offerDTO.getTags() != null) {
//                offer.setTags(offerDTO.getTags());
//            } else {
//                offer.setTags(exsiting_offer.get().getTags());
//            }


            offer.setImage(exsiting_offer.get().getImage());

            if (offerDTO.getStartDate() != null) {
                offer.setStartDate(offerDTO.getStartDate());
            } else {
                offer.setStartDate(exsiting_offer.get().getStartDate());
            }

            if (offerDTO.getEndDate() != null) {
                offer.setEndDate(offerDTO.getEndDate());
            } else {
                offer.setEndDate(exsiting_offer.get().getEndDate());
            }
        }
        return offer;
    }

    @Override
    public ResponseResult searchOffers(String keyword, String location, String tagName) {
        if (keyword == null) keyword = "";
        if (location == null) location = "";

        ObjectId tagId = null;
        if (tagName != null && !tagName.isEmpty()) {
            // Find the tag by name
            Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
            if (tagOptional.isPresent()) {
                tagId = tagOptional.get().getId();
            } else {
                return new ResponseResult(400, "Tag not found");
            }
        }

        List<Restaurant> restaurants;
        List<ObjectId> restaurantIds;

        if (!location.isEmpty()) {
            // Location is provided
            // Get locationId based on the provided location string
            ObjectId locationId;

            // Check if the location is an area or a region
            Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
            Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);

            if (areaOptional.isPresent()) {
                locationId = areaOptional.get().getId();
            } else if (regionOptional.isPresent()) {
                locationId = regionOptional.get().getId();
            } else {
                return new ResponseResult(404, "Location not found");
            }

            // Retrieve restaurants by location
            restaurants = restaurantRepository.filteredRestaurantsByLocation(locationId);

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No offers found in the specified location");
            }

        } else {
            // Location is not provided; retrieve all restaurants
            restaurants = restaurantRepository.findAll();

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No offers found");
            }
        }

        // Get the list of restaurant IDs
        restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        // Find offers matching the keyword, restaurant IDs, and tag if provided
        List<Offers> offers;
        if (tagId != null) {
            // Tag is provided, include tag filter
            offers = offersRepository.findByTagsContainingAndRestaurantIdIn(tagId,restaurantIds);
        } else {
            // No tag provided, use existing method
            offers = offersRepository.findByRestaurantIdIn(restaurantIds);
        }

        /* Filter offers by keyword */
        String finalKeyword = keyword.toLowerCase();
        // find based on offerName/tag/restaurantName/restaurantLocation.
        List<Offers> filteredOffers = offers.stream()
                .filter(offer ->{
                    // 1. Match offer's name
                    boolean offerNameMatch = offer.getOfferName().toLowerCase().contains(finalKeyword);
                    // 2. Match offer tag
                    boolean tagMatch = 	offer.getTags().stream()
                            .map(offerTagId -> tagsRepository.findById(offerTagId))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Tags::getTagName)
                            .anyMatch(offerTagName -> offerTagName.toLowerCase().contains(finalKeyword));

                    Optional<Restaurant> offerRestaurant = restaurantRepository.findById(offer.getRestaurantId());
                    boolean restaurantNameMatch = false;
                    boolean restaurantLocationMatch = false;
                    if (offerRestaurant.isPresent()){
                        // 3. Match offer corresponding restaurant name
                        restaurantNameMatch = offerRestaurant.get().getName().toLowerCase().contains(finalKeyword);
                        // 4. Match offer corresponding restaurant location
                        Optional<String> regionName = Optional.empty();
                        Optional<String> areaName = Optional.empty();
                        if (offerRestaurant.get().getLocation().get("region") != null) {
                            regionName = regionsRepository.findById(offerRestaurant.get().getLocation().get("region"))
                                    .map(Regions::getRegionName);
                        }
                        if (offerRestaurant.get().getLocation().get("area") != null) {
                            areaName = areasRepository.findById(offerRestaurant.get().getLocation().get("area"))
                                    .map(Areas::getAreaName);
                        }
                        restaurantLocationMatch = regionName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false) ||
                                areaName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false);
                    }
                    return offerNameMatch || tagMatch || restaurantNameMatch || restaurantLocationMatch;

                }).collect(Collectors.toList());


        // Map offers to DTOs
        List<OffersResponseDTO> dtos = filteredOffers.stream()
                .map(offer -> {
                    Optional<Restaurant> restaurantOpt = restaurants.stream()
                            .filter(r -> r.getId().equals(offer.getRestaurantId()))
                            .findFirst();
                    Restaurant restaurant = restaurantOpt.orElse(null);
                    return offersResponseDTOConverter.convertToResponseDTO(offer);
                })
                .collect(Collectors.toList());

        // Check if no offers found
        if (dtos.isEmpty()) {
            // Fetch 3 random offers using MongoTemplate
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.sample(3)
            );
            AggregationResults<Offers> aggregationResults = mongoTemplate.aggregate(aggregation, "offers", Offers.class);
            List<Offers> randomOffers = aggregationResults.getMappedResults();
            if (randomOffers.isEmpty()) {
                return new ResponseResult(404, "No offers available at the moment.");
            }
            // Map random offers to DTOs
            dtos = randomOffers.stream()
                    .map(offer -> {
                        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(offer.getRestaurantId());
                        Restaurant restaurant = restaurantOpt.orElse(null);
                        return offersResponseDTOConverter.convertToResponseDTO(offer);
                    })
                    .collect(Collectors.toList());
            return new ResponseResult(200, "No offers found matching your search criteria. Here are some random offers.", dtos);
        }
        return new ResponseResult(200, "success", dtos);
    }

    /**
     * Endpoint to filter the offers with based on specific location.
     * @param location The location of the offer restaurant.
     * @param randomNum The number of random offers to get if no tag found in specific location (default 3).
     * @return The offers based on specific location (region or area)
     * if no offers found in specific location return random num (default randomNum = 3) of offers.
     * if location is not in areas or regions return error message.
     */
    @Override
    public ResponseResult filterByLocation(String location, int randomNum) {
        // First, find restaurants matching the location
        ObjectId locationId;
        // Check if filter by area or region
        Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
        Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);
        if (areaOptional.isPresent()) {
            locationId = areaOptional.get().getId();
        } else if (regionOptional.isPresent()) {
            locationId = regionOptional.get().getId();
        } else {
            return new ResponseResult(400, "Location not found");
        }
        List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

        List<ObjectId> restaurantIds = restaurantsByLocation.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        // Find offers matching the keyword and restaurant IDs
        List<Offers> offers = offersRepository.findByRestaurantIdIn(restaurantIds);

        List<OffersResponseDTO> offerList = new ArrayList<>();

        if (!offers.isEmpty()) {
            for (Offers offer : offers) {
                OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer);
                offerList.add(dto);
            }
            return new ResponseResult(200, "success", offerList);
        }
        return getRandomOffers(randomNum);
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
    @Override
    public ResponseResult filterByTagAndLocation(String tagName, String location, int randomNum) {
        // First, find restaurants matching the location
        ObjectId locationId;
        // Check if filter by area or region
        Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
        Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);
        if (areaOptional.isPresent()) {
            locationId = areaOptional.get().getId();
        } else if (regionOptional.isPresent()) {
            locationId = regionOptional.get().getId();
        } else {
            return new ResponseResult(400, "Location not found");
        }
        List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

        List<ObjectId> restaurantIds = restaurantsByLocation.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        // Second, find offers matching the tagName
        // Step 1: Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {return new ResponseResult(400, "Tag not found");}
        // Step 2: Get the tag ID
        ObjectId tagId = tagOptional.get().getId();

        // Find offers matching the keyword and restaurant IDs
        List<Offers> offers = offersRepository.findByTagsContainingAndRestaurantIdIn(tagId, restaurantIds);

        List<OffersResponseDTO> offerList = new ArrayList<>();

        if (!offers.isEmpty()) {
            for (Offers offer : offers) {
                OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer);
                offerList.add(dto);
            }
            return new ResponseResult(200, "success", offerList);
        }
        return getRandomOffers(randomNum);
    }

    /**
     * Endpoint to random get specific num (default 3) of offers.
     * @param randomNum The number of random offers to get (default 3).
     * @return The result of the get operation.
     */
    @Override
    public ResponseResult getRandomOffers(int randomNum) {
        List<Offers> offers = offersRepository.findAll();
        List<OffersResponseDTO> offerListDTO = new ArrayList<>();

        if (!offers.isEmpty()) {
            // Shuffle the offers to randomize
            Collections.shuffle(offers);

            // Limit the number of offers to randomNum or the size of the list, whichever is smaller
            List<Offers> randomOffers = new ArrayList<>();

            // Add offers to the list until we reach the desired randomNum
            while (randomOffers.size() < randomNum) {
                randomOffers.addAll(offers);
            }

            // Trim the list to exactly randomNum items
            randomOffers = randomOffers.subList(0, randomNum);

            for (Offers offer : randomOffers) {
                OffersResponseDTO dto = offersResponseDTOConverter.convertToResponseDTO(offer);
                offerListDTO.add(dto);
            }
            return new ResponseResult(200, "success", offerListDTO);
        }
        return new ResponseResult(400, "fail");
    }
}
