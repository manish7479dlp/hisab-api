package com.fms.util;

import java.time.LocalDateTime;

public class ErrorResponse {

    private boolean success;
    private ErrorDetails error;
    private LocalDateTime timestamp;

    public ErrorResponse(ErrorDetails error) {
        this.success = false;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public ErrorDetails getError() { return error; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

