
package com.hcq.elion.common;

public final class CacheKeys {

    private static final String USER_PREFIX = "elion:ur:";//用户路由

    private static final String SESSION_PREFIX = "elion:rs:";//可复用session

    private static final String FAST_CONNECTION_DEVICE_PREFIX = "elion:fcd:";

    private static final String ONLINE_USER_LIST_KEY_PREFIX = "elion:oul:";//在线用户列表

    public static final String SESSION_AES_KEY = "elion:sa";
    public static final String SESSION_AES_SEQ_KEY = "elion:sas";
    public static final String PUSH_TASK_PREFIX = "elion:pt";

    public static String getUserRouteKey(String userId) {
        return USER_PREFIX + userId;
    }

    public static String getSessionKey(String sessionId) {
        return SESSION_PREFIX + sessionId;
    }

    public static String getDeviceIdKey(String deviceId) {
        return FAST_CONNECTION_DEVICE_PREFIX + deviceId;
    }

    public static String getOnlineUserListKey(String publicIP) {
        return ONLINE_USER_LIST_KEY_PREFIX + publicIP;
    }

    public static String getPushTaskKey(String taskId) {
        return PUSH_TASK_PREFIX + taskId;
    }

}
