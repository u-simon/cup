package com.ca.ms.cup.common.task.service.publish;


import com.ca.ms.cup.common.dto.BaseRequest;
import com.ca.ms.cup.common.dto.BooleanResponse;
import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.dto.TaskGroupByStatusResponseList;
import com.ca.ms.cup.common.task.dto.TaskModifyRequest;
import com.ca.ms.cup.common.task.dto.TaskQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskQueryResponse;
import com.ca.ms.cup.common.task.service.TaskQueryService;
import com.ca.ms.cup.common.task.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Service
@Path("worker/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskService {
    @Autowired
    private TaskQueryService taskQueryService;
    @Autowired
    private TaskStatusService taskStatusService;

    @POST
    @Path("paginate")
    public PaginateResponse<TaskQueryResponse> findPaginateTasks(TaskQueryRequest taskQueryRequest) {
        return taskQueryService.findPaginateTasks(taskQueryRequest);
    }

    @POST
    @Path("reset")
    public BooleanResponse reset(TaskModifyRequest taskModifyRequest) {
        return taskStatusService.reset(taskModifyRequest);
    }

    @POST
    @Path("group")
    public TaskGroupByStatusResponseList group(BaseRequest baseRequest) {
        return taskQueryService.findTaskGroups(baseRequest);
    }

}
