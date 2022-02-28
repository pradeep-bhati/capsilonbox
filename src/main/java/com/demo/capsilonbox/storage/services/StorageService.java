package com.demo.capsilonbox.storage.services;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demo.capsilonbox.storage.dtos.Document;

public interface StorageService {

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public String createDocument(Document document, byte[] content);

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteDocumentById(String id);

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public Document getDocumentMetadataById(String id);

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public byte[] getDocumentContentById(String id);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public List<Document> listAllDocuments();

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Document> listAllDocumentsByOwner(String owner);

}
