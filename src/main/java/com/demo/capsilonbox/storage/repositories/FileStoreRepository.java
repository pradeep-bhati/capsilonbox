package com.demo.capsilonbox.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.capsilonbox.storage.entities.FileStore;

public interface FileStoreRepository extends JpaRepository<FileStore, String> {
	

}
