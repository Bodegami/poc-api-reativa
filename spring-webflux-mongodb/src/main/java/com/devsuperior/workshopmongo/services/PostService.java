package com.devsuperior.workshopmongo.services;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.workshopmongo.dto.PostDTO;
import com.devsuperior.workshopmongo.repositories.PostRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	@Transactional(readOnly = true)
	public Mono<PostDTO> findById(String id) {

		return repository.findById(id)
				.map(PostDTO::new)
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado")));
	}

	public Flux<PostDTO> findByTitle(String text) {
		return repository.searchTitle(text).map(PostDTO::new);
	}

	public Flux<PostDTO> fullSearch(String text, Instant minDate, Instant maxDate) {
		maxDate = maxDate.plusSeconds(86400); // 24 * 60 * 60
		return repository.fullSearch(text, minDate, maxDate)
				.map(PostDTO::new);
	}

	public Flux<PostDTO> findByUser(String id) {
		return repository.findByUser(new ObjectId(id))
				.map(PostDTO::new);
	}

}
