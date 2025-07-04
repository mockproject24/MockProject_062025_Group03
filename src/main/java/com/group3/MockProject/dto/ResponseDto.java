package com.group3.MockProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "Success", data);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(200, message, data);
    }

    public static <T> ResponseDto<T> error( String message, int code) {
        return new ResponseDto<>(code, message);
    }

    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(500, message);
    }
}