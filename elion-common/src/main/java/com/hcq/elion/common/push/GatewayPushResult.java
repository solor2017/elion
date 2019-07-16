
package com.hcq.elion.common.push;

import com.hcq.elion.common.message.gateway.GatewayPushMessage;
import com.hcq.elion.tools.Jsons;

/**
 *
 *
 */
public final class GatewayPushResult {
    public String userId;
    public Integer clientType;
    public Object[] timePoints;

    public GatewayPushResult() {
    }

    public GatewayPushResult(String userId, Integer clientType, Object[] timePoints) {
        this.userId = userId;
        this.timePoints = timePoints;
        if (clientType > 0) this.clientType = clientType;
    }

    public static String toJson(GatewayPushMessage message, Object[] timePoints) {
        return Jsons.toJson(new GatewayPushResult(message.userId, message.clientType, timePoints));
    }

    public static GatewayPushResult fromJson(String json) {
        if (json == null) return null;
        return Jsons.fromJson(json, GatewayPushResult.class);
    }
}
