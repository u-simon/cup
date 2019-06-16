package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public class PaginateQueryClause extends WarehouseDefinition {
    private Integer pageSize = 10;
    private Integer pageIndex = 1;
    private Integer startIndex;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            return;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        if (pageIndex == null) {
            return;
        }
        this.pageIndex = pageIndex;
    }

    public Integer getStartIndex() {
        return pageIndex == 1 ? 0 : (pageIndex - 1) * pageSize;
    }

    public PaginateQueryClause() {
    }

}
