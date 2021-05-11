package com.eloli.fakechat.core.config;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainConfiguration extends Configure {

    @Migrate("version")
    @Expose(deserialize = false)
    public Integer version = 2;

    @Migrate("replace")
    @Expose
    public Map<String,PlayerSetting> replace = new HashMap<>();

    public static class PlayerSetting extends Configure{
        @Expose
        public String replace;

        @Expose
        public Boolean replaceSelf;
    }
}
