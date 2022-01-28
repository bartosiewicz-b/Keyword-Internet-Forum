package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.AddCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto add(String token, AddCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostDoesNotExistException(request.getPostId()));

        Comment parentComment = null;
        if(request.getParentCommentId() != null)
            parentComment = commentRepository.findById(request.getParentCommentId()).orElse(null);

        Comment comment = commentRepository.save(Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .dateCreated(new Date(System.currentTimeMillis()))
                .votes(0)
                .edited(false)
                .build());

        user.setNrOfComments(user.getNrOfComments() + 1);
        userRepository.save(user);

        return commentMapper.mapToDto(comment, user);
    }

    @Override
    public List<CommentDto> getAll(String token, Long postId) {

        AppUser user = jwtUtil.getUserFromToken(token).orElse(null);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostDoesNotExistException(postId));

        return commentRepository.findAllByPostOrderByVotesDesc(post)
                .stream()
                .map(c -> commentMapper.mapToDto(c, user))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto edit(String token, EditCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Comment comment = commentRepository.findById(request.getId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getId()));

        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException(user, comment);

        comment.setContent(request.getNewContent());
        comment.setEdited(true);

        commentRepository.save(comment);

        return commentMapper.mapToDto(comment, user);
    }

    @Override
    public void delete(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentDoesNotExistException(id));

        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException(user, comment);

        user.setNrOfComments(user.getNrOfComments() - 1);
        userRepository.save(user);

        commentRepository.deleteById(id);
    }

    @Override
    public int upvote(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentDoesNotExistException(id));

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

        return comment.getVotes();
    }

    @Override
    public int downvote(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentDoesNotExistException(id));

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

        return comment.getVotes();
    }
}
