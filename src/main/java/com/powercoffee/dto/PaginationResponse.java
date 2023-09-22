package com.powercoffee.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class PaginationResponse<T> {
    private List<T> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private String sortBy;
    private String sortDir;
    private boolean first;
    private boolean last;
}
