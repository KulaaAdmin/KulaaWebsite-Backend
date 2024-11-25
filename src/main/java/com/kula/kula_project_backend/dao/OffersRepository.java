package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Dishes;
import com.kula.kula_project_backend.entity.Offers;
import com.kula.kula_project_backend.entity.Restaurant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface OffersRepository extends MongoRepository<Offers, ObjectId> {

    Optional<Offers> findById(ObjectId OfferId);

    List<Offers> findByRestaurantId(ObjectId restaurantId);
    List<Offers> findByTagsContainingIgnoreCase(String tagName);

    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'restaurants', 'localField': 'restaurant_id', 'foreignField': '_id', 'as': 'restaurant' } }",
            "{ '$unwind': '$restaurant' }",
//            "{ '$match': { 'restaurant.rating': { '$ne': null } } }", // Ensure that the restaurant has a rating
            "{ '$sort': { 'restaurant.rating': -1 } }"
//            "{ '$limit': 5 }"
    })
    List<Offers> sortedOffersByRestaurantRating();

    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'restaurants', 'localField': 'restaurant_id', " +
                    "'foreignField': '_id', 'as': 'restaurant' } }",
            "{ '$unwind': '$restaurant' }",
            "{ '$match': { 'tags': ?0 }}",  // Direct match on ObjectId instead of regex
            "{ '$sort': { 'restaurant.rating': -1 } }"
    })
    List<Offers> sortedOffersByRestaurantRatingBasedOnTag(ObjectId tagId);

    @Aggregation(pipeline = {
            "{ '$match': { 'tags': {'$in': [?0]}}}", // Match offers by tag
    })
    // Alternatively, if you want to be explicit about the array containment
    List<Offers> findByTagsContaining(ObjectId tagId);
    List<Offers> findByOfferNameContainingIgnoreCase(String title);
    List<Offers> findByOfferNameContainingIgnoreCaseAndRestaurantIdIn(String title, List<ObjectId> restaurantIds);
    List<Offers> findByRestaurantIdIn(List<ObjectId> restaurantId);
    List<Offers> findByTagsContainingAndRestaurantIdIn(ObjectId tagId,List<ObjectId>  restaurantId);
    List<Offers> findByOfferNameContainingIgnoreCaseAndRestaurantIdInAndTagsContaining(String keyword, List<ObjectId> restaurantIds, ObjectId tagId);
}
