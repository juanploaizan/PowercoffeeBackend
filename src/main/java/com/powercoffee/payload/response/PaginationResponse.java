package com.powercoffee.payload.response;

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

    public PaginationResponse<T> build(List<T> data, Integer pageNumber, Integer pageSize, Integer totalElements, Integer totalPages, String sortBy, String sortDir, boolean first, boolean last) {
        PaginationResponse<T> paginationResponse = new PaginationResponse<>();
        paginationResponse.setData(data);
        paginationResponse.setPageNumber(pageNumber);
        paginationResponse.setPageSize(pageSize);
        paginationResponse.setTotalElements(totalElements);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setSortBy(sortBy);
        paginationResponse.setSortDir(sortDir);
        paginationResponse.setFirst(first);
        paginationResponse.setLast(last);
        return paginationResponse;
    }
}
