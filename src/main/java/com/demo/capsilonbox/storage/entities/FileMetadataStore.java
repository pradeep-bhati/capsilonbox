package com.demo.capsilonbox.storage.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "file_metadata_store")
public class FileMetadataStore {

	@Size(max = 36)
	@NotNull
	@Column(name = "id", length = 36)
	@Id
	private String id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private FileStore fileStore;

	@Size(max = 255)
	@NotNull
	private String name;

	@Min(1)
	private long size;

	@Size(max = 255)
	@NotNull
	private String contentType;

	@Size(max = 255)
	@NotNull
	private String owner;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FileStore getFileStore() {
		return fileStore;
	}

	public void setFileStore(FileStore fileStore) {
		this.fileStore = fileStore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

}
