package com.ca.ms.cup.common.route;

/**
 *
 */
public interface RouteRuleResolver {
    public static final String routeRuleName = "routerule";
    public static final String routeRuleSplitter = ",";

    RouteRule resolve(String routeRuleDesc);
}
