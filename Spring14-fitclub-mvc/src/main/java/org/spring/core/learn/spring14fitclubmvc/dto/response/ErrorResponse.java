package org.spring.core.learn.spring14fitclubmvc.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

    /*
     * ================= REST API: consistent JSON error shape =================
     * React needs a predictable JSON error SHAPE it can parse consistently
     * for every kind of failure, e.g.:
     *   { "status": 409, "message": "Username already taken: ravi_k",
     *     "fieldErrors": null, "timestamp": "2026-08-23T10:15:30" }
     */
    public class ErrorResponse {

        private final int status;
        private final String message;
        private final Map<String, String> fieldErrors; // null unless this was a validation failure
        private final LocalDateTime timestamp;

        public ErrorResponse(int status, String message, Map<String, String> fieldErrors) {
            this.status = status;
            this.message = message;
            this.fieldErrors = fieldErrors;
            this.timestamp = LocalDateTime.now();
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public Map<String, String> getFieldErrors() { return fieldErrors; }
        public LocalDateTime getTimestamp() { return timestamp; }
}
