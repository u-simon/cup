package com.ca.ms.cup.controller;

import com.ca.ms.cup.dao.worker.BpcWorkerTaskDao;
import com.ca.ms.cup.domain.worker.BpcWorkerTask;
import com.ca.ms.cup.domain.worker.BpcWorkerTaskQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("bpcworker")
public class BpcWorkerController {
    @Resource
    private BpcWorkerTaskDao bpcWorkerTaskDao;

    @RequestMapping("query")
    @ResponseBody
    public List<BpcWorkerTask> queryWorkerList(BpcWorkerTaskQuery workerTask) {
        List<BpcWorkerTask> bpcWorkerTasks = bpcWorkerTaskDao.selectList(workerTask);
        return bpcWorkerTasks;
    }
}
