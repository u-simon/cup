package com.ca.ms.cup.common.route;


/**
 *
 */
public final class RouteRuleContext {
    private static final ThreadLocal<RouteRule> context = new ThreadLocal<RouteRule>();

    public static void set(String orgNo, String distributeNo, String warehouseNo) {
        RouteRule routeRule = context.get();
        if (routeRule != null) {
            return;
        }
        context.set(new RouteRule(orgNo, distributeNo, warehouseNo));
    }

    public static RouteRule get() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }

}
