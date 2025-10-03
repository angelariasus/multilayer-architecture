package com.biblioteca.controller;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    
    protected Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    protected Map<String, Object> createSuccessResponse(String message, Object data) {
        return createResponse(true, message, data);
    }
    
    protected Map<String, Object> createErrorResponse(String message) {
        return createResponse(false, message, null);
    }
    
    protected Map<String, Object> createErrorResponse(String message, Exception e) {
        Map<String, Object> response = createResponse(false, message, null);
        response.put("error", e.getMessage());
        return response;
    }
}