package com.eloli.fakechat.core;

import java.io.IOException;

public class FakeChatCore {
    public static String basePath;
    public static PlatformAdapter api;
    public FakeChatCore(PlatformAdapter api, String basePath){
        FakeChatCore.api = api;
        FakeChatCore.basePath=basePath;
        try {
            Config.init();
        } catch (IOException e) {
            warn("Failed to load config",e);
        }
    }

    public static void info(String message){
        api.info(message);
    }
    public static void info(String message,Exception exception){
        api.info(message,exception);
    }
    public static void warn(String message){
        api.warn(message);
    }
    public static void warn(String message,Exception exception){
        api.warn(message,exception);
    }
}
