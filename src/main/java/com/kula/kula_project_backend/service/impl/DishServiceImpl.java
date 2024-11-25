package com.kula.kula_project_backend.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

//import static com.kula.kula_project_backend.common.converter.DishesResponseDTOConverter.convertToResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kula.kula_project_backend.entity.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.kula.kula_project_backend.common.PostStats;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.DishesResponseDTOConverter;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.DishesRepository;
import com.kula.kula_project_backend.dao.PostsRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.RestaurantRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.requestdto.DishesDTO;
import com.kula.kula_project_backend.dto.responsedto.CrossCollectionResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.DishesResponseDTO;
import com.kula.kula_project_backend.service.IAzureBlobService;
import com.kula.kula_project_backend.service.IDishService;


/**
 * Service implementation for Dish operations.
 * This service is used to save, delete and get Dishes.
 * The service uses the DishesRepository to interact with the database.
 * The service uses the PostsRepository to interact with the database.
 * The service uses the DishesResponseDTO to convert Dishes to DishesResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the DishesDTO to save the Dishes.
 * The service uses the ObjectId to get the ID of the Dish.
 */
@Service
public class DishServiceImpl implements IDishService {

    @Autowired
    private DishesRepository dishRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

	@Autowired
	private RestaurantRepository restaurantRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private RegionsRepository regionsRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private IAzureBlobService azureBlobService;

    @Autowired
    private DishesResponseDTOConverter dishesResponseDTOConverter;

	private static final Logger logger = LoggerFactory.getLogger(DishServiceImpl.class);

    // BlobServiceClient is the main interface for interacting with the Azure Blob Storage service.
    @Autowired
    private BlobServiceClient blobServiceClient;
    // The container name is used to create a container client
    @Value("${azure.blob.container-name}")
    private String containerName;
    // The endpoint is used to create a blob client
    @Value("${azure.blob.container-endpoint}")
    private String endPoint;

