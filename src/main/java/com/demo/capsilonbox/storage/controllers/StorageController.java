package com.demo.capsilonbox.storage.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.demo.capsilonbox.storage.dtos.Document;
import com.demo.capsilonbox.storage.services.StorageService;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class StorageController {

	@Autowired
	private StorageService storageService;

	private static final Logger logger = LoggerFactory.getLogger(StorageController.class);

	@RequestMapping(value = "/document", method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	public void addDocument(@RequestParam("file") @NotNull MultipartFile file, Principal principal) throws IOException {

		Assert.isTrue(!file.isEmpty(), "Invalid file, file doesn't have any content!");

		// check file extension jpeg or pdf
		String contentType = file.getContentType();
		if ("application/pdf".equalsIgnoreCase(contentType) == false
				&& "image/jpeg".equalsIgnoreCase(contentType) == false) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
					"Only pdf and jpg file types are allowed.");
		}

		byte[] content = file.getBytes();
		if (content.length > 100 * 1024 * 1024) { // 100 MB max file size
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Max file size allowed is 100 MB.");
		}

		String fileName = "Document";
		fileName = file.getOriginalFilename();

		Document document = new Document();
		document.setName(fileName);
		document.setContentType(contentType);
		document.setOwner(principal.getName());
		storageService.createDocument(document, content);
	}

	@RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
	public void getDocument(@PathVariable("id") @NotBlank String id, HttpServletRequest request,
			HttpServletResponse response, Principal principal) throws SQLException, IOException {

		Assert.hasLength(id, "Id cannot be empty!");

		// user can access personal document only, admin can access any document.
		Document document = storageService.getDocumentMetadataById(id);
		if (request.isUserInRole("ROLE_USER")) {
			if (!document.getOwner().equalsIgnoreCase(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}

		ByteArrayInputStream is = new ByteArrayInputStream(storageService.getDocumentContentById(id));
		response.setContentType(document.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=" + document.getName());
		org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(value = "/document/{id}/info", method = RequestMethod.GET)
	public ResponseEntity<?> getDocumentsInfo(@PathVariable("id") @NotBlank String id, Principal principal,
			HttpServletRequest request) {
		// user can access personal document only, admin can access any document.
		Document document = storageService.getDocumentMetadataById(id);
		if (request.isUserInRole("ROLE_USER")) {
			if (!document.getOwner().equalsIgnoreCase(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}
		return ResponseEntity.status(200).body(document);
	}

	@RequestMapping(value = "/document/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteDocument(@PathVariable("id") @NotBlank String id, Principal principal,
			HttpServletRequest request) {
		Assert.hasLength(id, "Id cannot be empty!");
		// user can delete personal document only, admin can delete any document.
		Document document = storageService.getDocumentMetadataById(id);
		if (request.isUserInRole("ROLE_USER")) {
			if (!document.getOwner().equalsIgnoreCase(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}
		storageService.deleteDocumentById(id);
	}

	@RequestMapping(value = "/documents/personal", method = RequestMethod.GET)
	public ResponseEntity<?> getDocumentsByOwner(Principal principal) {
		return ResponseEntity.status(200).body(storageService.listAllDocumentsByOwner(principal.getName()));
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/documents/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDocuments() {
		return ResponseEntity.status(200).body(storageService.listAllDocuments());
	}

}
