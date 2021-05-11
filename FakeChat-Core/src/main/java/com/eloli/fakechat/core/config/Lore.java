package com.eloli.fakechat.core.config;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Lores.class)
public @interface Lore {
    String value();
}
