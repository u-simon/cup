package com.ca.ms.cup.domain.worker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BpcWorkerTaskType {

    /**
     * 1. 出库初始化,任务下发,开始拣货,拣货完成...
     */
    private static final List<Integer> outboundTaskTypes = Arrays.asList(10, 90, 91,95, 25, 110, 100, 110, 225);

    /**
     * 2.出库任务:拣货结果
     */
    private static final List<Integer> outboundPickTypes = Arrays.asList(111);

    /**
     * 3. 库存差异回传
     */
    private static final List<Integer> stockDiffBackTypes = Arrays.asList(140);

    /**
     * 4. 计算库存差异
     */
    private static final List<Integer> stockDiffCalculateTypes = Arrays.asList(60);

    /**
     * 5.入库初始化,任务下发.移库入回传,入库任务取消
     */
    private static final List<Integer> inboundTaskTypes = Arrays.asList(26, 30, 120, 220);

    /**
     * 6.上架完成回传
     */
    private static final List<Integer> inboundFinishBackTypes = Arrays.asList(130);

    /**
     * 7. 入库上架驳回
     */
    private static final List<Integer> inboundRejectBackTypes = Arrays.asList(150);

    /**
     * 8. 生成盘点任务
     */
    private static final List<Integer> reviewTypes = Arrays.asList(40);

    /**
     * 9. 盘点结果回传
     */
    private static final List<Integer> reviewBatchbackTypes = Arrays.asList(160);

    /**
     * 10. 盘点回传,bill
     */
    private static final List<Integer> reviewBillBackTypes = Arrays.asList(161);

    /**
     * 11. 移库任务下发
     */
    private static final List<Integer> moveTypes = Arrays.asList(170);

    /**
     * 12. 合箱整理任务
     */
    private static final List<Integer> arrangeTypes = Arrays.asList(190);

    /**
     * 13. 补货
     * 201 生成某sku的一般补货建议
     */
    private static final List<Integer> replenishProposalInitTypes = Arrays.asList(201);

    /**
     * 14. 拆托余量出,空托设备收集
     */
    private static final List<Integer> moveProposalFeedbackTypes = Arrays.asList(211, 212);

    /**
     * 15. 空托盘相关
     */
    private static final List<Integer> specialTaskTypes = Arrays.asList(210, 213, 214, 216);

    /**
     * 16. 下发盘点任务,生成补货建议,补货建议回传,空托任务收集
     */
    private static final List<Integer> loopTaskTypes = Arrays.asList(50, 180, 202, 215);

    /**
     * 16. 查询排产理货下架明细
     */
    private static final List<Integer> queryCheckTaskDetailTypes = Arrays.asList(245);



    private static final Map<Integer, List<Integer>> taskTypeMap = new HashMap();


    static {
        taskTypeMap.put(1, outboundTaskTypes);
        taskTypeMap.put(2, outboundPickTypes);
        taskTypeMap.put(3, stockDiffBackTypes);
        taskTypeMap.put(4, stockDiffCalculateTypes);
        taskTypeMap.put(5, inboundTaskTypes);
        taskTypeMap.put(6, inboundFinishBackTypes);
        taskTypeMap.put(7, inboundRejectBackTypes);
        taskTypeMap.put(8, reviewTypes);
        taskTypeMap.put(9, reviewBatchbackTypes);
        taskTypeMap.put(10, reviewBillBackTypes);
        taskTypeMap.put(11, moveTypes);
        taskTypeMap.put(12, arrangeTypes);
        taskTypeMap.put(13, replenishProposalInitTypes);
        taskTypeMap.put(14, moveProposalFeedbackTypes);
        taskTypeMap.put(15, specialTaskTypes);
        taskTypeMap.put(16, loopTaskTypes);
        taskTypeMap.put(17, queryCheckTaskDetailTypes);

    }

    public Integer getGroupCode(Integer bizType) {
        if (outboundTaskTypes.contains(bizType)) {
            return 1;
        } else if (outboundPickTypes.contains(bizType)) {
            return 2;
        } else if (stockDiffBackTypes.contains(bizType)) {
            return 3;
        } else if (stockDiffCalculateTypes.contains(bizType)) {
            return 4;
        } else if (inboundTaskTypes.contains(bizType)) {
            return 5;
        } else if (inboundFinishBackTypes.contains(bizType)) {
            return 6;
        } else if (inboundRejectBackTypes.contains(bizType)) {
            return 7;
        } else if (reviewTypes.contains(bizType)) {
            return 8;
        } else if (reviewBatchbackTypes.contains(bizType)) {
            return 9;
        } else if (reviewBillBackTypes.contains(bizType)) {
            return 10;
        } else if (moveTypes.contains(bizType)) {
            return 11;
        } else if (arrangeTypes.contains(bizType)) {
            return 12;
        } else if (replenishProposalInitTypes.contains(bizType)) {
            return 13;
        } else if (moveProposalFeedbackTypes.contains(bizType)) {
            return 14;
        } else if (specialTaskTypes.contains(bizType)) {
            return 15;
        } else if (loopTaskTypes.contains(bizType)) {
            return 16;
        } else if (queryCheckTaskDetailTypes.contains(bizType)) {
            return 17;
        } else {
            return 100;
        }
    }

}
