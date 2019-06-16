package com.ca.ms.cup.common.route;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DefaultRouteRuleResolver implements RouteRuleResolver {

    @Override
    public RouteRule resolve(String routeRuleDesc) {
        Preconditions.checkArgument(StringUtils.isNotBlank(routeRuleDesc), "routeRule is blank!");
        String[] routeRuleArr = routeRuleDesc.split(routeRuleSplitter);
        Preconditions.checkArgument(routeRuleArr.length > 2, "routeRule is not illegal!desc:" + routeRuleDesc);
        return new RouteRule(routeRuleArr[0], routeRuleArr[1], routeRuleArr[2]);
    }

}
