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
    public CommentDto addComment(AppUser user, CreateCommentRequest request) {

        Optional<Post> post = postRepository.findById(request.getPostId());
        Optional<Comment> parentComment = request.getParentCommentId() != null ?
                commentRepository.findById(request.getParentCommentId()) :
                Optional.empty();

        if(post.isEmpty())
            throw new PostDoesNotExistException(request.getPostId());

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post.get())
                .parentComment(parentComment.orElse(null))
                .dateCreated(new Date(System.currentTimeMillis()))
                .votes(0)
                .edited(false)
                .build();

        commentRepository.save(comment);

        return commentMapper.mapToDto(comment, user);
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
    public void vote(AppUser user, Long commentId, VoteType voteType){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException(commentId));

        Optional<CommentVote> vote = commentVoteRepository.findByUserAndComment(user, comment);

        if(vote.isEmpty()){
            commentVoteRepository.save(CommentVote.builder()
                    .user(user)
                    .comment(comment)
                    .type(voteType)
                    .build());

            comment.setVotes(comment.getVotes() + voteType.getValue());
        } else if (vote.get().getType() == voteType) {
            commentVoteRepository.delete(vote.get());
            comment.setVotes(comment.getVotes() - voteType.getValue());
        } else {
            vote.get().setType(voteType);
            commentVoteRepository.save(vote.get());
            comment.setVotes(comment.getVotes() + (2 * voteType.getValue()));
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
