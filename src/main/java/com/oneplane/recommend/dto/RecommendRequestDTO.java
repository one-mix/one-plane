package com.oneplane.recommend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRequestDTO {
    private Long userId;
    private String travelPurpose;
    private String companion;
}