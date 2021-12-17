package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.service.interf.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final CommentVoteRepository commentVoteRepository;

    @Override
    public void addComment(AppUser user, CreateCommentRequest request) {

        Optional<Post> post = postRepository.findById(request.getPostId());
        Optional<Comment> comment = request.getParentCommentId() != null ?
                commentRepository.findById(request.getParentCommentId()) :
                Optional.empty();

        if(post.isEmpty())
            throw new PostDoesNotExistException(request.getPostId());

        commentRepository.save(Comment.builder()
                        .content(request.getContent())
                        .user(user)
                        .post(post.get())
                        .parentComment(comment.orElse(null))
                        .dateCreated(new Date(System.currentTimeMillis()))
                        .votes(0)
                        .edited(false)
                .build());

    }

    @Override
    public List<CommentDto> getComments(Long postId, AppUser user) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostDoesNotExistException(postId));

        return commentRepository.findAllByPostOrderByVotesDesc(post)
                .stream()
                .map(c -> commentMapper.mapToDto(c, user))
                .collect(Collectors.toList());
    }

    @Override
    public void editComment(AppUser user, EditCommentRequest request) {

        if(commentRepository.findById(request.getId()).isEmpty() ||
                !Objects.equals(user.getId(), commentRepository.findById(request.getId()).get().getUser().getId()))
            throw new UnauthorizedException();

        Comment comment = commentRepository.findById(request.getId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getId()));

        comment.setContent(request.getNewContent());
        comment.setEdited(true);

        commentRepository.save(comment);
    }

    @Override
    public void upvote(AppUser user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException(commentId));

        Optional<CommentVote> vote = commentVoteRepository.findByUserAndComment(user, comment);

        if(vote.isEmpty()){
            commentVoteRepository.save(CommentVote.builder()
                    .user(user)
                    .comment(comment)
                    .type(VoteType.UP)
                    .build());

            comment.setVotes(comment.getVotes() + 1);
        } else if (vote.get().getType() == VoteType.UP) {
            commentVoteRepository.delete(vote.get());
            comment.setVotes(comment.getVotes() - 1);
        } else {
            vote.get().setType(VoteType.UP);
            commentVoteRepository.save(vote.get());
            comment.setVotes(comment.getVotes() + 2);
        }

        commentRepository.save(comment);
    }

    @Override
    public void downvote(AppUser user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException(commentId));

        Optional<CommentVote> vote = commentVoteRepository.findByUserAndComment(user, comment);

        if(vote.isEmpty()){
            commentVoteRepository.save(CommentVote.builder()
                    .user(user)
                    .comment(comment)
                    .type(VoteType.DOWN)
                    .build());

            comment.setVotes(comment.getVotes() - 1);
        } else if (vote.get().getType() == VoteType.DOWN) {
            commentVoteRepository.delete(vote.get());
            comment.setVotes(comment.getVotes() + 1);
        } else {
            vote.get().setType(VoteType.DOWN);
            commentVoteRepository.save(vote.get());
            comment.setVotes(comment.getVotes() - 2);
        }

        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(AppUser user, Long id) {

        if(commentRepository.findById(id).isEmpty() ||
                !Objects.equals(user.getId(), commentRepository.findById(id).get().getUser().getId()))
            throw new UnauthorizedException();

        commentRepository.deleteById(id);
    }
}
