package indi.simple.pms.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:26
 * @Description:
 */
@Slf4j
public class GrantedAuthorityDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {
    public Collection<? extends GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper)jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);
        Set<SimpleGrantedAuthority> grantedAuthorities = new LinkedHashSet();
        Iterator elements = jsonNode.elements();

        while(elements.hasNext()) {
            JsonNode next = (JsonNode)elements.next();
            grantedAuthorities.add(new SimpleGrantedAuthority(next.asText()));
        }

        return grantedAuthorities;
    }

    public Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        ObjectMapper mapper = (ObjectMapper)jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);
        Set grantedAuthorities = null;

        try {
            Class<?> clz = Class.forName(jsonNode.get(0).asText());
            grantedAuthorities = (Set)clz.newInstance();
            Iterator elements = jsonNode.get(1).elements();

            while(elements.hasNext()) {
                JsonNode next = (JsonNode)elements.next();
                grantedAuthorities.add(new SimpleGrantedAuthority(next.asText()));
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return grantedAuthorities;
    }
}
