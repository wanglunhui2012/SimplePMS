package indi.simple.pms.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:37
 * @Description:
 */
class IdSerializer extends JsonSerializer<Long> {
    IdSerializer() {
    }

    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(aLong * 2L);
    }
}