    /**
     * Retrieves all dishes.
     * @return ResponseResult object containing status, message and all dishes.
     */
    @Override
    public ResponseResult getAll() {
        List<Dishes> dishes = dishRepository.findAll();
        List<DishesResponseDTO> dishList = new ArrayList<>();

        if (!dishes.isEmpty()) {

            for (Dishes dish : dishes) {
                Optional<Restaurant> restaurant = restaurantRepository.findById(dish.getRestaurantId());
                DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
                dishList.add(dto);
            }
            return new ResponseResult(200, "Success", dishList);
        } else {
            return new ResponseResult(404, "Dish not found");
        }
    }
    /**
     * Saves a new dish.
     * @param dishDTO The DTO containing the dish details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(DishesDTO dishDTO) {
        Dishes dish = convertToEntity(dishDTO);
        dishRepository.save(dish);
        if (dish.getId() != null) {
            return new ResponseResult(200, "Success", dish.getId().toHexString());
        } else {
            return new ResponseResult(400, "Fail");
        }
    }
    /**
     * Retrieves a dish by its ID.
     * @param id The ID of the dish.
     * @return ResponseResult object containing status, message and the dish.
     */
    @Override
    public ResponseResult getById(ObjectId id) {
        Optional<Dishes> optionalDish = dishRepository.findById(id);
        if(optionalDish.isPresent()) {
            Optional<Restaurant> restaurant = restaurantRepository.findById(optionalDish.get().getRestaurantId());
            DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(optionalDish.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Updates a dish.
     * @param dishDTO The DTO containing the updated dish details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult update(DishesDTO dishDTO) {
        Optional<Dishes> optionalDish = dishRepository.findById(dishDTO.getId());
        if (optionalDish.isPresent()) {
            Dishes dish = convertToEntity(dishDTO);
            dishRepository.save(dish);

            return new ResponseResult(200, "Success");
        } else {
            return new ResponseResult(404, "Dish not found");
        }
    }
    /**
     * Deletes a dish by its ID.
     * @param id The ID of the dish.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteDish(ObjectId id) {
        Optional<Dishes> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {
            dishRepository.deleteById(id);
            return new ResponseResult(200, "Success");
        } else {
            return new ResponseResult(404, "Dish not found");
        }
    }
    /**
     * Searches for dishes in a restaurant.
     * @param restaurantId The ID of the restaurant.
     * @param dishName The name of the dish.
     * @return ResponseResult object containing status, message and the dishes.
     */
    @Override
    public ResponseResult searchDishesInRestaurant(ObjectId restaurantId, String dishName) {
        List<Dishes> dishes = dishRepository.findByRestaurantIdAndDishNameLikeIgnoreCase(restaurantId, dishName);
//        List<DishesResponseDTO> dtos = dishes.stream()
//                .map(DishesResponseDTOConverter::convertToResponseDTO(optionalDish.get(), restaurant.get()))
//                .collect(Collectors.toList());
        List<DishesResponseDTO> dishList = new ArrayList<>();

        if (!dishes.isEmpty()) {
            for (Dishes dish : dishes) {
                Optional<Restaurant> restaurant = restaurantRepository.findById(dish.getRestaurantId());
                DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
                dishList.add(dto);
            }
//            Optional<Restaurant> restaurant = restaurantRepository.findById(dishes.get().getRestaurantId());
//            DishesResponseDTO dto = DishesResponseDTOConverter.convertToResponseDTO(dishes.get(), restaurant.get());
            return new ResponseResult(200, "Success", dishList);
        } else {
            return new ResponseResult(404, "Dish not found");
        }

    }
    /**
     * Retrieves the average rating of a dish.
     * @param dishId The ID of the dish.
     * @return ResponseResult object containing status, message and the average rating.
     */
    @Override
    public ResponseResult getAverageRating(ObjectId dishId) {
        Optional<Dishes> optionalDish = dishRepository.findById(dishId);
        if (optionalDish.isPresent()) {
            Dishes dish = optionalDish.get();
            Query query = Query.query(Criteria.where("dishId").is(dishId));
            List<Posts> relatedPosts = mongoTemplate.find(query, Posts.class);
            double averageRating = relatedPosts.stream()
                    .mapToDouble(Posts::getRating)
                    .average()
                    .orElse(0);
            return new ResponseResult(200, "Success", averageRating);
        } else {
            return new ResponseResult(404, "Dish not found");
        }
    }

	/**
	 * Retrieves a dish by its restaurant ID.
	 * 
	 * @param id The ID of the dish.
	 * @return ResponseResult object containing status, message and the dish.
	 */
	@Override
	public ResponseResult getByRestaurantId(ObjectId id) {
		List<Dishes> dishes = dishRepository.findByRestaurantId(id);
		List<DishesResponseDTO> dishList = new ArrayList<>();
		if (!dishes.isEmpty()) {
			for (Dishes dish : dishes) {
                Optional<Restaurant> restaurant = restaurantRepository.findById(dish.getRestaurantId());
				DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
				dishList.add(dto);
			}
			return new ResponseResult(200, "success", dishList);
		}
		return new ResponseResult(400, "Dish not found");
	}

	/**
	 * Retrieves a trending dish .
	 *
	 * @return ResponseResult object containing status, message and the dish.
	 */

	@Override
	public ResponseResult getTrendingDish() {
		// Define the date 14 days ago from now
		LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);

		// Aggregation pipeline
		Aggregation aggregation = Aggregation.newAggregation(
				// Match posts updated within the last year
				Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo.atZone(ZoneOffset.UTC).toInstant())
						.and("dish_id").exists(true)
				),

				// Group by dish_id and count the number of posts, also calculate the average
				// rating
				Aggregation.group("dish_id").first("dish_id").as("entityId").count().as("postCount").avg("rating")
						.as("avgRating"),

				// Filter for average rating > 4.5
				Aggregation.match(Criteria.where("avgRating").gt(4.5)),

				// Sort by post count in descending order
				Aggregation.sort(Sort.Direction.DESC, "postCount"),

				// Limit to 1 result (dish with the most posts)
				Aggregation.limit(1));

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		// logger.info("getTrendingDish -- results: {}", results);
		List<PostStats> stats = results.getMappedResults();
		// logger.info("getTrendingDish -- stats: {}", stats);
		// Return the dish_id with the most posts if available
		if (!stats.isEmpty()) {

			ObjectId trendingDishId = stats.get(0).getEntityId();
			// results.getUniqueMappedResult().getEntityId();
			// logger.info("getTrendingDish -- id: {}", trendingDishId);
			if (trendingDishId != null) {

				return getTrendingDishById(trendingDishId);
			}
		}

		return new ResponseResult(400, "No dish satisfies requirements for trending dish");

	}


	/**
	 * Retrieves a trending dish by tags.
	 *
	 * @return ResponseResult object containing status, message and the dish.
	 */

	@Override
	public ResponseResult getTrendingDishByTags(List<String> tags) {
		if (tags == null || tags.isEmpty()) {
			// Handle empty tags (e.g., return all results, or an empty list)
			return new ResponseResult(400, "No tag is provided");
		}

		// transfer tag names from string to ObjectId
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
						.and("dish_id").exists(true)
						.and("tags").all(tagIds)),

