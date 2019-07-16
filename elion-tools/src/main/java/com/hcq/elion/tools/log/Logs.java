
package com.hcq.elion.tools.log;

import com.hcq.elion.tools.config.CC;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
public interface Logs {
    boolean logInit = init();

    static boolean init() {
        if (logInit) return true;
        System.setProperty("log.home", CC.elion.log_dir);
        System.setProperty("log.root.level", CC.elion.log_level);
        System.setProperty("logback.configurationFile", CC.elion.log_conf_path);
        LoggerFactory
                .getLogger("console")
                .info(CC.elion.cfg.root().render(ConfigRenderOptions.concise().setFormatted(true)));
        return true;
    }

    Logger Console = LoggerFactory.getLogger("console"),

    CONN = LoggerFactory.getLogger("elion.conn.log"),

    MONITOR = LoggerFactory.getLogger("elion.monitor.log"),

    PUSH = LoggerFactory.getLogger("elion.push.log"),

    HB = LoggerFactory.getLogger("elion.heartbeat.log"),

    CACHE = LoggerFactory.getLogger("elion.cache.log"),

    RSD = LoggerFactory.getLogger("elion.srd.log"),

    HTTP = LoggerFactory.getLogger("elion.http.log"),

    PROFILE = LoggerFactory.getLogger("elion.profile.log");
}
