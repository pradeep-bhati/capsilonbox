package com.demo.capsilonbox.storage.entities;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "file_store")
public class FileStore {

	@Size(max = 36)
	@NotNull
	@Column(name = "id", length = 36)
	@Id
	private String id;

	@NotNull
	@Lob
	private Blob content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

}
