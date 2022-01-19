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
import com.keyword.keywordspring.repository.UserRepository;
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
    private final UserRepository userRepository;

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

        user.setNrOfPosts(user.getNrOfPosts() + 1);
        userRepository.save(user);

        return saved.getId();
    }

    @Override
    public List<PostDto> getPosts(Integer page, String name, String groupId, AppUser user) {

        ForumGroup group = groupId==null ? null : groupRepository.findById(groupId).orElse(null);
        List<Post> posts;

        if(name!=null && group!=null)
            posts = postRepository.findByTitleLikeAndForumGroupLike("%"+name+"%", group, PageRequest.of(page, 10, Sort.by("votes").descending()));
        else if(name==null && group!=null)
            posts = postRepository.findByForumGroupLike(group, PageRequest.of(page, 10, Sort.by("votes").descending()));
        else if(name!=null)
            posts = postRepository.findByTitleLike("%"+name+"%", PageRequest.of(page, 10, Sort.by("votes").descending()));
        else
            posts = postRepository.findAll(PageRequest.of(page, 10, Sort.by("votes").descending()));

        return posts.stream()
                .map(p -> postMapper.mapToDto(p, user))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getPostsCount(String groupId, String name) {

        ForumGroup group = groupId==null ? null : groupRepository.findById(groupId).orElse(null);

        if(name!=null && group!=null)
            return postRepository.findByTitleLikeAndForumGroupLike("%"+name+"%", group).size();
        else if(name==null && group!=null)
            return postRepository.findByForumGroupLike(group).size();
        else if(name!=null)
            return postRepository.findByTitleLike("%"+name+"%").size();
        else
            return postRepository.findAll().size();
    }

    @Override
    public PostDto getPost(Long id, AppUser user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        return postMapper.mapToDto(post, user);
    }

    @Override
    public Long editPost(AppUser user, EditPostRequest request) {

        if(postRepository.findById(request.getPostId()).isEmpty() ||
                !Objects.equals(user.getId(), postRepository.findById(request.getPostId()).get().getUser().getId()))
            throw new UnauthorizedException();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getPostId()));

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setEdited(true);

        postRepository.save(post);

        return post.getId();
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

        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        if(!post.getUser().equals(user) && !post.getForumGroup().getModerators().contains(user))
            throw new UnauthorizedException();

        AppUser postOwner = post.getUser();
        postOwner.setNrOfPosts(postOwner.getNrOfPosts() - 1);


        userRepository.save(postOwner);
        postRepository.deleteById(id);
    }
}
