package com.ca.ms.cup.common.dto;

import java.util.List;

/**
 *
 */
public class PaginateResponse<T> extends BaseResponse {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer currentItemCount;
    private Integer totalItems;
    private List<T> items;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentItemCount() {
        return items != null ? items.size() : 0;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public PaginateResponse() {
    }

    public PaginateResponse(Integer pageIndex, Integer pageSize, Integer totalItems) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public PaginateResponse(String orgNo, String distributeNo, String warehouseNo, Integer pageIndex, Integer pageSize, Integer totalItems) {
        super(orgNo, distributeNo, warehouseNo);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public static PaginateResponse empty(Integer pageIndex, Integer pageSize) {
        return new PaginateResponse(pageIndex, pageSize, 0);
    }
}
