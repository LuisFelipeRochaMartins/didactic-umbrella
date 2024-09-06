package com.github.luisfeliperochamartins.aluraflix.domain.categorias;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;

	public Categoria() {}

	public Categoria(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void update(Categoria body) {
		if (body.getNome() != null) {
			this.nome = body.getNome();
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("id = ").append(id);
		sb.append(", nome = ").append(nome);
		return sb.toString();
	}
}
