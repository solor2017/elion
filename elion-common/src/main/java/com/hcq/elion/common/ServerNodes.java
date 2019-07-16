
package com.hcq.elion.common;

import com.hcq.elion.api.srd.CommonServiceNode;
import com.hcq.elion.api.srd.ServiceNames;
import com.hcq.elion.api.srd.ServiceNode;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.config.ConfigTools;

/**
 *
 *
 */
public class ServerNodes {

    public static ServiceNode cs() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getConnectServerRegisterIp());
        node.setPort(CC.elion.net.connect_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.CONN_SERVER);
        node.setAttrs(CC.elion.net.connect_server_register_attr);
        return node;
    }

    public static ServiceNode ws() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getConnectServerRegisterIp());
        node.setPort(CC.elion.net.ws_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.WS_SERVER);
        //node.addAttr(ATTR_PUBLIC_IP, ConfigTools.getPublicIp());
        return node;
    }

    public static ServiceNode gs() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getGatewayServerRegisterIp());
        node.setPort(CC.elion.net.gateway_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.GATEWAY_SERVER);
        return node;
    }
}
