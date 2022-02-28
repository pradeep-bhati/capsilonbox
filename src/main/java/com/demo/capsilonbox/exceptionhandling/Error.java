package com.demo.capsilonbox.exceptionhandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Error {

		private HttpStatus status;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	    private LocalDateTime timestamp;
	    private String message;
	    private String debugMessage;
	    

	    private Error() {
	        timestamp = LocalDateTime.now();
	    }

	    Error(HttpStatus status) {
	        this();
	        this.status = status;
	    }

		Error(HttpStatus status, Throwable ex) {
	        this();
	        this.status = status;
	        this.message = "Unexpected error";
	        this.debugMessage = ex.getLocalizedMessage();
	    }

	    Error(HttpStatus status, String message, Throwable ex) {
	        this();
	        this.status = status;
	        this.message = message;
	        this.debugMessage = ex.getLocalizedMessage();
	    }
	    

	    public HttpStatus getStatus() {
			return status;
		}

		public void setStatus(HttpStatus status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getDebugMessage() {
			return debugMessage;
		}

		public void setDebugMessage(String debugMessage) {
			this.debugMessage = debugMessage;
		}

}
