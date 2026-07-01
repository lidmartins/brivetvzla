
package com.brivetvzla.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        final String correlationId = MDC.get("correlationId");
        log.warn("Unauthorized access attempt for correlation-id: {}", correlationId);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                correlationId
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        final String correlationId = MDC.get("correlationId");
        log.warn("Resource not found for correlation-id: {}", correlationId);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                correlationId
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        // The correlationId is automatically added to the log message by the Logback pattern.
        // We retrieve it here to include it in the response body.
        final String correlationId = MDC.get("correlationId");
        log.error("An unexpected error occurred for correlation-id: {}", correlationId, ex);

        // Find the root cause of the exception to provide a more specific message
        Throwable rootCause = ex;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        // Create a detailed error response for the client, including the correlation ID
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                rootCause.getMessage(),
                request.getDescription(false),
                correlationId
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

class ErrorDetails {
    @Getter @Setter private Date timestamp;
    @Getter @Setter private String message;
    @Getter @Setter private String details;
    @Getter @Setter private String correlationId;

    public ErrorDetails(Date timestamp, String message, String details, String correlationId) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.correlationId = correlationId;
    }
}