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
    public Long createPost(AppUser user, CreatePostRequest request) {

        Optional<ForumGroup> group = groupRepository.findById(request.getGroupId());

        if(group.isEmpty())
            throw new GroupDoesNotExistException(request.getGroupId());

        Post saved = postRepository.save(Post.builder()
                        .user(user)
                        .forumGroup(group.get())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .dateCreated(new Date(System.currentTimeMillis()))
                        .edited(false)
                        .votes(0)
                .build());

        return saved.getId();
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

        if(postRepository.findById(request.getPostId()).isEmpty() ||
                !Objects.equals(user.getId(), postRepository.findById(request.getPostId()).get().getUser().getId()))
            throw new UnauthorizedException();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getPostId()));

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setEdited(true);

        postRepository.save(post);
    }

    @Override
    public void vote(AppUser user, Long postId, VoteType voteType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostDoesNotExistException(postId));

        Optional<PostVote> vote = postVoteRepository.findByUserAndPost(user, post);

        if(vote.isEmpty()){
            postVoteRepository.save(PostVote.builder()
                    .user(user)
                    .post(post)
                    .type(voteType)
                    .build());

            post.setVotes(post.getVotes() + voteType.getValue());
        } else if (vote.get().getType() == voteType) {
            postVoteRepository.delete(vote.get());
            post.setVotes(post.getVotes() - voteType.getValue());
        } else {
            vote.get().setType(voteType);
            postVoteRepository.save(vote.get());
            post.setVotes(post.getVotes() + (2 * voteType.getValue()));
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
