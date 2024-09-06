package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import com.github.luisfeliperochamartins.aluraflix.domain.categorias.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

	private final CategoriaRepository categoriaRepository;
	private final VideoRepository videoRepository;

	@Autowired
	public VideoService(CategoriaRepository categoriaRepository, VideoRepository videoRepository) {
		this.categoriaRepository = categoriaRepository;
		this.videoRepository = videoRepository;
	}

	public Video update(VideoUpdateRecord record) {
		var video = new Video();
		if (record.titulo() != null) {
			video.setTitulo(record.titulo());
		}

		if (record.descricao() != null) {
			video.setDescricao(record.descricao());
		}

		if (record.url() != null) {
			video.setUrl(record.url());
		}

		if (record.categoriaId() != null) {
			var categoria = categoriaRepository.findById(record.categoriaId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));
			video.setCategoria(categoria);
		}

		return video;
	}
}
