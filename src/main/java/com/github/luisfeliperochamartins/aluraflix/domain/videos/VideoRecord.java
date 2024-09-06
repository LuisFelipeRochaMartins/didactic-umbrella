package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VideoRecord(@NotBlank String titulo,
                          @NotBlank String descricao,

                          @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(/.*)?$")
                          String url) {

	public VideoRecord(Video video) {
		this(video.getTitulo(), video.getDescricao(), video.getUrl());
	}
}
