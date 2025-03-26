package com.moviescloud.common.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private HttpStatus httpStatus;

    private Iterable<T> items;

    private long totalElements;

    private long totalPages;
}
