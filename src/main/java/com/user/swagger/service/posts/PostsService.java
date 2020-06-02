package com.user.swagger.service.posts;

import com.user.swagger.advice.exception.CPostsNotFoundException;
import com.user.swagger.config.response.SingleResult;
import com.user.swagger.domain.posts.Posts;
import com.user.swagger.domain.posts.PostsRepository;
import com.user.swagger.domain.posts.dto.PostsListResponseDto;
import com.user.swagger.domain.posts.dto.PostsResponseDto;
import com.user.swagger.domain.posts.dto.PostsSaveRequestDto;
import com.user.swagger.domain.posts.dto.PostsUpdateRequestDto;
import com.user.swagger.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final ResponseService responseService;
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAll().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public SingleResult<Posts> save(PostsSaveRequestDto requestDto) {
        Posts posts = requestDto.toEntity();
        return responseService.getSingleResult(postsRepository.save(posts));
    }

    @Transactional
    public SingleResult<Posts> update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(CPostsNotFoundException::new);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return responseService.getSingleResult(posts);
    }

    public SingleResult<PostsResponseDto> findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(CPostsNotFoundException::new);
        return responseService.getSingleResult(new PostsResponseDto(entity));
    }

    @Transactional
    public SingleResult<Long> delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(CPostsNotFoundException::new);
        postsRepository.deleteById(posts.getId());
        return responseService.getSingleResult(id);
    }
}

