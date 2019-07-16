
package com.hcq.elion.cache.redis.manager;

import com.hcq.elion.cache.redis.RedisServer;

import java.util.List;

public interface RedisClusterManager {

    void init();

    List<RedisServer> getServers();
}
