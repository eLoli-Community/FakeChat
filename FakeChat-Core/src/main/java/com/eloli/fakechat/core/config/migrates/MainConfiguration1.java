package com.eloli.fakechat.core.config.migrates;

import com.eloli.fakechat.core.config.Configure;
import com.eloli.fakechat.core.config.Migrate;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainConfiguration1 extends Configure {

    @Expose(deserialize = false)
    public Integer version = 1;

    @Expose
    public Map<String, com.eloli.fakechat.core.config.MainConfiguration.PlayerSetting> replace = new HashMap<>();

    public static class PlayerSetting extends Configure{
        public String replace;
        public Boolean replaceSelf;
    }
}