				// Group by dish_id and count the number of posts, also calculate the average
				// rating
				Aggregation.group("dish_id").count().as("postCount").avg("rating").as("avgRating").first("dish_id")
						.as("entityId"),

				// Filter for average rating > 4.5
				Aggregation.match(Criteria.where("avgRating").gt(4.5)),

				// Sort by post count in descending order
				Aggregation.sort(Sort.Direction.DESC, "postCount"),

				// Limit to 1 result (dish with the most posts)
				Aggregation.limit(1));

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		List<PostStats> stats = results.getMappedResults();

		// Return the dish_id with the most posts if available
		if (!stats.isEmpty()) {
			ObjectId trendingDishId = stats.get(0).getEntityId();
			double avgRating = stats.get(0).getAvgRating();
			// results.getUniqueMappedResult().getEntityId();
			// logger.info("getTrendingDishbyTag -- id: {}", trendingDishId);
			if (trendingDishId != null) {
				return getTrendingDishById(trendingDishId, avgRating);
			}
		}

		return new ResponseResult(400, "No dish satisfies requirements for trending dish");

	}

    /**
     * Retrieves a trending dish by its ID.
     *
     * @param id The ID of the dish.
     * @return ResponseResult object containing status, message and the dish.
     */

    @Override
    public ResponseResult getTrendingDishById(ObjectId id) {

        List<Object> dishList = new ArrayList<>();
        Optional<Dishes> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {

            ObjectId restaurantId = optionalDish.get().getRestaurantId();

            List<Dishes> dishes = dishRepository.findByRestaurantId(restaurantId);

            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
			// rating from posts - recent 14 days
			Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

			// Create the query to find related posts based on dishId and updated_at
			Query query = Query.query(Criteria.where("dishId").is(id).and("updated_at").gte(fourteenDaysAgo));

			// Execute the query and retrieve related posts
			List<Posts> relatedPosts = mongoTemplate.find(query, Posts.class);

			double averageRating = relatedPosts.stream().mapToDouble(Posts::getRating).average().orElse(0);

            if (optionalRestaurant.isPresent()) {

				// add average rating from posts - recent 14 days
				CrossCollectionResponseDTO ratingDTO = new CrossCollectionResponseDTO(averageRating);
				dishList.add(ratingDTO);

				// add searched dish
				DishesResponseDTO dishDTO = dishesResponseDTOConverter.convertToResponseDTO(optionalDish.get());
				dishList.add(dishDTO);
				// add other dishes in same restaurant
                if (!dishes.isEmpty()) {
					// select 3 top dishes from the same restaurant with highest rating
					List<Dishes> topDishes = dishes.stream()
							.sorted(Comparator.comparingDouble(Dishes::getRating).reversed()) // Sort by rating in
																								// descending order
							.limit(3) // Limit to top 3
							.collect(Collectors.toList()); // Collect the result as a list
					int sizeThreshold = dishList.size() + 2;
					for (Dishes dish : topDishes) {
						if (!dish.getId().toString().equals(id.toString())) {// not the dish that is being retrieved by
																				// id
                        DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
						dishList.add(dto);

						if (dishList.size() > sizeThreshold) {// to make sure only return 2 top dishes besides the dish
																// that is
													// retrieved by id
							break;
						}
					}
                    }
                    return new ResponseResult(200, "success", dishList);
                }
            }
        }
        return new ResponseResult(400, "Dish not found");
    }

	@Override
	public ResponseResult getTrendingDishById(ObjectId id, double avgRating) {
		List<Object> dishList = new ArrayList<>();
		Optional<Dishes> optionalDish = dishRepository.findById(id);
		if (optionalDish.isPresent()) {

			ObjectId restaurantId = optionalDish.get().getRestaurantId();

			List<Dishes> dishes = dishRepository.findByRestaurantId(restaurantId);

			Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

			double averageRating = avgRating;

			if (optionalRestaurant.isPresent()) {

				// add average rating from posts - recent 14 days
				CrossCollectionResponseDTO ratingDTO = new CrossCollectionResponseDTO(averageRating);
				dishList.add(ratingDTO);

				// add searched dish
				DishesResponseDTO dishDTO = dishesResponseDTOConverter.convertToResponseDTO(optionalDish.get());
				dishList.add(dishDTO);
				// add other dishes in same restaurant
				if (!dishes.isEmpty()) {
					// select 3 top dishes from the same restaurant with highest rating
					List<Dishes> topDishes = dishes.stream()
							.sorted(Comparator.comparingDouble(Dishes::getRating).reversed()) // Sort by rating in
																								// descending order
							.limit(3) // Limit to top 3
							.collect(Collectors.toList()); // Collect the result as a list
					int sizeThreshold = dishList.size() + 2;
					for (Dishes dish : topDishes) {
						if (!dish.getId().toString().equals(id.toString())) {// not the dish that is being retrieved by
																				// id
							DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
							dishList.add(dto);

							if (dishList.size() > sizeThreshold) {// to make sure only return 2 top dishes besides the
																	// dish
																	// that is
								// retrieved by id
								break;
							}
						}
					}
					return new ResponseResult(200, "success", dishList);
				}
			}
		}
		return new ResponseResult(400, "Dish not found");
	}

	/**
	 * Retrieves a trending dish filtered by location and tags.
	 *
	 * @param tags and location.
	 * @return ResponseResult object containing status, message and the dish.
	 */
	@Override
	public ResponseResult getTrendingDishByTagsAndLocation(List<String> tags, String location) {

		if (location.isEmpty()) {
			return new ResponseResult(404, "Location not provided");
		}

		List<Dishes> dishes;
		if (!location.isEmpty()) {
			// Location is provided
			// Retrieve restaurants in that location first
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
			List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

			if (restaurantsByLocation.isEmpty()) {
				return new ResponseResult(404, "No dishes found for your location");
			}

			// Get the list of restaurant IDs
			List<ObjectId> restaurantIds = restaurantsByLocation.stream().map(Restaurant::getId)
					.collect(Collectors.toList());

			// Retrieve matching dishes
			dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdIn("", restaurantIds);
		} else {
			// Location is not provided; retrieve all dishes matching the keyword
			dishes = dishRepository.findByDishNameContainingIgnoreCase("");
		}
		if (dishes.isEmpty()) {
			return new ResponseResult(404, "No dishes found for your search text");
		}
		List<ObjectId> dishIds = dishes.stream().map(Dishes::getId).collect(Collectors.toList());

		if (tags == null || tags.isEmpty()) {
			// Handle empty tags (e.g., return all results, or an empty list)
			return new ResponseResult(400, "No tag is provided");
		}

		// transfer tag names from string to ObjectId
		List<ObjectId> tagIds = new ArrayList<>();
		for (String tagName : tags) {
			Optional<Tags> tag = tagsRepository.findByTagName(tagName);
			tag.ifPresent(t -> tagIds.add(t.getId()));

		}
		if (tagIds == null || tagIds.isEmpty()) {
			// Handle if no tag is found from Tags collection based on tag name string
			return new ResponseResult(400, "Tags provided are not valid");
		}

		// Define the date 14 days ago from now
		Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

		// Aggregate posts
		MatchOperation matchOperation = Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo)
				.and("dish_id").exists(true).and("tags").all(tagIds));

		GroupOperation groupOperation = Aggregation.group("dish_id").first("dish_id").as("entityId").count()
				.as("postCount").avg("rating").as("avgRating");

		MatchOperation filterAvgRating = Aggregation.match(Criteria.where("avgRating").gt(4.5));

		SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "postCount"));

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, filterAvgRating,
				sortOperation);

		// Execute aggregation
		AggregationResults<PostStats> results = mongoTemplate.aggregate(aggregation, "posts", PostStats.class);
		List<PostStats> mappedResults = results.getMappedResults();
		if (mappedResults.isEmpty()) {
			return new ResponseResult(404, "No dishes found");
		} else {
			ObjectId trendingDishId = mappedResults.get(0).getEntityId();
			double avgRating = mappedResults.get(0).getAvgRating();
			if (trendingDishId != null) {
				return getTrendingDishById(trendingDishId, avgRating);
			}
		}

		return new ResponseResult(404, "No dish satisfies requirements for trending dish");
	}

    /**
     * Upload (more) images for dish by its ID.
     *
     * @param id The ID of the dish.
     * @param files is the Object-classed filenames.
     * @return ResponseResult object containing status, message and the image filenames.
     */
    @Override
    public ResponseResult uploadImagesById(ObjectId id, MultipartFile[] files) {
        Optional<Dishes> dish = dishRepository.findById(id);
        if (dish.isPresent()) {
            ResponseResult uploadResult = azureBlobService.uploadMultipleFiles(files);
            if (uploadResult.getCode() != 200) {return uploadResult;}
            if (uploadResult.getData() instanceof ArrayList<?>) {
                // Extract filenames from Object to ArrayList<String>
                ArrayList<String> newFilenames = ((ArrayList<?>) uploadResult.getData()).stream()
                        .filter(String.class::isInstance)         // Filter non-String elements
                        .map(String.class::cast)                  // Transfer Object to String
                        .collect(Collectors.toCollection(ArrayList::new)); // Collect into ArrayList<String>
                // Check if image filename arraylist is null，initialize it if so
                if (dish.get().getImages() == null) {dish.get().setImages(new ArrayList<>());}
                // Add dish Images
                dish.get().getImages().addAll(newFilenames);
                // Store modified dish object back into database
                dishRepository.save(dish.get());
                return new ResponseResult(200, "success add images to dish id: " + id, newFilenames);
            }
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Delete all images for a dish by its ID.
     *
     * @param id The ID of the dish.
     * @return ResponseResult object containing status, message and image filenames.
     */
    @Override
    public ResponseResult deleteAllImages(ObjectId id) {
        Optional<Dishes> dish = dishRepository.findById(id);
        if (dish.isPresent()) {
            ArrayList<String> filenames = dish.get().getImages();
            // If there are images bind --> Delete from database and from Azure
            if (filenames != null){
                // Step 1: Delete from database
                dish.get().setImages(null);
                dishRepository.save(dish.get());
                // Step 2: Delete from Azure
                for (String filename : filenames) {
                    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                    BlobClient blobClient = containerClient.getBlobClient(filename);
                    // check if the blob exists
                    if (blobClient.exists()) {
                        blobClient.delete();

                    }
                }
            }
            // If there is no image bind --> return 200 directly (idempotent)
            return new ResponseResult(200, "success delete images for dish id: " + id, filenames);
        }
        return new ResponseResult(400, "fail finding dish.");
    }

    /**
     * Retrieves dishes based on tag.
     * @return ResponseResult object containing status, message and the top dishes.
     */
    @Override
    public ResponseResult filterDishesByTag(String tagName) {
		// Step 1: Find the tag by name
		Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
		if (!tagOptional.isPresent()) {return new ResponseResult(400, "Tag not found");}
		// Step 2: Get the tag ID
		ObjectId tagId = tagOptional.get().getId();
        List<Dishes> filteredDishes = dishRepository.findByTagsContaining(tagId);
		// Step 3: Convert to response DTOs
        List<DishesResponseDTO> dishesListByTag = new ArrayList<>();

        if (!filteredDishes.isEmpty()) {
            for (Dishes dish : filteredDishes) {
                DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
                dishesListByTag.add(dto);
            }
            return new ResponseResult(200, "success", dishesListByTag);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Retrieves top dishes (deafult top 5).
     * @return ResponseResult object containing status, message and the top dishes.
     */
    @Override
    public ResponseResult getTopDishes(int topNum) {
        List<Dishes> dishes = dishRepository.findAll();
        List<Dishes> topDishes = dishes.stream()
                .sorted(Comparator.comparing(Dishes::getRating).reversed())
                .limit(topNum)
                .collect(Collectors.toList());
        List<DishesResponseDTO> topDishesList = new ArrayList<>();

        if (!topDishes.isEmpty()) {
            for (Dishes dish : topDishes) {
                Optional<Restaurant> restaurant = restaurantRepository.findById(dish.getRestaurantId());
                DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
                topDishesList.add(dto);
            }
            return new ResponseResult(200, "success", topDishesList);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Retrieves top dishes (deafult top 5) with specific tag.
     * @return ResponseResult object containing status, message and the top dishes.
     */
    @Override
    public ResponseResult getTopDishesBasedOnTag(String tagName, int topNum) {
        // Step 1: Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {return new ResponseResult(400, "Tag not found");}
        // Step 2: Get the tag ID
        ObjectId tagId = tagOptional.get().getId();
        // Step 3: Use the tag ID to retrieve restaurants
        List<Dishes> sortedDishes = dishRepository.sortedDishesByRatingBasedOnTagId(tagId);
        if (sortedDishes.isEmpty()) {return new ResponseResult(400, "No dishes found for the given tag");}
        // Step 4: Get the top N restaurants
        List<Dishes> topDishes = sortedDishes.subList(0, Math.min(topNum, sortedDishes.size()));
        // Step 5: Convert to response DTOs
        List<DishesResponseDTO> topDishesList = topDishes.stream()
                .map(restaurant -> dishesResponseDTOConverter.convertToResponseDTO(restaurant))
                .collect(Collectors.toList());
        return new ResponseResult(200, "success", topDishesList);
    }

    // Helper method to convert DTO to Entity
    private Dishes convertToEntity(DishesDTO dishDTO) {
        Dishes dish = new Dishes();
        dish.setDishName(dishDTO.getDishName());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setRestaurantId(dishDTO.getRestaurantId());
        dish.setReviewCount(dishDTO.getReviewCount());
        /* Transfer tagName to tagId*/
        ArrayList<ObjectId> tagIds = new ArrayList<>();
        /* transfer tag id to tag name when expose */
        for (String tagName : dishDTO.getTags()){
            Optional<Tags> tag = tagsRepository.findByTagName(tagName);
            tag.ifPresent(tags -> tagIds.add(tags.getId()));
        }
        dish.setTags(tagIds);
        return dish;
    }

    @Override
    public ResponseResult searchDishes(String keyword, String location) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}
        // First, find restaurants matching the location
        List<Restaurant> restaurants = restaurantRepository.findByLocationContainingIgnoreCase(location);
        List<ObjectId> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());
        // Find dishes matching the keyword and restaurant IDs
        List<Dishes> dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdIn(keyword, restaurantIds);
        // Map dishes to DTOs
        List<DishesResponseDTO> dtos = dishes.stream()
                .map(dish -> {
                    Optional<Restaurant> restaurantOpt = restaurants.stream()
                            .filter(r -> r.getId().equals(dish.getRestaurantId()))
                            .findFirst();
                    Restaurant restaurant = restaurantOpt.orElse(null);
                    return dishesResponseDTOConverter.convertToResponseDTO(dish);
                })
                .collect(Collectors.toList());
        // Check if the list is empty
        if (dtos.isEmpty()) {
            return new ResponseResult(404, "No dishes found");
        } else {
            return new ResponseResult(200, "success", dtos);
        }
    }
    @Override
    public ResponseResult searchDishes(String name) {
        List<Dishes> dishes = dishRepository.findByDishNameContainingIgnoreCase(name);

        if (!dishes.isEmpty()) {
            // Log the ratings before sorting
//            System.out.println("Ratings before sorting:");
//            for (Dishes dish : dishes) {
//                System.out.println("Dish: " + dish.getDishName() + ", Rating: " + dish.getRating());
//            }
            // Sort the dishes by rating in descending order
            dishes.sort(Comparator.comparing(Dishes::getRating, Comparator.nullsFirst(Double::compareTo)).reversed());
            // Log the ratings after sorting
//            System.out.println("Ratings after sorting:");
//            for (Dishes dish : dishes) {
//                System.out.println("Dish: " + dish.getDishName() + ", Rating: " + dish.getRating());
//            }
            List<DishesResponseDTO> dishList = new ArrayList<>();
            for (Dishes dish : dishes) {
                Optional<Restaurant> restaurantOpt = restaurantRepository.findById(dish.getRestaurantId());
                if (restaurantOpt.isPresent()) {
                    DishesResponseDTO dto = dishesResponseDTOConverter.convertToResponseDTO(dish);
                    dishList.add(dto);
                } else {
                    // Handle the case where the restaurant is not found, if necessary
                }
            }
            return new ResponseResult(200, "Success", dishList);
        } else {
            return new ResponseResult(404, "Dish not found");
        }
    }

    @Override
    public ResponseResult searchTopDishes(String keyword, String location, String tagName, int topNum) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}

        // Validate topNum
        if (topNum <= 0) {
            return new ResponseResult(400, "Invalid value for topNum. It must be a positive integer.");
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
                return new ResponseResult(404, "No restaurants found in the specified location");
            }

        } else {
            // Location is not provided; retrieve all restaurants
            restaurants = restaurantRepository.findAll();

            if (restaurants.isEmpty()) {
                return new ResponseResult(404, "No restaurants found");
            }
        }

        // Get the list of restaurant IDs
        restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        // Retrieve matching dishes
        List<Dishes> dishes;

        if (tagName != null && !tagName.isEmpty()) {
            // Tag is provided
            // Find the tag by name
            Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
            if (tagOptional.isPresent()) {
                ObjectId tagId = tagOptional.get().getId();
                // Find dishes matching the keyword, restaurant IDs, and tag
//                dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdInAndTagsContaining(keyword, restaurantIds, tagId);
				dishes = dishRepository.findByTagsContainingAndRestaurantIdIn(tagId,restaurantIds);
            } else {
                return new ResponseResult(400, "Tag not found");
            }
        } else {
            // No tag provided
//            dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdIn(keyword, restaurantIds);
			dishes = dishRepository.findByRestaurantIdIn(restaurantIds);
        }

		/* Filter offers by keyword */
		String finalKeyword = keyword.toLowerCase();
		// find based on dishName/*description/tag/restaurantName/restaurantLocation.
		List<Dishes> filteredDishes = dishes.stream()
				.filter(dish -> {
					// 1. Match dish's name
					boolean dishNameMatch = dish.getDishName().toLowerCase().contains(finalKeyword);
					// 2. Match dish's description
					boolean descriptionMatch = dish.getDescription().toLowerCase().contains(finalKeyword);
					// 3. Match dish's tag
					boolean tagMatch = 	dish.getTags().stream()
							.map(dishTagId -> tagsRepository.findById(dishTagId))
							.filter(Optional::isPresent)
							.map(Optional::get)
							.map(Tags::getTagName)
							.anyMatch(dishTagName -> dishTagName.toLowerCase().contains(finalKeyword));
					Optional<Restaurant> dishRestaurant = restaurantRepository.findById(dish.getRestaurantId());
					boolean restaurantNameMatch = false;
					boolean restaurantLocationMatch = false;
					if (dishRestaurant.isPresent()){
						// 4. Match dish's corresponding restaurant name
						restaurantNameMatch = dishRestaurant.get().getName().toLowerCase().contains(finalKeyword);
						// 5. Match dish's corresponding restaurant location
						Optional<String> regionName = Optional.empty();
						Optional<String> areaName = Optional.empty();
						if (dishRestaurant.get().getLocation().get("region") != null) {
							regionName = regionsRepository.findById(dishRestaurant.get().getLocation().get("region"))
									.map(Regions::getRegionName);
						}
						if (dishRestaurant.get().getLocation().get("area") != null) {
							areaName = areasRepository.findById(dishRestaurant.get().getLocation().get("area"))
									.map(Areas::getAreaName);
						}
						restaurantLocationMatch = regionName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false) ||
								areaName.map(name -> name.toLowerCase().contains(finalKeyword)).orElse(false);
					}
					return dishNameMatch || descriptionMatch || tagMatch || restaurantNameMatch || restaurantLocationMatch;
				}).collect(Collectors.toList());

        if (filteredDishes.isEmpty()) {
            return new ResponseResult(404, "Dish not found for your search text");
        }

        // Sort the dishes by rating in descending order
		filteredDishes.sort(Comparator.comparing(Dishes::getRating, Comparator.nullsFirst(Double::compareTo)).reversed());

        // Limit the results to the topNum
        List<Dishes> topDishes = filteredDishes.stream()
                .limit(topNum)
                .collect(Collectors.toList());

        // Map dishes to DTOs
        List<DishesResponseDTO> dtos = topDishes.stream()
                .map(dish -> {
                    Optional<Restaurant> restaurantOpt = restaurants.stream()
                            .filter(r -> r.getId().equals(dish.getRestaurantId()))
                            .findFirst();
                    Restaurant restaurant = restaurantOpt.orElse(null);
                    return dishesResponseDTOConverter.convertToResponseDTO(dish);
                })
                .collect(Collectors.toList());

        return new ResponseResult(200, "Success", dtos);
    }

    @Override
	public ResponseResult searchTrendingDishes(String keyword, String location, String tagName) {
        if (keyword == null) {
			keyword = "";
		}
        if (location == null) {
			location = "";
		}

        List<Dishes> dishes;
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
            // Retrieve restaurants in that location first
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
            List<Restaurant> restaurantsByLocation = restaurantRepository.filteredRestaurantsByLocation(locationId);

            if (restaurantsByLocation.isEmpty()) {
                return new ResponseResult(404, "No dishes found for your location");
            }

            // Get the list of restaurant IDs
            List<ObjectId> restaurantIds = restaurantsByLocation.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toList());
            // Retrieve matching dishes based on tag
            if (tagId != null) {
                // Tag is provided
                dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdInAndTagsContaining(keyword, restaurantIds, tagId);
            } else {
                // No tag provided
                dishes = dishRepository.findByDishNameContainingIgnoreCaseAndRestaurantIdIn(keyword, restaurantIds);
            }

        }else {
            // Location is not provided; retrieve all dishes matching the keyword
            if (tagId != null) {
                // Tag is provided
                dishes = dishRepository.findByDishNameContainingIgnoreCaseAndTagsContaining(keyword, tagId);
            } else {
                // No tag provided
                dishes = dishRepository.findByDishNameContainingIgnoreCase(keyword);
            }
        }
        if (dishes.isEmpty()) {
            return new ResponseResult(404, "No dishes found for your search text");
        }
        List<ObjectId> dishIds = dishes.stream()
                .map(Dishes::getId)
                .collect(Collectors.toList());

        // Define the date 14 days ago from now
        Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);

        // Aggregate posts
        MatchOperation matchOperation = Aggregation.match(Criteria.where("updated_at").gte(fourteenDaysAgo)
				.and("dish_id").in(dishIds));

        GroupOperation groupOperation = Aggregation.group("dish_id")
                .first("dish_id").as("entityId")
                .count().as("postCount")
                .avg("rating").as("avgRating");

        MatchOperation filterAvgRating = Aggregation.match(Criteria.where("avgRating").gt(4.5));

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "postCount"));

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
            return new ResponseResult(404, "No dishes found");
        }else{
            ObjectId trendingDishId = mappedResults.get(0).getEntityId();
			double avgRating = mappedResults.get(0).getAvgRating();
            if (trendingDishId != null) {
				return getTrendingDishById(trendingDishId, avgRating);
            }
        }

        return new ResponseResult(404, "No dish satisfies requirements for trending dish");
    }

    @Override
    public ResponseResult getTopDishesBasedOnLocation(String location, int topNum) {

        Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);
        Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
        if (!regionOptional.isPresent() && !areaOptional.isPresent()) { return  new ResponseResult(404, "Location not found");}

        List<Dishes> sortedDishes = new ArrayList<>();
        if (regionOptional.isPresent()){
            ObjectId regionId = regionOptional.get().getId();
            sortedDishes = dishRepository.sortedDishesByRatingLocation(regionId);
        }

        if (areaOptional.isPresent()){
            ObjectId areaId = areaOptional.get().getId();
            sortedDishes = dishRepository.sortedDishesByRatingLocation(areaId);
        }

        if (sortedDishes.isEmpty()) {return new ResponseResult(404, "No dishes found for the given location");}

        // Get the top N restaurants
        List<Dishes> topDishes = sortedDishes.subList(0, Math.min(topNum, sortedDishes.size()));

        // Convert to response DTOs
        List<DishesResponseDTO> topDishesList = topDishes.stream()
                .map(restaurant -> dishesResponseDTOConverter.convertToResponseDTO(restaurant))
                .collect(Collectors.toList());
        return new ResponseResult(200, "success", topDishesList);
    }


    @Override
    public ResponseResult getTopDishesBasedOnTagAndLocation(String tagName, String location, int topNum) {
        // Find the tag by name
        Optional<Tags> tagOptional = tagsRepository.findByTagName(tagName);
        if (!tagOptional.isPresent()) {
            return new ResponseResult(404, "Tag not found");
        }

        // Get the tag ID
        ObjectId tagId = tagOptional.get().getId();

        // Find the region or area by name
        Optional<Regions> regionOptional = regionsRepository.findByRegionName(location);
        Optional<Areas> areaOptional = areasRepository.findByAreaName(location);
        if (!regionOptional.isPresent() && !areaOptional.isPresent()) { return  new ResponseResult(404, "Location not found");}

        // Filter dishes by location
        List<Dishes> dishesByLocation = new ArrayList<>();
        if (regionOptional.isPresent()){
            ObjectId regionId = regionOptional.get().getId();
            dishesByLocation = dishRepository.findDishesByLocation(regionId);
        }

        if (areaOptional.isPresent()){
            ObjectId areaId = areaOptional.get().getId();
            dishesByLocation = dishRepository.findDishesByLocation(areaId);
        }

        if (dishesByLocation.isEmpty()) {
            return new ResponseResult(404, "No dishes found for the given location");
        }

        // Filter dishes by the given tag and sort by rating in descending order
        List<Dishes> sortedDishes = dishesByLocation.stream()
                .filter(dish -> dish.getTags().contains(tagId))  // filter by tag ID
                .sorted(Comparator.comparing(Dishes::getRating).reversed())  // sort by dish rating in descending order
                .collect(Collectors.toList());

        if (sortedDishes.isEmpty()) {
            return new ResponseResult(404, "No dishes found for the given tag and location");
        }

        // Get the top N dishes
        List<Dishes> topDishes = sortedDishes.subList(0, Math.min(topNum, sortedDishes.size()));

        // Convert top dishes to response DTOs
        List<DishesResponseDTO> topDishesList = topDishes.stream()
                .map(dish -> dishesResponseDTOConverter.convertToResponseDTO(dish))
                .collect(Collectors.toList());

        // Return the successful response
        return new ResponseResult(200, "success", topDishesList);
    }



}

