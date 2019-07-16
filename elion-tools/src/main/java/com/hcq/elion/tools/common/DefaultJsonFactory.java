package com.hcq.elion.tools.common;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.Json;
import com.hcq.elion.api.spi.common.JsonFactory;
import com.hcq.elion.tools.Jsons;

@Spi
public final class DefaultJsonFactory implements JsonFactory, Json {
    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        return Jsons.fromJson(json, clazz);
    }

    @Override
    public String toJson(Object json) {
        return Jsons.toJson(json);
    }

    @Override
    public Json get() {
        return this;
    }
}