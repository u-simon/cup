package com.ca.ms.cup.common.task.dto;


import com.ca.ms.cup.common.dto.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskGroupByStatusResponseList extends BaseResponse {
    private List<TaskGroupByStatusResponse> items = new ArrayList<>();

    public List<TaskGroupByStatusResponse> getItems() {
        return items;
    }

    public void setItems(List<TaskGroupByStatusResponse> items) {
        this.items = items;
    }
}
