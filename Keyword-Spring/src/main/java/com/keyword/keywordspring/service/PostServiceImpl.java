package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.PostVoteRepository;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final PostMapper postMapper;
    private final PostVoteRepository postVoteRepository;

    @Override
    public void createPost(AppUser user, CreatePostRequest request) {

        Optional<ForumGroup> group = groupRepository.findById(request.getGroupId());

        if(group.isEmpty())
            throw new GroupDoesNotExistException(request.getGroupId());

        postRepository.save(Post.builder()
                        .user(user)
                        .forumGroup(group.get())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .dateCreated(new Date(System.currentTimeMillis()))
                        .edited(false)
                        .votes(0)
                .build());
    }

    @Override
    public List<PostDto> getPosts(Integer page, String name, AppUser user) {

        return (name == null ?
                postRepository.findAll(PageRequest.of(page, 10, Sort.by("votes").descending())) :
                postRepository.findByTitleLike("%"+name+"%", PageRequest.of(page, 10, Sort.by("votes").descending())))
                .stream()
                .map(p -> postMapper.mapToDto(p, user))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPost(Long id, AppUser user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        return postMapper.mapToDto(post, user);
    }

    @Override
    public void editPost(AppUser user, EditPostRequest request) {

        if(postRepository.findById(request.getId()).isEmpty() ||
                !Objects.equals(user.getId(), postRepository.findById(request.getId()).get().getUser().getId()))
            throw new UnauthorizedException();

        Post post = postRepository.findById(request.getId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getId()));

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setEdited(true);

        postRepository.save(post);
    }

    @Override
    public void upvote(AppUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostDoesNotExistException(postId));

        Optional<PostVote> vote = postVoteRepository.findByUserAndPost(user, post);

        if(vote.isEmpty()){
            postVoteRepository.save(PostVote.builder()
                    .user(user)
                    .post(post)
                    .type(VoteType.UP)
                    .build());

            post.setVotes(post.getVotes() + 1);
        } else if (vote.get().getType() == VoteType.UP) {
            postVoteRepository.delete(vote.get());
            post.setVotes(post.getVotes() - 1);
        } else {
            vote.get().setType(VoteType.UP);
            postVoteRepository.save(vote.get());
            post.setVotes(post.getVotes() + 2);
        }

        postRepository.save(post);
    }

    @Override
    public void downvote(AppUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostDoesNotExistException(postId));

        Optional<PostVote> vote = postVoteRepository.findByUserAndPost(user, post);

        if(vote.isEmpty()){
            postVoteRepository.save(PostVote.builder()
                    .user(user)
                    .post(post)
                    .type(VoteType.DOWN)
                    .build());

            post.setVotes(post.getVotes() - 1);
        } else if (vote.get().getType() == VoteType.DOWN) {
            postVoteRepository.delete(vote.get());
            post.setVotes(post.getVotes() + 1);
        } else {
            vote.get().setType(VoteType.DOWN);
            postVoteRepository.save(vote.get());
            post.setVotes(post.getVotes() - 2);
        }

        postRepository.save(post);
    }

    @Override
    public void deletePost(AppUser user, Long id) {

        if(postRepository.findById(id).isEmpty() ||
                !Objects.equals(user.getId(), postRepository.findById(id).get().getUser().getId()))
            throw new UnauthorizedException();

        postRepository.deleteById(id);
    }
}
