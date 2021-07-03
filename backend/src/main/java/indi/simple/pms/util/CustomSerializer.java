package indi.simple.pms.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:35
 * @Description:
 */
class CustomSerializer {
    @JsonSerialize(using = IdSerializer.class
    )
    @JsonDeserialize(
            using = IdDeserializer.class
    )
    private Long id;
    private String name;

    public CustomSerializer() {
    }

    public CustomSerializer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
