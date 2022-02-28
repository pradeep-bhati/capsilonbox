package com.demo.capsilonbox.storage.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.demo.capsilonbox.exceptionhandling.EntityNotFoundException;
import com.demo.capsilonbox.storage.dtos.Document;
import com.demo.capsilonbox.storage.entities.FileMetadataStore;
import com.demo.capsilonbox.storage.entities.FileStore;
import com.demo.capsilonbox.storage.repositories.FileMetadataStoreRepository;
import com.demo.capsilonbox.storage.repositories.FileStoreRepository;

@Service
public class StorageServiceDBImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(StorageServiceDBImpl.class);

	@Autowired
	private FileStoreRepository fileStoreRepository;

	@Autowired
	private FileMetadataStoreRepository fileMetadataStoreRepository;

	@Override
	@Transactional
	public String createDocument(Document document, byte[] content) {
		Assert.notNull(document, "Document cannot be null!");
		Assert.hasLength(document.getName(), "Document name cannot be empty!");
		Assert.hasLength(document.getContentType(), "Document content type cannot be empty!");
		Assert.notNull(content, "Content cannot be null!");
		Assert.isTrue(content.length > 0, "Content cannot be empty!");

		String id = UUID.randomUUID().toString();
		FileStore fileStore = new FileStore();
		fileStore.setId(id);

		try {
			fileStore.setContent(new SerialBlob(content));
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date creationTime = new Date();
		Date modificationTime = creationTime;

		FileMetadataStore fileMetadataStore = new FileMetadataStore();
		fileMetadataStore.setId(id);
		fileMetadataStore.setName(document.getName());
		fileMetadataStore.setContentType(document.getContentType());
		fileMetadataStore.setOwner(document.getOwner());
		fileMetadataStore.setSize(content.length);
		fileMetadataStore.setCreationTime(creationTime);
		fileMetadataStore.setModificationTime(modificationTime);
		fileMetadataStore.setFileStore(fileStore);

		fileStoreRepository.save(fileStore);
		fileMetadataStoreRepository.save(fileMetadataStore);
		return id;
	}

	@Override
	@Transactional
	public void deleteDocumentById(String id) {
		Assert.hasLength(id, "Document Id cannot be empty!");
		fileMetadataStoreRepository.deleteById(id);
		fileStoreRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Document getDocumentMetadataById(String id) {
		Assert.hasLength(id, "Document Id cannot be empty!");
		FileMetadataStore file = fileMetadataStoreRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Document not found."));
		Document doc = new Document();
		doc.setId(file.getId());
		doc.setName(file.getName());
		doc.setSize(file.getSize());
		doc.setContentType(file.getContentType());
		doc.setOwner(file.getOwner());
		doc.setCreationTime(file.getCreationTime());
		doc.setModificationTime(file.getModificationTime());
		return doc;
	}

	@Override
	@Transactional
	public byte[] getDocumentContentById(String id) {
		logger.info("Fetching document ID: " + id);
		Assert.hasLength(id, "Document Id cannot be empty!");

		FileStore fileStore = fileStoreRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Document not found."));
		byte[] content = null;
		try {
			content = fileStore.getContent().getBytes(1l, (int) fileStore.getContent().length());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	@Override
	@Transactional
	public List<Document> listAllDocuments() {
		List<Document> documents = fileMetadataStoreRepository.findAll().stream().map(file -> {
			Document d = new Document();
			d.setId(file.getId());
			d.setName(file.getName());
			d.setSize(file.getSize());
			d.setContentType(file.getContentType());
			d.setOwner(file.getOwner());
			d.setCreationTime(file.getCreationTime());
			d.setModificationTime(file.getModificationTime());
			return d;
		}).collect(Collectors.toList());
		return documents;
	}

	@Override
	@Transactional
	public List<Document> listAllDocumentsByOwner(String owner) {
		List<Document> documents = fileMetadataStoreRepository.findAllByOwner(owner).stream().map(file -> {
			Document d = new Document();
			d.setId(file.getId());
			d.setName(file.getName());
			d.setSize(file.getSize());
			d.setContentType(file.getContentType());
			d.setOwner(file.getOwner());
			d.setCreationTime(file.getCreationTime());
			d.setModificationTime(file.getModificationTime());
			return d;
		}).collect(Collectors.toList());
		return documents;
	}

}
