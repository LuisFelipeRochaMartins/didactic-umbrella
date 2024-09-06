package com.github.luisfeliperochamartins.aluraflix.domain.videos;

import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String titulo;
	private String descricao;
	private String url;

	public Video() {}

	public Video(Integer id, String titulo, String descricao, String url) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.url = url;
	}

	public Video(VideoRecord record) {
		this.descricao = record.descricao();
		this.titulo = record.titulo();
		this.url = record.url();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void update(VideoUpdateRecord record) {
		if (record.titulo() != null) {
			this.titulo = record.titulo();
		}

		if (record.descricao() != null) {
			this.descricao = record.descricao();
		}

		if (record.url() != null) {
			this.url = record.url();
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("id = ").append(id);
		sb.append(", titulo = ").append(titulo);
		sb.append(", descricao = ").append(descricao);
		sb.append(", url = ").append(url);
		return sb.toString();
	}
}
