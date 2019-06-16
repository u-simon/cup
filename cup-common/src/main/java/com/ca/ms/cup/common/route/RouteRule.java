package com.ca.ms.cup.common.route;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class RouteRule {
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getDistributeNo() {
        return distributeNo;
    }

    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }


    public String format() {
        return StringUtils.join(new String[]{this.orgNo, this.distributeNo, this.warehouseNo}, RouteRuleResolver.routeRuleSplitter);
    }

    public RouteRule() {
    }

    public RouteRule(String orgNo, String distributeNo, String warehouseNo) {
        this.orgNo = orgNo;
        this.distributeNo = distributeNo;
        this.warehouseNo = warehouseNo;
    }

}
