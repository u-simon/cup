package com.ca.ms.cup.common.db;

import java.io.Serializable;
import java.util.List;


/**
 * @param <T>
 * @author jianglongfei
 * @ClassName: Page
 * @Description: 分页类
 * @date 2014-6-10 上午09:21:39
 */
public class Page<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5231189231501990278L;

	private int page;

    //总页数
    private int pages;
    //总记录数
    private int total;

    public int pageSize = 10;

    public List<T> rows;


    private String sort;

    private int sortIndex;

    private String order;
    
    private String sidx;
    
    private String sord;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPageSize() {
        if (rows != null && rows.size() == 1) {
            Object obj = rows.get(0);
            if (obj instanceof String) {
            	pageSize =  Integer.parseInt(rows.get(0).toString());
            }
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return rows;
    }

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPages() {
    	return total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
