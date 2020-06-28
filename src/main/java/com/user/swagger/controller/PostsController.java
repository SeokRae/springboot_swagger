package com.user.swagger.controller;

import com.user.swagger.config.response.SingleResult;
import com.user.swagger.domain.posts.Posts;
import com.user.swagger.domain.posts.dto.PostsListResponseDto;
import com.user.swagger.domain.posts.dto.PostsResponseDto;
import com.user.swagger.domain.posts.dto.PostsSaveRequestDto;
import com.user.swagger.domain.posts.dto.PostsUpdateRequestDto;
import com.user.swagger.service.posts.PostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 해당 클래스가 Swagger 리소스라는 것을 명시
@Api(value = "PostsController v1", tags = {"2. Posts"}, description = "포스트 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/posts")
public class PostsController {

    private final PostsService postsService;

    @ApiOperation(value = "모든 게시물 조회", notes = "모든 게시물을 조회한다")
    @GetMapping(value = "")
    public List<PostsListResponseDto> findAllPosts() {
        return postsService.findAllDesc();
    }

    @ApiOperation(value = "게시물 조회",notes = "PostsId로 게시물을 조회한다.")
    @GetMapping(value = "{postId}")
    public SingleResult<PostsResponseDto> findPostsById(
            @ApiParam(value = "게시물번호", required = true, example = "1") @PathVariable Long postId) {
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return postsService.findById(postId);
    }

    @ApiOperation(value = "게시물 등록", notes = "게시물을 등록한다.")
    @PostMapping(value = "")
    public SingleResult<Posts> save(
            @ApiParam(value = "게시물타이틀", required = true, example = "제목") @RequestParam String title,
            @ApiParam(value = "게시물내용", required = true, example = "내용") @RequestParam String content,
            @ApiParam(value = "작성자", required = true, example = "작성자") @RequestParam String author) {
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                    .title(title)
                    .content(content)
                    .author(author)
                    .build();
        return postsService.save(requestDto);
    }

    @ApiOperation(value = "게시물 수정", notes = "게시물 정보를 수정한다")
    @PutMapping(value = "")
    public SingleResult<Posts> update(
            @ApiParam(value = "게시물번호", required = true, example = "1") @RequestParam Long postId,
            @ApiParam(value = "게시물타이틀", required = true, example = "제목") @RequestParam String title,
            @ApiParam(value = "게시물내용", required = true, example = "내용") @RequestParam String content,
            @ApiParam(value = "작성자", required = true, example = "작성자") @RequestParam String author) {

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        return postsService.update(postId, requestDto);
    }

    @ApiOperation(value = "게시물 삭제", notes = "게시물 삭제한다")
    @DeleteMapping(value = "{postId}")
    public SingleResult<Long> delete(
            @ApiParam(value = "게시물번호", required = true, example = "1") @PathVariable Long postId) {
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return postsService.delete(postId);
    }
}
