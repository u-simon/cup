package com.ca.ms.cup.common.task.service.publish;

import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryResponse;
import com.ca.ms.cup.common.task.service.TaskWarningQueryService;
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
@Path("worker/warning")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskWarningService {
    @Autowired
    private TaskWarningQueryService taskWarningQueryService;

    @POST
    @Path("paginate")
    public PaginateResponse<TaskWarningQueryResponse> findPaginateWarnings(TaskWarningQueryRequest queryRequest) {
        return taskWarningQueryService.findPaginateWarnings(queryRequest);
    }

}
