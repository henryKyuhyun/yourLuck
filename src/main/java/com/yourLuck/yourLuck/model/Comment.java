package com.yourLuck.yourLuck.model;

import com.yourLuck.yourLuck.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Comment {

    private Integer id;
    private String comment;
    private String userName;
    private Integer PostId;
    private Integer parentId;
    private List<Comment> children = new ArrayList<>();
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity) {
        Comment comment = new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getUserName(),
                entity.getPost().getId(),
                entity.getParent() != null ? entity.getParent().getId() : null, // 수정
                new ArrayList<>(),  // children 초기화
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
        for(CommentEntity child : entity.getChildren()) { // 추가
            comment.getChildren().add(fromEntity(child));
        }
        return comment;
    }



}
