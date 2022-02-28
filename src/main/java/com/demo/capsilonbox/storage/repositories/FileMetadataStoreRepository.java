package com.demo.capsilonbox.storage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.capsilonbox.storage.entities.FileMetadataStore;

public interface FileMetadataStoreRepository extends JpaRepository<FileMetadataStore, String> {

	List<FileMetadataStore> findAllByOwner(String owner);

}
