package dev.charles.SimpleService.comments.repository;

import dev.charles.SimpleService.comments.dto.CommentsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedCommentsRepository {
    Page<CommentsResponseDto> findAllParentsByPostId(Long postId, Pageable pageable);
    Page<CommentsResponseDto> findAllChildrenByParentId(Long parentId, Pageable pageable);
}
