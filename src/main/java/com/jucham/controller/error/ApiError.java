package com.jucham.controller.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ApiError {
    
    private String error;
    private String errorDescription;
}
