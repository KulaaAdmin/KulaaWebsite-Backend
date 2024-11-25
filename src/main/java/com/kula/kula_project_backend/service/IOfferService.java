package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import org.bson.types.ObjectId;
import com.kula.kula_project_backend.dto.requestdto.OffersDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOfferService {
    ResponseResult getAll();
    ResponseResult getById(ObjectId id);
    ResponseResult filterByTag(String tagName, int randomNum);
    ResponseResult getTopOffers(int topNum);
    ResponseResult getTopOffersBasedOnTag(String tag, int topNum);
    ResponseResult getTopNOffersBasedOnRestaurantId(ObjectId restaurantId, int topNum);
    ResponseResult uploadImageById(ObjectId id, MultipartFile file);
    ResponseResult deleteImage(ObjectId id);
    ResponseResult save(OffersDTO offerDTO);
    ResponseResult update(OffersDTO offerDTO);
    ResponseResult deleteOffer(ObjectId id);
    ResponseResult searchOffers(String keyword, String location, String tagName);
//    ResponseResult filterByLocation(String areaName, String regionName, int randomNum);
    ResponseResult filterByLocation(String location, int randomNum);
    ResponseResult filterByTagAndLocation(String tagName,  String location, int randomNum);
    ResponseResult getRandomOffers(int randomNum);
}
