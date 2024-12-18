package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.AggregationResult;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.PostsResponseDTOConverter;
import com.kula.kula_project_backend.dao.*;
import com.kula.kula_project_backend.dto.requestdto.PostsDTO;
import com.kula.kula_project_backend.dto.responsedto.PostsResponseDTO;
import com.kula.kula_project_backend.entity.*;
import com.kula.kula_project_backend.query.PostsQuery;
import com.kula.kula_project_backend.service.IAzureBlobService;
import com.kula.kula_project_backend.service.IPostsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kula.kula_project_backend.security.TextReviewService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Service implementation for Posts operations.
 * This service is used to save, delete and get Posts.
 * The service uses the PostsRepository to interact with the database.
 * The service uses the UsersRepository to interact with the database.
 * The service uses the PostsDTO to save the Posts.
 * The service uses the PostsResponseDTO to convert Posts to PostsResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the Posts.
 * The service uses the PostsQuery to get the query parameters for the Posts.
 */
@Service
public class PostsServiceImpl implements IPostsService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DishesRepository dishesRepository;
    @Autowired
    private IAzureBlobService azureBlobService;
    @Autowired
    private TextReviewService textReviewService;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private PostsResponseDTOConverter postsResponseDTOConverter;
    @Value("${azure.blob.container-endpoint}")
    private String containerEndpoint;
    /**
     * Retrieves all posts.
     * @return ResponseResult object containing status, message and all posts.
     */
    @Cacheable(value = "postsGetAll")
    @Override
    public ResponseResult getAll() {
        //return new ResponseResult(200, "success", postsRepository.findAll());
        List<Posts> posts = postsRepository.findAll();
        List<PostsResponseDTO> postList = new ArrayList<>();
        if (!posts.isEmpty()) {
            for (Posts post : posts) {
                PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(post);
                postList.add(dto);
            }
            return new ResponseResult(200, "success", postList);
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Saves a new post or updates an existing one.
     * @param postsDTO The DTO containing the post details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(PostsDTO postsDTO) {
        ResponseResult validationResult = validatePost(postsDTO,false);
        if (validationResult.getCode()!=200){return validationResult;}
        Posts post = convertToEntity(postsDTO);
        postsRepository.insert(post);
        return new ResponseResult(200, "success", post.getId().toString());
    }
    /**
     * Retrieves a post by its ID.
     * @param id The ID of the post.
     * @return ResponseResult object containing status, message and the post.
     */
    @Cacheable(value = "postsGetById", key = "#id")
    @Override
    public ResponseResult getById(ObjectId id) {
        Optional<Posts> posts = postsRepository.findById(id);
        if (posts.isPresent()) {
            PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(posts.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Retrieves posts by various parameters.
     * @param postsQuery The query containing the parameters.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Override
    public ResponseResult listByParams(PostsQuery postsQuery) {
        Criteria criteria = new Criteria();

        // Adding conditions based on provided query parameters
        if (postsQuery.getAuthId() != null) {
            criteria.and("auth_id").is(postsQuery.getAuthId());
        }
        if (postsQuery.getTitle() != null) {
            criteria.and("title").regex(postsQuery.getTitle(), "i"); // Case-insensitive search
        }
        if (postsQuery.getDishId() != null) {
            criteria.and("dish_id").is(postsQuery.getDishId());
        }
        if (postsQuery.getTags() != null && postsQuery.getTags().length > 0) {
            criteria.and("tags").in((Object[]) postsQuery.getTags()); // Matches any of the provided tags
        }
        if (postsQuery.getContent() != null) {
            criteria.and("content").regex(postsQuery.getContent(), "i");
        }
        if (postsQuery.getCreatedAt() != null) {
            // Assuming createdAt is a date, you might need specific date handling here
            criteria.and("created_at").is(postsQuery.getCreatedAt());
        }
        if (postsQuery.getUpdatedAt() != null) {
            // Similarly for updatedAt
            criteria.and("updated_at").is(postsQuery.getUpdatedAt());
        }
        if (postsQuery.getRating() > 0) {
            criteria.and("rating").is(postsQuery.getRating());
        }

        Query query = Query.query(criteria);
        List<Posts> postsList = mongoTemplate.find(query, Posts.class);
        List<PostsResponseDTO> responseList = new ArrayList<PostsResponseDTO>();
        for (Posts post : postsList) {
            PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(post);
            responseList.add(dto);
        }

        if (!postsList.isEmpty()) {
            return new ResponseResult(200, "success", responseList);
        } else {
            return new ResponseResult(400, "No matching posts found.");
        }
    }
    /**
     * Retrieves posts by tag names.
     * @param postsQuery The query containing the tag names.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Cacheable(value = "postsListByTitle", key = "#postsQuery")
    @Override
    public ResponseResult listByTagNames(PostsQuery postsQuery) {
        Criteria criteria = new Criteria();

        // Adding conditions based on provided query parameters
        if (postsQuery.getTags() != null && postsQuery.getTags().length > 0) {
            // Convert tag names to tag IDs
            List<String> tagNames = Arrays.asList(postsQuery.getTags());

            Query tagQuery = Query.query(Criteria.where("tagName").in(tagNames));
            List<Tags> tags = mongoTemplate.find(tagQuery, Tags.class);
            // List<ObjectId> tagIds = tags.stream().map(tag -> new
            // ObjectId(tag.getId())).collect(Collectors.toList());
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            for (Tags tag : tags) {
                tagIds.add(new ObjectId(String.valueOf(tag.getId())));
            }

            criteria.andOperator(Criteria.where("tags").all(tagIds));
        }

        Query query = Query.query(criteria);
        List<Posts> postsList = mongoTemplate.find(query, Posts.class);
        List<PostsResponseDTO> responseList = new ArrayList<PostsResponseDTO>();
        for (Posts post : postsList) {
            PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(post);
            responseList.add(dto);
        }

        if (!postsList.isEmpty()) {
            return new ResponseResult(200, "success", responseList);
        } else {
            return new ResponseResult(400, "No matching posts found.");
        }
    }
    /**
     * Updates a post.
     * @param postsDTO The DTO containing the updated post details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult update(PostsDTO postsDTO) {
        ResponseResult validationResult = validatePost(postsDTO,true);
        if (validationResult.getCode()!=200){return validationResult;}
        Optional<Posts> posts = postsRepository.findById(postsDTO.getId());
        if (posts.isPresent()) {
            Posts updatedPost = convertToEntity(postsDTO);
            updatedPost.setId(posts.get().getId());
            postsRepository.save(updatedPost);
            return new ResponseResult(200, "Post updated successfully: ", updatedPost.getId().toString());
        }
        return new ResponseResult(400, "Post not found");
    }
    /**
     * Deletes a post by its ID.
     * @param id The ID of the post.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deletePost(ObjectId id) {
        Optional<Posts> posts = postsRepository.findById(id);
        if (posts.isPresent()) {
            postsRepository.deleteById(id);
            return new ResponseResult(200, "success");
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Retrieves posts by date after a specified date.
     * @param postsQuery The query containing the date.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Override
    public ResponseResult listByDateAfter(PostsQuery postsQuery) {
        Criteria criteria = new Criteria();

        // Convert the provided createdAt string to a Date object
        if (postsQuery.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure the time zone is UTC if that's what you store in DB

            criteria.and("created_at").gt(postsQuery.getCreatedAt()); // Use less than for dates before the specified
                                                                      // date

        } else {
            return new ResponseResult(400, "No date parameter provided.");
        }

        try {
            Query query = Query.query(criteria);
            List<Posts> postsList = mongoTemplate.find(query, Posts.class);
            if (postsList.isEmpty()) {
                return new ResponseResult(404, "No matching posts found.");
            }

            List<PostsResponseDTO> responseList = new ArrayList<PostsResponseDTO>();
            for (int i = 0; i < postsList.size(); i++) {
                PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(postsList.get(i));
                responseList.add(dto);
            }
            return new ResponseResult(200, "success", responseList);
        } catch (DataAccessException e) {
            return new ResponseResult(500, "Database access error: " + e.getMessage());
        }
    }
    /**
     * Retrieves posts by start and end dates.
     * @param postsQuery The query containing the start and end dates.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Override
    public ResponseResult listByStartDateAndEndDate(PostsQuery postsQuery) {
        Criteria criteria = new Criteria();

        // Convert the provided startDate and endDate strings to Date objects
        if (postsQuery.getStartDate() != null && postsQuery.getEndDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure the time zone is UTC if that's what you store in DB

            if (postsQuery.getStartDate() == null || postsQuery.getEndDate() == null) {
                return new ResponseResult(400, "Both start and end dates must be provided.");
            }
            if (postsQuery.getStartDate().after(postsQuery.getEndDate())) {
                return new ResponseResult(400, "Start date must be before end date.");
            }

            Date startDate = postsQuery.getStartDate();
            Date endDate = postsQuery.getEndDate();

            criteria.andOperator(
                    Criteria.where("created_at").gte(startDate),
                    Criteria.where("created_at").lte(endDate));

        } else {
            return new ResponseResult(400, "No date parameters provided.");
        }

        try {
            Query query = Query.query(criteria);
            List<Posts> postsList = mongoTemplate.find(query, Posts.class);
            if (postsList.isEmpty()) {
                return new ResponseResult(404, "No matching posts found.");
            }

            List<PostsResponseDTO> responseList = new ArrayList<PostsResponseDTO>();
            for (int i = 0; i < postsList.size(); i++) {
                PostsResponseDTO dto = postsResponseDTOConverter.convertToResponseDTO(postsList.get(i));
                responseList.add(dto);
            }
            return new ResponseResult(200, "success", responseList);
        } catch (DataAccessException e) {
            return new ResponseResult(500, "Database access error: " + e.getMessage());
        }
    }
    /**
     * Retrieves posts by the amount of comments.
     * @param amount The amount of comments.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Override
    public ResponseResult listByCommentsAmount(Integer amount) {
        // Using MongoDB aggregation to count comments per post and sort by this count
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("comments", "_id", "post_id", "comments"),
                Aggregation.unwind("comments", true),
                Aggregation.group("_id")
                        .first("$$ROOT").as("post")
                        .count().as("commentsCount"),
                Aggregation.sort(Sort.Direction.DESC, "commentsCount"),
                Aggregation.limit(amount));

        // Execute aggregation
        List<AggregationResult> results = mongoTemplate.aggregate(aggregation, Posts.class, AggregationResult.class)
                .getMappedResults();

        // Convert to DTO
        List<PostsResponseDTO> responseList = results.stream()
                .map(result -> postsResponseDTOConverter.convertToResponseDTO(result.getPost()))
                .collect(Collectors.toList());

        if (!responseList.isEmpty()) {
            return new ResponseResult(200, "success", responseList);
        } else {
            return new ResponseResult(400, "No matching posts found.");
        }
    }
    /**
     * Retrieves image URLs by post ID.
     * @param id The ID of the post.
     * @return Array of image URLs.
     */
    @Override
    public String[] getImageURLsByPostId(ObjectId id) {
        Optional<Posts> post = postsRepository.findById(id);
        if (post.isPresent()) {
            String[] imageURLs = post.get().getImageURL();
            for (int i = 0; i < imageURLs.length; i++) {
                imageURLs[i] = containerEndpoint + "/" + imageURLs[i];
            }
            return imageURLs;
        }
        return new String[0];
    }
    /**
     * Retrieves the amount of likes a post has.
     * @param id The ID of the post.
     * @return ResponseResult object containing status, message and the amount of likes.
     */
    @Override
    public ResponseResult getLikesAmount(ObjectId id) {
        return null;
    }
    /**
     * Retrieves posts by user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the posts.
     */
    @Override
    public ResponseResult getPostsByUserId(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users user = users.get();
            Query query = Query.query(Criteria.where("auth_id").is(user.getId()));
            List<Posts> postsList = mongoTemplate.find(query, Posts.class);
            List<PostsResponseDTO> responseList = postsList.stream()
                    .map(postsResponseDTOConverter::convertToResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseResult(200, "success", responseList);
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves the amount of posts a user has.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the amount of posts.
     */
    @Override
    public ResponseResult getPostsAmountByUserId(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users user = users.get();
            Query query = Query.query(Criteria.where("auth_id").is(user.getId()));
            List<Posts> postsList = mongoTemplate.find(query, Posts.class);
            return new ResponseResult(200, "success", postsList.size());
        } else {
            return new ResponseResult(400, "fail");
        }
    }

    /**
     * Helper Function to upload images for Restaurant/Dish posts.
     * @param images The image files waiting for upload.
     * @param post The post object to be updated.
     * @return updated post object.
     */
    public Posts uploadImages(MultipartFile[] images, Posts post) {
        ResponseResult uploadResult = azureBlobService.uploadMultipleFiles(images);
        if (uploadResult.getCode() != 200) {return null;}
        if (uploadResult.getData() instanceof ArrayList<?>) {
            // Extract filenames from Object to ArrayList<String>
            ArrayList<String> newFilenames = ((ArrayList<?>) uploadResult.getData()).stream()
                    .filter(String.class::isInstance)         // Filter non-String elements
                    .map(String.class::cast)                  // Transfer Object to String
                    .collect(Collectors.toCollection(ArrayList::new)); // Collect into ArrayList<String>
            // Check if image filename arraylist is null，initialize it if so
            if (post.getImages() == null) {post.setImages(new ArrayList<>());}
            // Add dish Images
            post.getImages().addAll(newFilenames);
        }
        return post;
    }

    /**
     * Upload (more) images for Restaurant/Dish posts.
     * @param id The ID of Post.
     * @param files The image files to be uploaded.
     * @return ResponseResult object containing status, message and the amount of posts.
     */
    @Override
    public ResponseResult uploadImagesById(ObjectId id, MultipartFile[] files) {
        Optional<Posts> posts = postsRepository.findById(id);
        if (posts.isPresent()) {
            ResponseResult uploadResult = azureBlobService.uploadMultipleFiles(files);
            if (uploadResult.getCode() != 200) {return uploadResult;}
            if (uploadResult.getData() instanceof ArrayList<?>) {
                // Extract filenames from Object to ArrayList<String>
                ArrayList<String> newFilenames = ((ArrayList<?>) uploadResult.getData()).stream()
                        .filter(String.class::isInstance)         // Filter non-String elements
                        .map(String.class::cast)                  // Transfer Object to String
                        .collect(Collectors.toCollection(ArrayList::new)); // Collect into ArrayList<String>
                // Check if image filename arraylist is null，initialize it if so
                if (posts.get().getImages() == null) {posts.get().setImages(new ArrayList<>());}
                // Add dish Images
                posts.get().getImages().addAll(newFilenames);
                // Store modified dish object back into database
                postsRepository.save(posts.get());
                return new ResponseResult(200, "success add images to dish id: " + id, newFilenames);
            }
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Get posts list for feed.
     * @param page the page number
     * @param pageSize the number of posts in each page
     * @return The paginated list of posts ordered by createdAt Desc.
     */
    @Override
    public ResponseResult getFeed(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Posts> postsPage = postsRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostsResponseDTO> convertedList = postsPage.getContent().stream()
                .map(postsResponseDTOConverter::convertToResponseDTO)
                .collect(Collectors.toList());
        if (!convertedList.isEmpty()) {
            return new ResponseResult(200, "success", convertedList);
        } else {
            return new ResponseResult(404, "no more posts");
        }
    }

    // Helper method for validate post
    private ResponseResult validatePost(PostsDTO postsDTO,Boolean is_update){
        Optional<Dishes> dish = Optional.empty();
        Optional<Restaurant> restaurant = Optional.empty();
        if (postsDTO.getDishId()!=null){dish = dishesRepository.findById(postsDTO.getDishId());}
        if (postsDTO.getRestaurantId()!=null){restaurant = restaurantRepository.findById((postsDTO.getRestaurantId()));}
        /* Validate frontend should not provide post id when create post */
        if (!is_update && postsDTO.getId()!=null){return new ResponseResult(400,"ID should be null when create a new post");}
        /* Validate frontend should provide post id when update post */
        if (is_update && postsDTO.getId()==null){return new ResponseResult(400,"ID should not be null when update an existing post");}
        /* Validate user authentication */
        if (!usersRepository.findById(postsDTO.getAuthId()).isPresent()) {return new ResponseResult(400, "User not found.");}
        /* Validate restaurant id and dish id*/
        // 1. if the post not contains restaurantId or dishId, return error.
        if (!restaurant.isPresent() && !dish.isPresent()){return new ResponseResult(400, "No valid restaurant/dish id provided");}
        // 2. else if both restaurantId or dishId provided, test if the dish is contained by the restaurant
        else if (restaurant.isPresent() && dish.isPresent()){if (!restaurant.get().getId().equals(dish.get().getRestaurantId())){return new ResponseResult(400, "Provided dish not included by the restaurant");}}
        return new ResponseResult(200,"post is valid");
    }

    // Helper method to convert DTO to Entity
    private Posts convertToEntity(PostsDTO postsDTO){
        Posts posts = new Posts();
        Optional<Dishes> dish = Optional.empty();
        if (postsDTO.getDishId()!=null){dish = dishesRepository.findById(postsDTO.getDishId());}
        // if the post is related to a dish, then restaurantId will be added automatically.
        if (dish.isPresent()){
            ObjectId restaurantId = dish.get().getRestaurantId();
            posts.setRestaurantId(restaurantId);
        }
        posts.setTitle(textReviewService.policy.sanitize(postsDTO.getTitle()));
        posts.setContent(textReviewService.policy.sanitize(postsDTO.getContent()));
        posts.setAuthId(postsDTO.getAuthId());
        posts.setCreatedAt(new Date());
        posts.setDishId(postsDTO.getDishId());
        if (postsDTO.getImageURL() != null) {
            String[] imageURLs = postsDTO.getImageURL();
            posts.setImageURL(imageURLs);
        }
        /*save images*/
        if (postsDTO.getImages() != null){posts = uploadImages(postsDTO.getImages(),posts);}
        posts.setRating(postsDTO.getRating());
        /* Transfer tagName to tagId*/
        ArrayList<ObjectId> tagIds = new ArrayList<>();
        /* transfer tag id to tag name when expose */
        for (String tagName : postsDTO.getTags()){
            Optional<Tags> tag = tagsRepository.findByTagName(tagName);
            tag.ifPresent(tags -> tagIds.add(tags.getId()));
        }
        posts.setTags(tagIds);
        posts.setUpdatedAt(new Date());
        posts.setVideoURL(textReviewService.policy.sanitize(postsDTO.getVideoURL()));
        return posts;
    }
}
