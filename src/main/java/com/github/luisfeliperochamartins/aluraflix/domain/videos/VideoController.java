package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/videos")
public class VideoController {

	private final VideoRepository repository;

	@Autowired
	public VideoController(VideoRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<Page<VideoRecord>> getAll(@PageableDefault Pageable page) {
		var list = repository.findAll(page).map(VideoRecord::new);

		return ResponseEntity.ok(list);
	}

	@GetMapping(path = "/videos/{id}")
	public ResponseEntity<VideoRecord> getById(@PathVariable Integer id) {
		var video = repository.findById(id).orElseThrow(() -> new RuntimeException("Video não encontrado!"));

		return ResponseEntity.ok(new VideoRecord(video));
	}

	@PostMapping
	public ResponseEntity<VideoRecord> insert(@RequestBody VideoRecord record, UriComponentsBuilder uriBuilder) {
		var video = repository.save(new Video(record));

		var uri = uriBuilder.path("/videos/{id}").buildAndExpand(video.getId()).toUri();

		return ResponseEntity.created(uri).body(new VideoRecord(video));
	}

	@PutMapping
	@Transactional
	public ResponseEntity<VideoRecord> update(@RequestBody VideoUpdateRecord record) {
		var exists = repository.existsById(record.id());

		if(!exists) {
			throw new RuntimeException("VIdeo não encontrado!");
		}
		var video = repository.getReferenceById(record.id());
		video.update(record);

		return ResponseEntity.ok(new VideoRecord(video));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		var video = repository.findById(id).orElseThrow(() -> new RuntimeException("Video não encontrado!"));

		repository.delete(video);
		return ResponseEntity.noContent().build();
	}
}
