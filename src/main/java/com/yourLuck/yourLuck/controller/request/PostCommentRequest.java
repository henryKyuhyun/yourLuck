package com.yourLuck.yourLuck.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequest {
    private String comment;
    private Integer parentId;
}
