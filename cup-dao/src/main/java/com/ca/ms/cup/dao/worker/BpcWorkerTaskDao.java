package com.ca.ms.cup.dao.worker;

import com.ca.ms.cup.domain.worker.BpcWorkerTask;
import com.ca.ms.cup.domain.worker.BpcWorkerTaskQuery;
import com.ca.ms.cup.domain.worker.TaskNoBillNoInfo;

import java.util.List;

public interface BpcWorkerTaskDao {

    List<BpcWorkerTask> selectList(BpcWorkerTaskQuery workerTask);



    Integer count(BpcWorkerTaskQuery workerTask);



    List<BpcWorkerTask> selectOutboundTask(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectOutboundBillNoByTaskNo(BpcWorkerTaskQuery bpcWorkerTaskQuery);


    List<TaskNoBillNoInfo> selectByPickResult(BpcWorkerTaskQuery bpcWorkerTaskQuery);



    List<TaskNoBillNoInfo> selectByPutawayTask(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectByPutawayResult(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectByPutawayReject(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectByMoveTask(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectBySpecialTask(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectByReviewResult(BpcWorkerTaskQuery bpcWorkerTaskQuery);

    List<TaskNoBillNoInfo> selectByReviewMilestone(BpcWorkerTaskQuery bpcWorkerTaskQuery);


    List<TaskNoBillNoInfo> selectByReviewBill(BpcWorkerTaskQuery bpcWorkerTaskQuery);

}
