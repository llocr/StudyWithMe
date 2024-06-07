package december.spring.studywithme.service;

import december.spring.studywithme.dto.CommentRequestDto;
import december.spring.studywithme.dto.CommentResponseDto;
import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.exception.CommentException;
import december.spring.studywithme.exception.NoContentException;
import december.spring.studywithme.repository.CommentRepository;
import december.spring.studywithme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponseDto createComment(UserDetailsImpl userDetails, Long postId, CommentRequestDto requestDto) {
        Post post = postService.getValidatePost(postId);
        Comment comment = Comment.builder()
                .post(post)
                .user(userDetails.getUser())
                .contents(requestDto.getContents())
                .build();

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getAllComments(Long postId) {
        Post post = postService.getValidatePost(postId);
        List<Comment> commentList = post.getCommentList();

        if (commentList.isEmpty()) {
            throw new NoContentException("가장 먼저 댓글을 작성해보세요!");
        }

        return commentList.stream().map(CommentResponseDto::new).toList();
    }

    public CommentResponseDto getComment(Long postId, Long commentId) {
        Post post = postService.getValidatePost(postId);

        Comment comment = commentRepository.findByPostIdAndId(post.getId(), commentId).orElseThrow(() ->
                        new CommentException("게시글에 해당 댓글이 존재하지 않습니다."));

        return new CommentResponseDto(comment);
    }
}
