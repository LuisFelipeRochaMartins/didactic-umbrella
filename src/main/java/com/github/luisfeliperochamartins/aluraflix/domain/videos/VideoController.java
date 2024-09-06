package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import com.github.luisfeliperochamartins.aluraflix.domain.categorias.CategoriaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

	private final VideoRepository videoRepository;
	private final CategoriaRepository categoriaRepository;
	private final VideoService service;

	@Autowired
	public VideoController(VideoRepository repository, CategoriaRepository categoriaRepository, VideoService service) {
		this.videoRepository = repository;
		this.categoriaRepository = categoriaRepository;
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<VideoRecord>> getAll(@PageableDefault Pageable page) {
		var list = videoRepository.findAll(page).map(VideoRecord::new);

		return ResponseEntity.ok(list);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<VideoRecord> getById(@PathVariable Integer id) {
		var video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video não encontrado!"));

		return ResponseEntity.ok(new VideoRecord(video));
	}

	@PostMapping
	@Transactional
	public ResponseEntity<VideoRecord> insert(@RequestBody @Valid VideoRecord record, UriComponentsBuilder uriBuilder) {
		var categoria = categoriaRepository.findById(record.categoriaId());
		var video = new Video(record);

		if (categoria.isPresent()) {
			video.setCategoria(categoria.get());
		}

		videoRepository.save(video);
		var uri = uriBuilder.path("/videos/{id}").buildAndExpand(video.getId()).toUri();

		return ResponseEntity.created(uri).body(new VideoRecord(video));
	}

	@PutMapping
	@Transactional
	public ResponseEntity<VideoRecord> update(@RequestBody @Valid VideoUpdateRecord record) {
		var exists = videoRepository.existsById(record.id());

		if(!exists) {
			throw new RuntimeException("Vídeo não encontrado!");
		}
		var video = videoRepository.getReferenceById(record.id());
		video = service.update(record);
		return ResponseEntity.ok(new VideoRecord(video));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		var video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video não encontrado!"));

		videoRepository.delete(video);
		return ResponseEntity.noContent().build();
	}
}
