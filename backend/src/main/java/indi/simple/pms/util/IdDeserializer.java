package indi.simple.pms.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:36
 * @Description:
 */
class IdDeserializer extends JsonDeserializer<Long> {
    IdDeserializer() {
    }

    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper)jsonParser.getCodec();
        JsonNode jsonNode = (JsonNode)mapper.readTree(jsonParser);
        return jsonNode.asLong() * 4L;
    }
}
