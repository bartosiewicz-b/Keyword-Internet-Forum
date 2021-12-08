package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
                .build());
    }

    @Override
    public List<PostDto> getPosts(Integer page, String name) {

        return (name == null ?
                postRepository.findAll(PageRequest.of(page, 10)) :
                postRepository.findByTitleLike(name, PageRequest.of(page, 10)))
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostDoesNotExistException(id));

        return postMapper.mapToDto(post);
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
    public void deletePost(AppUser user, Long id) {

        if(postRepository.findById(id).isEmpty() ||
                !Objects.equals(user.getId(), postRepository.findById(id).get().getUser().getId()))
            throw new UnauthorizedException();

        postRepository.deleteById(id);
    }
}
