// 작성자: 김동현
package com.oneplane.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeInfo {
    private Integer postId;
    private int likeCount;
    private boolean isLiked;
}
