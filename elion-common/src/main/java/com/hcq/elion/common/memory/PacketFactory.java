
package com.hcq.elion.common.memory;

import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.api.protocol.UDPPacket;
import com.hcq.elion.tools.config.CC;

/**
 */
public interface PacketFactory {
    PacketFactory FACTORY = CC.elion.net.udpGateway() ? UDPPacket::new : Packet::new;

    static Packet get(Command command) {
        return FACTORY.create(command);
    }

    Packet create(Command command);
}