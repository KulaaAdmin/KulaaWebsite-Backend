package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.entity.Restaurant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RestaurantRepository is a Spring Data MongoDB repository for the Restaurant entity.
 */
@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
    /**
     * Finds a restaurant by its id.
     * @param RestaurantId The id of the restaurant.
     * @return An Optional that may contain the Restaurant object if found.
     */
    Optional<Restaurant> findById(ObjectId RestaurantId);
    /**
     * Finds restaurants by their name (case insensitive).
     * @param name The name of the restaurant.
     * @return A list of Restaurant objects that match the given name.
     */
    List<Restaurant> findByNameLikeIgnoreCase(String name);
    @Aggregation(pipeline = {
            "{ '$match': { 'tags': {'$in': [?0]}}}", // Match restaurants by tag
            "{ '$sort': { 'rating': -1 } }", // Sort by restaurants rating in descending order
    })
    List<Restaurant> sortedRestaurantsByRatingBasedOnTag(String tagName);

    // Method to find restaurants where 'tags' contains the specified tagId
    List<Restaurant> findByTags(ObjectId tagId);

    // Alternatively, if you want to be explicit about the array containment
    List<Restaurant> findByTagsContaining(ObjectId tagId);

    // Default method to sort the restaurants by rating
    default List<Restaurant> sortedRestaurantsByRatingBasedOnTagId(ObjectId tagId) {
        List<Restaurant> restaurants = findByTagsContaining(tagId);
        return restaurants.stream()
                .sorted(
                        Comparator.comparing(
                                Restaurant::getRating,
                                Comparator.nullsFirst(Double::compareTo)
                        ).reversed()
                )
                .collect(Collectors.toList());
    }

    List<Restaurant> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    List<Restaurant> findByLocationContainingIgnoreCase(String location);

//    @Aggregation(pipeline = {
//            "{ '$match': { 'location': ?0 } }"
//    })
//    List<Restaurant> filteredRestaurantsByLocation(String location);
    @Aggregation(pipeline = {
            "{ '$match': { '$or': [ { 'location.region': ?0 }, { 'location.area': ?0 } ] } }"
    })
    List<Restaurant> filteredRestaurantsByLocation(ObjectId locationId);

    /**
     * Retrieve the name of All restaurants (with _id)
     * */
    @Query(value = "{}", fields = "{ 'name': 1 }")
    List<Restaurant> restaurantIdAndNames();

}
