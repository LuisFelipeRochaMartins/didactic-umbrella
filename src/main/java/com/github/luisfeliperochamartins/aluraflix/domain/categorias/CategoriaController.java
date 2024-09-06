package com.github.luisfeliperochamartins.aluraflix.domain.categorias;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/categorias")
public class CategoriaController {

	private final CategoriaRepository repository;

	@Autowired
	public CategoriaController(CategoriaRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<Page<Categoria>> getAll(@PageableDefault Pageable page) {
		var list = repository.findAll(page);

		return ResponseEntity.ok(list);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Integer id) {
		var categoria = repository.findById(id).orElseThrow(() -> new RuntimeException("Vídeo não encontrada!"));

		return ResponseEntity.ok(categoria);
	}

	@PostMapping
	public ResponseEntity<Categoria> insert(@RequestBody Categoria body, UriComponentsBuilder uriBuilder) {
		var categoria = repository.save(body);

		var uri = uriBuilder.path("/categorias/{id}").buildAndExpand(categoria.getId()).toUri();

		return ResponseEntity.created(uri).body(categoria);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<Categoria> update(@RequestBody Categoria body) {
		var exists = repository.existsById(body.getId());

		if (!exists) {
			throw new RuntimeException("Vídeo não encontrado!");
		}

		var categoria = repository.findById(body.getId())
								.orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));
		categoria.update(body);

		return ResponseEntity.ok(categoria);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		if (id == 1) {
			return ResponseEntity.notFound().build();
		}

		var exists = repository.existsById(id);
		if (!exists) {
			throw new RuntimeException("Vídeo não encontrado!");
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
