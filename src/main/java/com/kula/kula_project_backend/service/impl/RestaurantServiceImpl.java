package com.kula.kula_project_backend.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
//import static com.kula.kula_project_backend.common.converter.DishesResponseDTOConverter.convertToResponseDTO;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kula.kula_project_backend.common.PostStats;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.DishesResponseDTOConverter;
import com.kula.kula_project_backend.common.converter.RestaurantsResponseDTOConverter;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.DishesRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.RestaurantRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.requestdto.RestaurantDTO;
import com.kula.kula_project_backend.dto.responsedto.CrossCollectionResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.DishesResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.RestaurantsResponseDTO;
import com.kula.kula_project_backend.entity.Areas;
import com.kula.kula_project_backend.entity.Dishes;
import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.entity.Regions;
import com.kula.kula_project_backend.entity.Restaurant;
import com.kula.kula_project_backend.entity.Tags;
import com.kula.kula_project_backend.service.IAzureBlobService;
import com.kula.kula_project_backend.service.IRestaurantService;


/**
 * Service implementation for Restaurant operations.
 */
@Service
public class RestaurantServiceImpl implements IRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
	@Autowired
	private DishesRepository dishRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private IAzureBlobService azureBlobService;
    @Autowired
    private DishesResponseDTOConverter dishesResponseDTOConverter;
    @Autowired
    private RestaurantsResponseDTOConverter restaurantsResponseDTOConverter;
	@Autowired
	private MongoTemplate mongoTemplate;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private AreasRepository areasRepository;

	private static final Logger logger = LoggerFactory.getLogger(DishServiceImpl.class);
    /**
     * Retrieves all restaurants.
     * @return ResponseResult object containing status, message and all restaurants.
     */
    @Override
    public ResponseResult getAll() {
		// return new ResponseResult(200, "success", restaurantRepository.findAll());
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<RestaurantsResponseDTO> restuarntList = new ArrayList<>();
		if (!restaurants.isEmpty()) {
			for (Restaurant restaurant : restaurants) {
				RestaurantsResponseDTO dto = restaurantsResponseDTOConverter.convertToResponseDTO(restaurant);
				restuarntList.add(dto);
			}
			return new ResponseResult(200, "success", restuarntList);
		}
		return new ResponseResult(400, "fail");
    }
    /**
     * Saves a new restaurant or updates an existing one.
     * @param restaurantDTO The DTO containing the restaurant details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(RestaurantDTO restaurantDTO) {
        if (restaurantDTO.getId() != null) {    //Checks if the restaurant ID is null (indicating a new restaurant).
            return new ResponseResult(400, "ID should be null for a new restaurant");
        }
        Restaurant restaurant = convertToEntity(restaurantDTO);     //Converts the DTO to an entity and saves it to the database.
        restaurantRepository.save(restaurant);
        return new ResponseResult(200, "Restaurant saved successfully", restaurant.getId().toString());
    }
    /**
     * Retrieves a restaurant by its ID.
     * @param id The ID of the restaurant.
     * @return ResponseResult object containing status, message and the restaurant.
     */
    @Override
    public ResponseResult getById(ObjectId id) {      //Retrieves a restaurant by its ID.
        Optional<Restaurant> restaurants = restaurantRepository.findById(id);
        if(restaurants.isPresent()) {
            RestaurantsResponseDTO dto = restaurantsResponseDTOConverter.convertToResponseDTO(restaurants.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Updates a restaurant.
     * @param restaurantDTO The DTO containing the updated restaurant details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult update(RestaurantDTO restaurantDTO) {     //Checks if the restaurant exists by ID.
        if (restaurantDTO.getId() == null) {
            return new ResponseResult(400, "ID cannot be null for updating a restaurant");
        }
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantDTO.getId());
        if (restaurantOptional.isPresent()) {       //Updates the restaurant if it exists
            Restaurant updatedRestaurant = convertToEntity(restaurantDTO);
            restaurantRepository.save(updatedRestaurant);
            return new ResponseResult(200, "Restaurant updated successfully", updatedRestaurant.getId());
        } else {
            return new ResponseResult(404, "Restaurant not found");
        }
    }
    /**
     * Deletes a restaurant by its ID.
     * @param id The ID of the restaurant.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteRestaurant(ObjectId id) {     //Deletes a restaurant by ID if it exists
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return new ResponseResult(200, "Restaurant deleted successfully");
        } else {
            return new ResponseResult(404, "Restaurant not found");
        }
    }
    /**
     * Searches for restaurants by name.
     * @param name The name of the restaurant.
     * @return ResponseResult object containing status, message and the restaurants.
     */
    @Override
    public ResponseResult searchByName(String name) {
        List<Restaurant> restaurants = restaurantRepository.findByNameLikeIgnoreCase(name);
        List<RestaurantsResponseDTO> dtos = restaurants.stream()
                .map(restaurantsResponseDTOConverter::convertToResponseDTO)  // 使用类名::方法名访问静态方法
                .collect(Collectors.toList());
        return new ResponseResult(200, "success", dtos);
    }


    // Helper method to convert DTO to Entity
    private Restaurant convertToEntity(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantDTO.getId());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setEmail(restaurantDTO.getEmail());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setOpeningHours(restaurantDTO.getOpeningHours());
		restaurant.setZipCode(restaurantDTO.getZipCode());
		restaurant.setLatitude(restaurantDTO.getLatitude());
		restaurant.setLongitude(restaurantDTO.getLongitude());
		restaurant.setCategory(restaurantDTO.getCategory());
		restaurant.setDescription(restaurantDTO.getDescription());

		Map<String, ObjectId> location = new HashMap<>();
		location.put("region", restaurantDTO.getRegion());
		location.put("area", restaurantDTO.getArea());
		restaurant.setLocation(location);

        restaurant.setRating(restaurantDTO.getRating());
        restaurant.setImages(restaurantDTO.getImages());
        restaurant.setLogo(restaurantDTO.getLogo());

        /* Transfer tagName to tagId*/
        ArrayList<ObjectId> tagIds = new ArrayList<>();
        /* transfer tag id to tag name when expose */
        for (String tagName : restaurantDTO.getTags()){
            Optional<Tags> tag = tagsRepository.findByTagName(tagName);
            tag.ifPresent(tags -> tagIds.add(tags.getId()));
        }
        restaurant.setTags(tagIds);

        return restaurant;
    }

	/**
	 * Retrieves a trending restaurant
	 * 
	 * @return ResponseResult object containing status, message and the restaurant
	 *         and two additional dishes.
	 */

	@Override
	public ResponseResult getTrendingRestaurant() {
		ObjectId restauarntId;

		// Define the date 14 days ago from now
		LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);

		// Aggregation pipeline
		Aggregation aggregation = Aggregation.newAggregation(
				// Match posts from the last 14 days
				Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo.atZone(ZoneOffset.UTC).toInstant())
						.and("restaurant_id").exists(true)),

				// Group by restaurant_id and calculate the count and average rating
				Aggregation.group("restaurant_id") // Group by restaurant_id
						.first("restaurant_id").as("entityId") // Keep the restaurant_id as entityId
						.count().as("postCount") // Count the posts
						.avg("rating").as("avgRating"), // Calculate the average rating

				// Filter by average rating > 4.5
				Aggregation.match(Criteria.where("avgRating").gt(4.5)),

				// Sort by post count in descending order
				Aggregation.sort(Sort.Direction.DESC, "postCount"),

				// Limit the result to 1 (most posts)
				Aggregation.limit(1));

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);

		List<PostStats> stats = results.getMappedResults();
		// logger.info("getTrendingRestaurant -- stats: {}", stats);

		// Return the restaurant_id with the most posts if available
		if (!stats.isEmpty()) {
			restauarntId = stats.get(0).getEntityId();
			// results.getUniqueMappedResult().getEntityId();
			// logger.info("getTrendingRestaurant -- id: {}", restauarntId);
			if (restauarntId != null) {
			return getTrendingById(restauarntId);
			}
		}

		return new ResponseResult(400, "No restaurant satisfies requirements for trending restaurant");
	}

	/**
	 * Retrieves a trending restaurant filtered by tags
	 * 
	 * @return ResponseResult object containing status, message and the restaurant
	 *         and two additional dishes.
	 */
	@Override
	public ResponseResult getTrendingRestaurantByTags(List<String> tags) {
		if (tags == null || tags.isEmpty()) {
			// Handle empty tags (e.g., return all results, or an empty list)
			return new ResponseResult(400, "No tag is provided");
		}

		// transfer tag names from string to
		List<ObjectId> tagIds = new ArrayList<>();
		for (String tagName : tags) {
			Optional<Tags> tag = tagsRepository.findByTagName(tagName);

			tag.ifPresent(t -> tagIds.add(t.getId()));

		}
		if (tagIds == null || tagIds.isEmpty()) {
			// Handle if no tag is found from Tags collection based on tag name string
			return new ResponseResult(400, "Tags provided are not valid");
		}

		LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);

		Aggregation aggregation = Aggregation.newAggregation(
				// Match posts updated within the last 14 days
				Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo.atZone(ZoneOffset.UTC).toInstant())
						.and("restaurant_id").exists(true)
						.and("tags").all(tagIds)),

				// Group by restaurant_id and count the number of posts, also calculate the
				// average
				// rating
				Aggregation.group("restaurant_id").first("restaurant_id").as("entityId").count().as("postCount")
						.avg("rating").as("avgRating")
				,

				// Filter for average rating > 4.5
				Aggregation.match(Criteria.where("avgRating").gt(4.5)),

				// Sort by post count in descending order
				Aggregation.sort(Sort.Direction.DESC, "postCount"),

				// Limit the result to 1 (most posts)
				Aggregation.limit(1));

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		List<PostStats> stats = results.getMappedResults();

		// Return the restaurant_id with the most posts if available
		if (!stats.isEmpty()) {
			// logger.info("getTrendingRestaurantbyTag -- id: {}",
			ObjectId RestaurantId = stats.get(0).getEntityId();
			double avgRating = stats.get(0).getAvgRating();
			// results.getUniqueMappedResult().getEntityId();
			if (RestaurantId != null) {
				return getTrendingById(RestaurantId, avgRating);

			}
		}

		return new ResponseResult(400, "No dish satisfies requirements for trending dish");
	}
	/**
	 * Retrieves a trending restaurant by its ID.
	 * 
	 * @param id The ID of the restaurant.
	 * @return ResponseResult object containing status, message and the restaurant
	 *         and two additional dishes.
	 */
	@Override
	public ResponseResult getTrendingById(ObjectId id) {
		List<Object> trendingRestaurant = new ArrayList<>();
		Optional<Restaurant> restaurants = restaurantRepository.findById(id);
		if (restaurants.isPresent()) {
			// rating from posts - recent 14 days
			Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

			// Create the query to find related posts based on dishId and updated_at
			Query query = Query.query(Criteria.where("restaurantId").is(id).and("updated_at").gte(fourteenDaysAgo));

			// Execute the query and retrieve related posts
			List<Posts> relatedPosts = mongoTemplate.find(query, Posts.class);
			double averageRating = relatedPosts.stream().mapToDouble(Posts::getRating).average().orElse(0);


			// add rating from posts - recent 14 days
			CrossCollectionResponseDTO ratingDTO = new CrossCollectionResponseDTO(averageRating);
			trendingRestaurant.add(ratingDTO);

			RestaurantsResponseDTO dto = restaurantsResponseDTOConverter.convertToResponseDTO(restaurants.get());
			trendingRestaurant.add(dto);
			List<Dishes> dishes = dishRepository.findByRestaurantId(id);
			if (!dishes.isEmpty()) {
				// select 2 top dishes from the same restaurant with highest rating
				List<Dishes> topDishes = dishes.stream()
						.sorted(Comparator.comparingDouble(Dishes::getRating).reversed()) // Sort by rating in
																									// descending order
						.limit(2) // Limit to top 2
						.collect(Collectors.toList()); // Collect the result as a list

				for (Dishes dish : topDishes) {
					DishesResponseDTO dtoDishes = dishesResponseDTOConverter.convertToResponseDTO(dish);
					trendingRestaurant.add(dtoDishes);

				}
				return new ResponseResult(200, "success", trendingRestaurant);
			}
			return new ResponseResult(200, "success but there is no dish available for the restaurant",
					trendingRestaurant);
		}
		return new ResponseResult(400, "fail");
	}

	/**
	 * Retrieves a trending restaurant by its ID.
	 * 
	 * @param id The ID of the restaurant.
	 * @return ResponseResult object containing status, message and the restaurant
	 *         and two additional dishes.
	 */
	@Override
	public ResponseResult getTrendingById(ObjectId id, double avgRating) {
		List<Object> trendingRestaurant = new ArrayList<>();
		Optional<Restaurant> restaurants = restaurantRepository.findById(id);
		if (restaurants.isPresent()) {
			// rating from posts - recent 14 days
			// Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

			// Create the query to find related posts based on dishId and updated_at
			// Query query =
			// Query.query(Criteria.where("restaurantId").is(id).and("updated_at").gte(fourteenDaysAgo));

			// Execute the query and retrieve related posts
			// List<Posts> relatedPosts = mongoTemplate.find(query, Posts.class);
			// double averageRating =
			// relatedPosts.stream().mapToDouble(Posts::getRating).average().orElse(0);
			double averageRating = avgRating;

			// add rating from posts - recent 14 days
			CrossCollectionResponseDTO ratingDTO = new CrossCollectionResponseDTO(averageRating);
			trendingRestaurant.add(ratingDTO);

			RestaurantsResponseDTO dto = restaurantsResponseDTOConverter.convertToResponseDTO(restaurants.get());
			trendingRestaurant.add(dto);
			List<Dishes> dishes = dishRepository.findByRestaurantId(id);
			if (!dishes.isEmpty()) {
				// select 2 top dishes from the same restaurant with highest rating
				List<Dishes> topDishes = dishes.stream()
						.sorted(Comparator.comparingDouble(Dishes::getRating).reversed()) // Sort by rating in
																							// descending order
						.limit(2) // Limit to top 2
						.collect(Collectors.toList()); // Collect the result as a list

				for (Dishes dish : topDishes) {
					DishesResponseDTO dtoDishes = dishesResponseDTOConverter.convertToResponseDTO(dish);
					trendingRestaurant.add(dtoDishes);

				}
				return new ResponseResult(200, "success", trendingRestaurant);
			}
			return new ResponseResult(200, "success but there is no dish available for the restaurant",
					trendingRestaurant);
		}
		return new ResponseResult(400, "fail");
	}

	/**
	 * Retrieves a trending restaurant filtered by tags and location
	 * 
	 * @return ResponseResult object containing status, message and the restaurant
	 *         and two additional dishes.
	 */
	@Override
	public ResponseResult getTrendingRestaurantByTagsAndLocation(List<String> tags, String location) {

		if (location.isEmpty()) {

			return new ResponseResult(404, "Location not provided");
		}
		List<Restaurant> restaurants;
		if (!location.isEmpty()) {
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
				return new ResponseResult(400, "Location not found");
			}

			// Retrieve restaurants by location
			List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

			if (restaurantsByLocation.isEmpty()) {
				return new ResponseResult(404, "No restaurants found for the given location");
			}

			// Filter restaurants by name containing the keyword (case-insensitive)
			String finalKeyword = "";
			restaurants = restaurantsByLocation.stream()
					.filter(restaurant -> restaurant.getName().toLowerCase().contains(finalKeyword.toLowerCase()))
					.collect(Collectors.toList());
		} else {
			restaurants = restaurantRepository.findByNameLikeIgnoreCase("");
		}
		if (restaurants.isEmpty()) {
			return new ResponseResult(404, "No restaurants found matching your search criteria");
		}

		if (tags == null || tags.isEmpty()) {
			// Handle empty tags (e.g., return all results, or an empty list)
			return new ResponseResult(400, "No tag is provided");
		}

		// transfer tag names from string to
		List<ObjectId> tagIds = new ArrayList<>();
		for (String tagName : tags) {

			Optional<Tags> tag = tagsRepository.findByTagName(tagName);

			tag.ifPresent(t -> tagIds.add(t.getId()));

		}
		if (tagIds == null || tagIds.isEmpty()) {
			// Handle if no tag is found from Tags collection based on tag name string
			return new ResponseResult(400, "Tags provided are not valid");
		}

		// Get the list of restaurant IDs
		List<ObjectId> restaurantIds = restaurants.stream().map(Restaurant::getId).collect(Collectors.toList());

		// Define the date 14 days ago from now
		Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

		// Aggregate posts
		MatchOperation matchOperation = Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo)
				.and("restaurant_id").in(restaurantIds).and("tags").all(tagIds));

		GroupOperation groupOperation = Aggregation.group("restaurant_id").first("restaurant_id").as("entityId").count()
				.as("postCount").avg("rating").as("avgRating");

		MatchOperation filterAvgRating = Aggregation.match(Criteria.where("avgRating").gt(4.5));

		SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "postCount");

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, filterAvgRating,
				sortOperation);

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		List<PostStats> mappedResults = results.getMappedResults();

		if (mappedResults.isEmpty()) {
			return new ResponseResult(404, "No trending restaurants found");
		}

		ObjectId restaurantId = mappedResults.get(0).getEntityId();
		double avgRating = mappedResults.get(0).getAvgRating();
		if (restaurantId != null) {
			return getTrendingById(restaurantId, avgRating);
		}
		return new ResponseResult(404, "No trending restaurants found");
	}

	/**
	 * Get names of all restaurants.
	 *
	 * @return List of restaurant names with ID.
	 */

	@Override
	public ResponseResult getRestaurantNames() {
		List<Restaurant> restaurantNames = restaurantRepository.restaurantIdAndNames();
		List<Map<Object,String>> resultList = restaurantNames.stream()
				.map(r -> {
					Map<Object,String> map = new LinkedHashMap<>();
					map.put("id", r.getId().toString());
					map.put("name", r.getName());
					return map;
				})
				.collect(Collectors.toList());


		if (!restaurantNames.isEmpty()) {
			return new ResponseResult(200, "success", resultList);
		}
		return new ResponseResult(400, "fail");
	}

	/**
     * Upload Logo for restaurant by its ID.
     *
     * @param id The ID of the restaurant.
     * @param file is the Object-classed filenames.
     * @return ResponseResult object containing status, message and image filenames.
     */
    @Override
    public ResponseResult uploadLogoById(ObjectId id, MultipartFile file) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            ResponseResult uploadResult = azureBlobService.uploadFile(file);
            if (uploadResult.getCode() != 200) {return uploadResult;}
            String filename = uploadResult.getData().toString();
            deleteImages(id,"logo");    // Delete previous logo since only 1 logo for each restaurant
            restaurant.get().setLogo(filename);
            restaurantRepository.save(restaurant.get());
            return new ResponseResult(200, "success add logo to restaurant id: " + id, filename);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Upload (more) images for restaurant by its ID.
     *
     * @param id The ID of the restaurant.
     * @param files is the Object-classed filenames.
     * @return ResponseResult object containing status, message and image filenames.
     */
    @Override
    public ResponseResult uploadImagesById(ObjectId id, MultipartFile[] files) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            ResponseResult uploadResult = azureBlobService.uploadMultipleFiles(files);
            if (uploadResult.getCode() != 200) {return uploadResult;}
            if (uploadResult.getData() instanceof ArrayList<?>) {
                // Extract filenames from Object to ArrayList<String>
                ArrayList<String> newFilenames = ((ArrayList<?>) uploadResult.getData()).stream()
                        .filter(String.class::isInstance)         // Filter non-String elements
                        .map(String.class::cast)                  // Transfer Object to String
                        .collect(Collectors.toCollection(ArrayList::new)); // Collect into ArrayList<String>
                // Check if image filename arraylist is null，initialize it if so
                if (restaurant.get().getImages() == null) {restaurant.get().setImages(new ArrayList<>());}
                // Add Restaurant Images
                restaurant.get().getImages().addAll(newFilenames);
                // Store modified restaurant object back into database
                restaurantRepository.save(restaurant.get());
                return new ResponseResult(200, "success add images to restaurant id: " + id, newFilenames);
            }
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Delete all images/logo for a restaurant by its ID.
     *
     * @param id The ID of the restaurant.
     * @param type The deletion content. "images" or "logo"
     * @return ResponseResult object containing status, message and image filenames.
     */
    @Override
    public ResponseResult deleteImages(ObjectId id, String type) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            switch (type) {
                case "images":{
                    ArrayList<String> filenames = restaurant.get().getImages();
                    // If there are images bind --> Delete from database and from Azure
                    if (filenames != null){
                        // Step 1: Delete from database
                        restaurant.get().setImages(null);
                        restaurantRepository.save(restaurant.get());
                        // Step 2: Delete from Azure
                        for (String filename : filenames) {azureBlobService.deleteFileFromAzure(filename);}
                    }
                    // File deleted --> return 200 directly (idempotent)
                    return new ResponseResult(200, "success delete images for restaurant id: " + id, filenames);
                }
                case "logo":{
                    String filename = restaurant.get().getLogo();
                    if (filename != null){
                        restaurant.get().setLogo(null);
                        restaurantRepository.save(restaurant.get());
                        azureBlobService.deleteFileFromAzure(filename);
                    }
                    return new ResponseResult(200, "success delete logo image for restaurant id: " + id, filename);
                }
            }
        }
        return new ResponseResult(400, "fail finding restaurant.");
    }

    /**
     * Retrieves top restaurant (default top 5).
     * @return ResponseResult object containing status, message and the top restaurants.
     */
    @Override
    public ResponseResult getTopRestaurants(int n) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Restaurant> topRestaurants = restaurants.stream()
                //.sorted(Comparator.comparing(Restaurant::getRating).reversed())
                .sorted(new Comparator<Restaurant>() {
                    @Override
                    public int compare(Restaurant r1, Restaurant r2) {
                        Double rating1 = (r1.getRating() != null) ? r1.getRating() : -1.0;
                        Double rating2 = (r2.getRating() != null) ? r2.getRating() : -1.0;
                        return rating2.compareTo(rating1);  // 降序排列，rating 高的排在前面
                    }
                })
                .limit(n)
                .collect(Collectors.toList());
        List<RestaurantsResponseDTO> topRestaurantList = new ArrayList<>();

        if (!topRestaurants.isEmpty()) {
            for (Restaurant restaurant : topRestaurants) {
                RestaurantsResponseDTO dto = restaurantsResponseDTOConverter.convertToResponseDTO(restaurant);
                topRestaurantList.add(dto);
            }
            return new ResponseResult(200, "success", topRestaurantList);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Retrieves top restaurants (deafult top 5) with specific tag.
     * @return ResponseResult object containing status, message and the top restaurants.
     */
    @Override
    public ResponseResult getTopRestaurantsByTag(String tagName, int n) {
        // Step 1: Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {
            return new ResponseResult(400, "Tag not found");
        }

        // Step 2: Get the tag ID
        ObjectId tagId = tagOptional.get().getId();

        // Step 3: Use the tag ID to retrieve restaurants
        List<Restaurant> sortedRestaurants = restaurantRepository.sortedRestaurantsByRatingBasedOnTagId(tagId);
        if (sortedRestaurants.isEmpty()) {
            return new ResponseResult(400, "No restaurants found for the given tag");
        }

        // Step 4: Get the top N restaurants
        List<Restaurant> topRestaurants = sortedRestaurants.subList(0, Math.min(n, sortedRestaurants.size()));

        // Step 5: Convert to response DTOs
        List<RestaurantsResponseDTO> topRestaurantsList = topRestaurants.stream()
                .map(restaurant -> restaurantsResponseDTOConverter.convertToResponseDTO(restaurant))
                .collect(Collectors.toList());

        return new ResponseResult(200, "success", topRestaurantsList);
    }

    @Override
    public ResponseResult searchRestaurants(String keyword, String location) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}
		// System.out.println(keyword);
        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(keyword, location);
		// System.out.println("Here's the restaurants: " + restaurants);
        List<RestaurantsResponseDTO> dtos = restaurants.stream()
                .map(restaurantsResponseDTOConverter::convertToResponseDTO)
                .collect(Collectors.toList());
        // Check if the list is empty
        if (dtos.isEmpty()) {
            return new ResponseResult(404, "No restaurant found");
        } else {
            return new ResponseResult(200, "success", dtos);
        }
    }

    @Override
	public ResponseResult searchTrendingRestaurants(String keyword, String location, String tagName) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}
        List<Restaurant> restaurants;

        ObjectId tagId = null;
        if (tagName != null && !tagName.isEmpty()) {
            // Tag is provided
            // Find the tag by name
            Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
            if (tagOptional.isPresent()) {
                tagId = tagOptional.get().getId();
            } else {
                return new ResponseResult(400, "Tag not found");
            }
        }
        if (!location.isEmpty()) {
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
                return new ResponseResult(400, "Location not found");
            }

            // Retrieve restaurants by location
            List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

            if (restaurantsByLocation.isEmpty()) {
                return new ResponseResult(404, "No restaurants found for the given location");
            }

            // Filter restaurants by name containing the keyword (case-insensitive)
            String finalKeyword = keyword;
            restaurants = restaurantsByLocation.stream()
                    .filter(restaurant -> restaurant.getName().toLowerCase().contains(finalKeyword.toLowerCase()))
                    .collect(Collectors.toList());
        }else {
            restaurants = restaurantRepository.findByNameLikeIgnoreCase(keyword);
        }
        if (restaurants.isEmpty()) {
            return new ResponseResult(404, "No restaurants found matching your search criteria");
        }

        // If tag is provided, filter restaurants by tag
        if (tagId != null) {
            ObjectId finalTagId = tagId;
            restaurants = restaurants.stream()
                    .filter(restaurant -> restaurant.getTags() != null && restaurant.getTags().contains(finalTagId))
                    .collect(Collectors.toList());

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No restaurants found matching your tag");
            }
        }

        // Get the list of restaurant IDs
        List<ObjectId> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        // Define the date 14 days ago from now
        Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

        // Aggregate posts
        MatchOperation matchOperation = Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo)
                .and("restaurant_id").in(restaurantIds));

        GroupOperation groupOperation = Aggregation.group("restaurant_id")
                .first("restaurant_id").as("entityId")
                .count().as("postCount")
                .avg("rating").as("avgRating");

        MatchOperation filterAvgRating = Aggregation.match(Criteria.where("avgRating").gt(4.5));

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "postCount");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                groupOperation,
                filterAvgRating,
                sortOperation
        );

        // Execute aggregation
        AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		List<PostStats> mappedResults = results.getMappedResults();
        if (mappedResults.isEmpty()) {
            return new ResponseResult(404, "No trending restaurants found");
        }

        ObjectId restaurantId = mappedResults.get(0).getEntityId();
		double avgRating = mappedResults.get(0).getAvgRating();
        if (restaurantId != null) {
			return getTrendingById(restaurantId, avgRating);
        }
        return new ResponseResult(404, "No trending restaurants found");
    }

    @Override
    public ResponseResult searchTopRestaurants(String keyword, String location, String tagName, int topNum) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}

        List<Restaurant> restaurants;

        ObjectId tagId = null;
        if (tagName != null && !tagName.isEmpty()) {
            // Tag is provided
            // Find the tag by name
            Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
            if (tagOptional.isPresent()) {
                tagId = tagOptional.get().getId();
            } else {
                return new ResponseResult(400, "Tag not found");
            }
        }

        if (!location.isEmpty()) {
            // Location is provided
            ObjectId locationId;

            // Check if the location is an area or a region
            Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
            Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);

            if (areaOptional.isPresent()) {
                locationId = areaOptional.get().getId();
            } else if (regionOptional.isPresent()) {
                locationId = regionOptional.get().getId();
            } else {
                return new ResponseResult(400, "Location not found");
            }

            // Retrieve restaurants by location
            restaurants = restaurantRepository.filteredRestaurantsByLocation(locationId);

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No restaurants found for the given location");
            }
        } else {
            // Location is not provided; retrieve all restaurants
            restaurants = restaurantRepository.findAll();

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No restaurants found");
            }
        }

        /* Filter restaurants by keyword */
        String finalKeyword = keyword.toLowerCase();
		// find based on restaurant name/tag/location.
		List<Restaurant> filteredRestaurants = restaurants.stream()
				.filter(restaurant ->{
					// 1. Match restaurant name
					boolean nameMatch = restaurant.getName().toLowerCase().contains(finalKeyword);
					// 2. Match restaurant tag
//					boolean tagMatch = 	restaurant.getTags().stream()
//							.map(restaurantTagId -> tagsRepository.findById(restaurantTagId))
//							.filter(Optional::isPresent)
//							.map(Optional::get)
//							.map(Tags::getTagName)
//							.anyMatch(restaurantTagName -> restaurantTagName.toLowerCase().contains(finalKeyword));
					// 3. Match restaurant location
					Optional<String> regionName = Optional.empty();
					Optional<String> areaName = Optional.empty();
					if (restaurant.getLocation().get("region") != null) {
						regionName = regionsRepository.findById(restaurant.getLocation().get("region"))
								.map(Regions::getRegionName);
					}
					if (restaurant.getLocation().get("area") != null) {
						areaName = areasRepository.findById(restaurant.getLocation().get("area"))
								.map(Areas::getAreaName);
					}
					boolean locationMatch = regionName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false) ||
							areaName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false);
					return nameMatch || locationMatch;
				}).collect(Collectors.toList());

        if (filteredRestaurants.isEmpty()) {
            return new ResponseResult(404, "No restaurants found matching your search criteria");
        }

        // If tag is provided, filter restaurants by tag
        if (tagId != null) {
            ObjectId finalTagId = tagId;
            filteredRestaurants = filteredRestaurants.stream()
                    .filter(restaurant -> restaurant.getTags() != null && restaurant.getTags().contains(finalTagId))
                    .collect(Collectors.toList());

            if (filteredRestaurants.isEmpty()) {
                return new ResponseResult(404, "No restaurants found matching your tag");
            }
        }

        // Sort the restaurants by rating in descending order
        List<Restaurant> sortedRestaurants = filteredRestaurants.stream()
                .sorted(Comparator.comparing(
                        restaurant -> restaurant.getRating() == null ? -1 : restaurant.getRating(),
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toList());

        // Get the top N restaurants based on the specified number
        List<Restaurant> topRestaurants = sortedRestaurants.subList(0, Math.min(topNum, sortedRestaurants.size()));

        // Convert top restaurants to response DTOs
        List<RestaurantsResponseDTO> topRestaurantsList = topRestaurants.stream()
                .map(restaurant -> restaurantsResponseDTOConverter.convertToResponseDTO(restaurant))
                .collect(Collectors.toList());

        // Return the successful response with the top restaurants
        return new ResponseResult(200, "success", topRestaurantsList);
    }

    @Override
    public ResponseResult getTopRestaurantsBasedOnLocation(String location, int topNum) {
        // Filter restaurants by location
//        List<Restaurant> restaurantsByLocation;
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

        if (restaurantsByLocation.isEmpty()) {
            return new ResponseResult(404, "No restaurants found for the given location");
        }

        // Sort the restaurants by rating in descending order
        List<Restaurant> sortedRestaurants = restaurantsByLocation.stream()
                .sorted(Comparator.comparing(
                        restaurant -> restaurant.getRating() == null ? -1 : restaurant.getRating(),  // treat null as -1
                        Comparator.reverseOrder()  // sort in descending order
                ))
                .collect(Collectors.toList());

        // Get the top N restaurants based on the specified number
        List<Restaurant> topRestaurants = sortedRestaurants.subList(0, Math.min(topNum, sortedRestaurants.size()));

        // Convert top restaurants to response DTOs
        List<RestaurantsResponseDTO> topRestaurantsList = topRestaurants.stream()
                .map(restaurant -> restaurantsResponseDTOConverter.convertToResponseDTO(restaurant))  // convert each restaurant to its response DTO
                .collect(Collectors.toList());

        // Return the successful response with the top restaurants
        return new ResponseResult(200, "success", topRestaurantsList);
    }

    @Override
    public ResponseResult getTopRestaurantsBasedOnTagAndLocation(String tagName, String location, int topNum) {
        // Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {return new ResponseResult(404, "Tag not found");}

        // Get the tag ID
        ObjectId tagId = tagOptional.get().getId();

        // Filter restaurants by location
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

        if (restaurantsByLocation.isEmpty()) {
            return new ResponseResult(404, "No restaurants found for the given location");
        }

        // Filter restaurants by the given tag and sort by rating in descending order
        List<Restaurant> sortedRestaurants = restaurantsByLocation.stream()
                .filter(restaurant -> restaurant.getTags().contains(tagId))  // filter by tag ID
                .sorted(Comparator.comparing(
                        restaurant -> restaurant.getRating() == null ? -1 : restaurant.getRating(),  // treat null as -1
                        Comparator.reverseOrder()  // sort in descending order
                ))
                .collect(Collectors.toList());

        if (sortedRestaurants.isEmpty()) {
            return new ResponseResult(404, "No restaurants found for the given tag and location");
        }

        // Get the top N restaurants
        List<Restaurant> topRestaurants = sortedRestaurants.subList(0, Math.min(topNum, sortedRestaurants.size()));

        // Convert top dishes to response DTOs
        List<RestaurantsResponseDTO> topRestaurantsList = topRestaurants.stream()
                .map(restaurant -> restaurantsResponseDTOConverter.convertToResponseDTO(restaurant))
                .collect(Collectors.toList());

        // Return the successful response
        return new ResponseResult(200, "success", topRestaurantsList);
    }

}
