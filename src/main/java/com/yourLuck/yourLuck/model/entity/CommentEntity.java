package com.yourLuck.yourLuck.model.entity;

import com.yourLuck.yourLuck.model.CommentStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter @Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "comment")
    private String comment;
//부모 커멘트
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;
//자식커멘트(대댓글)
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<CommentEntity> children = new ArrayList<>();

//    댓글의 상태를 나타내는 필드
//    삭제 되지 않은 댓글은 'ACTIVE' , 삭제 된 댓글은 DELETE
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentStatus status;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
    @Column(name = "removed_at")
    private Timestamp removedAt;

    public void delete(){
        this.comment = "이 댓글은 삭제되었습니다.";
    }

    public static CommentEntity of(UserEntity userEntity, PostEntity postEntity, String comment,CommentEntity parent) {
        CommentEntity entity = new CommentEntity();
        entity.setUser(userEntity);
        entity.setPost(postEntity);
        entity.setComment(comment);
        entity.setParent(parent);
        return entity;
    }

}
