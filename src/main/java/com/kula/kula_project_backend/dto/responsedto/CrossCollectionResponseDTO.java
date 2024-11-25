package com.kula.kula_project_backend.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * CrossCollectionResponseDTO is a data transfer object for the response of an
 * another entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Only include non-null fields
public class CrossCollectionResponseDTO {


	/**
	 * Used when returning location of a dish, where the location information is
	 * extracted from Restaurant collection.
	 */
	@JsonProperty("Area")
	private String area;

	@JsonProperty("Region")
	private String region;

	/**
	 * Used when returning average rating of a dish from posts collection
	 */
	@JsonProperty("Average Rating From Posts - Rencent 14 days")
	private Double ratingFromPosts14Days = null;

	public CrossCollectionResponseDTO(String region, String area) {
		super();
		this.region = region;
		this.area = area;

	}

	public CrossCollectionResponseDTO(double ratingFromPosts14Days) {

		this.ratingFromPosts14Days = ratingFromPosts14Days;
	}

}
