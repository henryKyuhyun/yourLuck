package com.yourLuck.yourLuck.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequest {
    private String comment;
    private Integer parentId;
}
