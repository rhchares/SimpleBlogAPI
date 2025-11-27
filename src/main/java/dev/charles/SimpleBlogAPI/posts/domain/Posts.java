package dev.charles.SimpleBlogAPI.posts.domain;

import dev.charles.SimpleBlogAPI.comments.domain.Comments;
import dev.charles.SimpleBlogAPI.posts.dto.PostDto;
import dev.charles.SimpleBlogAPI.users.domain.BaseEntity;
import dev.charles.SimpleBlogAPI.users.domain.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Posts extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private Users createdBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();
    @Version
    private Long version;

    public Posts(String title, String content){
        this.title = title;
        this.content = content;
    }

    @Column(name = "post_tsv", columnDefinition = "tsvector",insertable = false)
    private String post_tsv;

    public static Posts of(PostDto postDto){
        return new Posts(postDto.getTitle(), postDto.getContent());
    }
    public void update(PostDto postDto){
        this.content = postDto.getContent();
        this.title = postDto.getTitle();
    }

    public void setUser(Users user){
        this.createdBy = user;
    }


}
