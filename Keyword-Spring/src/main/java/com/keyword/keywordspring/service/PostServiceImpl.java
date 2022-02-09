package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.AddPostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.exception.ActionTooQuickException;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.*;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final CommentService commentService;
    private final PostMapper postMapper;
    private final PostVoteRepository postVoteRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final int PAGE_SIZE = 10;

    @Override
    @Transactional
    public PostDto add(String token, AddPostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        if(Objects.nonNull(user.getLastPostCreated()) && user.getLastPostCreated().compareTo(new Date(System.currentTimeMillis() - 600000)) > 0)
            throw new ActionTooQuickException("create post", 10);

        ForumGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupDoesNotExistException(request.getGroupId()));

        Post post = postRepository.save(Post.builder()
                .user(user)
                .forumGroup(group)
                .title(request.getTitle())
                .description(request.getDescription())
                .comments(new ArrayList<>())
                .dateCreated(new Date(System.currentTimeMillis()))
                .edited(false)
                .votes(0)
                .build());

        user.setNrOfPosts(user.getNrOfPosts() + 1);
        user.setLastPostCreated(new Date(System.currentTimeMillis()));
        userRepository.save(user);

        return postMapper.mapToDto(post, user);
    }

    @Override
    @Transactional
    public List<PostDto> getAll(String token, String groupId, int page, String keyword) {

        AppUser user = jwtUtil.getUserFromToken(token).orElse(null);

        ForumGroup group = groupId == null ?
                null :
                groupRepository.findById(groupId).orElse(null);

        List<Post> posts;

        if(keyword!=null && group!=null)
            posts = postRepository.findByTitleLikeAndForumGroupLike("%"+keyword+"%", group, PageRequest.of(page, PAGE_SIZE, Sort.by("votes").descending()));
        else if(keyword==null && group!=null)
            posts = postRepository.findByForumGroupLike(group, PageRequest.of(page, PAGE_SIZE, Sort.by("votes").descending()));
        else if(keyword!=null)
            posts = postRepository.findByTitleLike("%"+keyword+"%", PageRequest.of(page, PAGE_SIZE, Sort.by("votes").descending()));
        else
            posts = postRepository.findAll(PageRequest.of(page, PAGE_SIZE, Sort.by("votes").descending()));

        return posts.stream()
                .map(p -> postMapper.mapToDto(p, user))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int getCount(String keyword, String groupId) {

        ForumGroup group = groupId == null ?
                null :
                groupRepository.findById(groupId).orElse(null);

        if(keyword!=null && group!=null)
            return postRepository.findByTitleLikeAndForumGroupLike("%"+keyword+"%", group).size();
        else if(keyword==null && group!=null)
            return postRepository.findByForumGroupLike(group).size();
        else if(keyword!=null)
            return postRepository.findByTitleLike("%"+keyword+"%").size();
        else
            return postRepository.findAll().size();
    }

    @Override
    @Transactional
    public PostDto get(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElse(null);

        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        return postMapper.mapToDto(post, user);
    }

    @Override
    @Transactional
    public PostDto edit(String token, EditPostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostDoesNotExistException(request.getPostId()));

        if(!user.equals(post.getUser()))
            throw new UnauthorizedException(user, post);

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setEdited(true);

        postRepository.save(post);

        return postMapper.mapToDto(post, user);
    }

    @Override
    @Transactional
    public void delete(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        if(!post.getUser().equals(user) && !post.getForumGroup().getModerators().contains(user))
            throw new UnauthorizedException(user, post);

        AppUser postOwner = post.getUser();
        postOwner.setNrOfPosts(postOwner.getNrOfPosts() - 1);

        userRepository.save(postOwner);

        postVoteRepository.deleteAllByPost(post);

        post.getComments().forEach(c -> {
            commentService.delete(token, c.getId());
        });

        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int upvote(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostDoesNotExistException(id));

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

        return post.getVotes();
    }

    @Override
    @Transactional
    public int downvote(String token, Long id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostDoesNotExistException(id));

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

        return post.getVotes();
    }
}
