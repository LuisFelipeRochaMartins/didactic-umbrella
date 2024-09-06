package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VideoUpdateRecord(@NotNull Integer id,
								@NotBlank String titulo,
                                @NotBlank String descricao,

                                @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(/.*)?$")
                                String url) {

	public VideoUpdateRecord(Video video) {
		this(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl());
	}
}
