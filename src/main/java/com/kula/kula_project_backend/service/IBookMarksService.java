package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.BookMarksDTO;
import org.bson.types.ObjectId;

public interface IBookMarksService {

    ResponseResult saveBookMarks(ObjectId userId);

    ResponseResult addPostToBookMarks(BookMarksDTO bookMarksDTO);

    ResponseResult deletePostFromBookMarks(BookMarksDTO bookMarksDTO);

    ResponseResult getBookMarksByUserId(ObjectId userId);

    ResponseResult getBookMarksById(ObjectId id);
}
