package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.BadRequestException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.request.ContentRequest;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.likes.Like;
import com.aradata.plaidapp.repository.ContentRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.comment.CommentService;
import com.aradata.plaidapp.service.likes.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
public class ContentService {

	@Autowired
	private ContentRepository repository;

	@Autowired
	private CommentService commentService;

	@Autowired
	private LikeService likeService;


	public PagedResponse<Content> fetchAllContent(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Content> contents = repository.findAll(pageable);

		if(contents.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), contents.getNumber(),
					contents.getSize(), contents.getTotalElements(), contents.getTotalPages());
		}

		List<Content> responseList = contents.getContent();

		return new PagedResponse<>(responseList, contents.getNumber(), contents.getSize(),
				contents.getTotalElements(), contents.getTotalPages());
	}

	private void validatePageNumberAndSize(int page, int size) {
		if(page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if(size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	private Content validateContentId(String contentId) {
		return repository.findById(contentId).orElseThrow(() ->
				new ResourceNotFoundException("Content", "id", contentId));
	}

	public Content createContent(@Valid ContentRequest contentRequest) {
		Content content = new Content();
		content.setText(contentRequest.getText());
		content.setTitle(contentRequest.getTitle());
		content.setDescription(contentRequest.getDescription());
		content.setType(contentRequest.getType());
		content.setUrl(contentRequest.getUrl());
		repository.save(content);
		return content;
	}

	public Content getContentById(String contentId) {
		Content content = validateContentId(contentId);

		return content;
	}

	public PagedResponse<Comment> fetchComments(UserPrincipal currentUser, String contentId, int page, int size) {
		validatePageNumberAndSize(page, size);
		validateContentId(contentId);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<Comment> comments = commentService.findAllByContentId(pageable, contentId);

		if(comments.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), comments.getNumber(),
					comments.getSize(), comments.getTotalElements(), comments.getTotalPages());
		}

		List<Comment> responseList = comments.getContent();

		return new PagedResponse<>(responseList, comments.getNumber(), comments.getSize(),
				comments.getTotalElements(), comments.getTotalPages());

	}

	public Comment createComment(UserPrincipal currentUser, CommentRequest request, String contentId) {
		validateContentId(contentId);
		return commentService.createComment(currentUser, request, contentId);
	}

	public Like createLike(String contentId, UserPrincipal currentUser) {
		validateContentId(contentId);
		Like like = likeService.createLike(contentId, currentUser);
		Content content = repository.findById(contentId).get();
		content.setLikes(content.getLikes() + 1);
		repository.save(content);
		return like;
	}

	public void deleteLike(String contentId, UserPrincipal currentUser) {
		validateContentId(contentId);
		likeService.deleteLike(contentId, currentUser);
		Content content = repository.findById(contentId).get();
		content.setLikes(content.getLikes() - 1);
		repository.save(content);
	}
}
