package dev.charles.SimpleService.posts.service;

import dev.charles.SimpleService.errors.exception.NotFoundResourceException;
import dev.charles.SimpleService.posts.domain.Posts;
import dev.charles.SimpleService.posts.dto.PostDto;
import dev.charles.SimpleService.posts.repository.PostsRepository;
import dev.charles.SimpleService.users.domain.Users;

import dev.charles.SimpleService.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {
    final private PostsRepository postsRepository;
    final private UsersRepository usersRepository;

    @Transactional
    public void createPost(String email, PostDto postDto) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException("Not found user by email"));
        Posts post = Posts.of(postDto);
        post.setUser(user);
        postsRepository.save(post);
    }

    public Page<PostDto> getAllPostsbyUser(final String email, final String keyword , final Integer pageNumber, final Long total){
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PostDto> postDtoList =  postsRepository.findAllByKeywordAndEmail("keyword", email, pageable);
        long totalCount = Optional.ofNullable(total).orElseGet(()->postsRepository.countByKeywordAndEmail(keyword, email));
        return new PageImpl<>(postDtoList, pageable, totalCount);
    }

    public Page<PostDto> getAllPosts(final String keyword, final Integer pageNumber, final Long total){
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PostDto> postDtoList =  postsRepository.findAllByKeyword(keyword, pageable);
        long totalCount = Optional.ofNullable(total).orElseGet(()->postsRepository.countByKeyword(keyword));
        return new PageImpl<>(postDtoList, pageable, totalCount);
    }

    public PostDto getPostById(Long postId) {
        return postsRepository.findById(postId, PostDto.class)
                .orElseThrow(() -> new NotFoundResourceException("Post not found with id: " + postId));
    }

    @Transactional
    public void updatePost(Long postId, PostDto postDto) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new NotFoundResourceException("Post not found with id: " + postId));
        post.update(postDto);
    }

    @Transactional
    public void deletePost(Long postId) {
        postsRepository.deleteById(postId);
    }


}
