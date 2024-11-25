package com.kula.kula_project_backend.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kula.kula_project_backend.entity.Offers;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kula.kula_project_backend.entity.Dishes;
/**
 * DishesRepository is a Spring Data MongoDB repository for the Dishes entity.
 */
@Repository
public interface DishesRepository extends MongoRepository<Dishes, ObjectId> {
    /**
     * Finds a dish by its id.
     * @param dishId The id of the dish.
     * @return An Optional that may contain the Dishes object if found.
     */
    @Override
	Optional<Dishes> findById(ObjectId dishId);

	/**
	 * Finds a dish by its id.
	 * 
	 * @param restaurantId The id of the dish.
	 * @return An Optional that may contain the Dishes object if found.
	 */
	List<Dishes> findByRestaurantId(ObjectId restaurantId);

	/**
	 * Finds a dish by its tag.
	 *
	 * @param tagName The id of the dish.
	 * @return An Optional that may contain the Dishes object if found.
	 */
	List<Dishes> findByTagsContainingIgnoreCase(String tagName);

    /**
     * Finds dishes by restaurant id and dish name (case insensitive).
     * @param restaurantId The id of the restaurant.
     * @param dishName The name of the dish.
     * @return A list of Dishes objects that match the given restaurant id and dish name.
     */
    @Query("{'restaurant_id': ?0, 'dish_name': {$regex: ?1, $options: 'i'}}")
    List<Dishes> findByRestaurantIdAndDishNameLikeIgnoreCase(ObjectId restaurantId, String dishName);

	@Aggregation(pipeline = {
			"{ '$match': { 'tags': {'$in': [?0]}}}", // Match dishes by tag
			"{ '$sort': { 'rating': -1 } }", // Sort by dish rating in descending order
	})

	//	List<Dishes> sortedDishesByRatingBasedOnTag(String tagName);

	// Alternatively, if you want to be explicit about the array containment
	List<Dishes> findByTagsContaining(ObjectId tagId);
	// Default method to sort the restaurants by rating
	default List<Dishes> sortedDishesByRatingBasedOnTagId(ObjectId tagId) {
		List<Dishes> dishes = findByTagsContaining(tagId);
		return dishes.stream()
				.sorted(
						Comparator.comparing(
								Dishes::getRating,
								Comparator.nullsFirst(Double::compareTo)
						).reversed()
				)
				.collect(Collectors.toList());
	}

	List<Dishes> findByDishNameContainingIgnoreCase(String name);
	List<Dishes> findByDishNameContainingIgnoreCaseAndRestaurantIdIn(String name, List<ObjectId> restaurantIds);


	@Aggregation(pipeline = {
			"{ '$lookup': { 'from': 'restaurants', 'localField': 'restaurant_id', " +
					"'foreignField': '_id', 'as': 'restaurant' } }",
			"{ '$unwind': '$restaurant' }",
			"{ '$match': { '$or': [ { 'restaurant.location.region': ?0 }, { 'restaurant.location.area': ?0 } ] } }",
			"{ '$sort': { 'rating': -1 } }"
	})
	List<Dishes> sortedDishesByRatingLocation(ObjectId areaOrRegionId);


	@Aggregation(pipeline = {
			"{ '$lookup': { 'from': 'restaurants', 'localField': 'restaurant_id', " +
					"'foreignField': '_id', 'as': 'restaurant' } }",
			"{ '$unwind': '$restaurant' }",
			"{ '$match': { '$or': [ { 'restaurant.location.region': ?0 }, { 'restaurant.location.area': ?0 } ] } }"
	})
	List<Dishes> findDishesByLocation(ObjectId areaOrRegionId);


	List<Dishes> findByDishNameContainingIgnoreCaseAndRestaurantIdInAndTagsContaining(String keyword, List<ObjectId> restaurantIds, ObjectId tagId);

	List<Dishes> findByDishNameContainingIgnoreCaseAndTagsContaining(String keyword, ObjectId tagId);

	List<Dishes> findByRestaurantIdIn(List<ObjectId> restaurantId);
	List<Dishes> findByTagsContainingAndRestaurantIdIn(ObjectId tagId, List<ObjectId> restaurantId);
}
