package indi.simple.pms.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:26
 * @Description:
 */
public class GrantedAuthoritySerializer extends JsonSerializer<Collection<? extends GrantedAuthority>> {
    public GrantedAuthoritySerializer() {
    }

    public void serialize(Collection<? extends GrantedAuthority> grantedAuthorities, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Iterator var4 = grantedAuthorities.iterator();

        while(var4.hasNext()) {
            GrantedAuthority grantedAuthority = (GrantedAuthority)var4.next();
            jsonGenerator.writeString(grantedAuthority.getAuthority());
        }

    }

    public void serializeWithType(Collection<? extends GrantedAuthority> grantedAuthorities, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
        WritableTypeId typeId = typeSerializer.typeId(grantedAuthorities, JsonToken.START_ARRAY);
        typeSerializer.writeTypePrefix(jsonGenerator, typeId);
        this.serialize(grantedAuthorities, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffix(jsonGenerator, typeId);
    }
}

