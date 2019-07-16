
package com.hcq.elion.tools.config;

import com.hcq.elion.tools.Utils;
import com.hcq.elion.tools.config.CC.elion.net.public_ip_mapping;

/**
 */
public final class ConfigTools {

    private ConfigTools() {
    }

    public static int getHeartbeat(int min, int max) {
        return Math.max(
                CC.elion.core.min_heartbeat,
                Math.min(max, CC.elion.core.max_heartbeat)
        );
    }

    /**
     * 获取内网IP地址
     *
     * @return 内网IP地址
     */
    public static String getLocalIp() {
        if (CC.elion.net.local_ip.length() > 0) {
            return CC.elion.net.local_ip;
        }
        return Utils.lookupLocalIp();
    }

    /**
     * 获取外网IP地址
     *
     * @return 外网IP地址
     */
    public static String getPublicIp() {

        if (CC.elion.net.public_ip.length() > 0) {
            return CC.elion.net.public_ip;
        }

        String localIp = getLocalIp();

        String remoteIp = public_ip_mapping.getString(localIp);

        if (remoteIp == null) {
            remoteIp = Utils.lookupExtranetIp();
        }

        return remoteIp == null ? localIp : remoteIp;
    }


    public static String getConnectServerRegisterIp() {
        if (CC.elion.net.connect_server_register_ip.length() > 0) {
            return CC.elion.net.connect_server_register_ip;
        }
        return getPublicIp();
    }

    public static String getGatewayServerRegisterIp() {
        if (CC.elion.net.gateway_server_register_ip.length() > 0) {
            return CC.elion.net.gateway_server_register_ip;
        }
        return getLocalIp();
    }
}
