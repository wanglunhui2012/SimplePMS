package indi.simple.pms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:37
 * @Description:
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final String JSON_OBJ_STRING = "{\"id\":1,\"name\":\"wlh\"}";
    private static final String JSON_ARRAY_STRING = "[{\"id\":1,\"name\":\"wlh\",\"password\":\"123456\"},{\"id\":2,\"name\":\"hw\",\"password\":\"456123\"}]";
    private static final String COMPLEX_JSON_STRING = "{\"name\":\"whut\",\"code\":10497,\"department\":{\"name\":\"软件开发部\",\"floor\":4},\"students\":[{\"id\":1,\"name\":\"wlh\"},{\"id\":2,\"name\":\"hw\"}]}";
    private static ObjectMapper objectMapper = newObjectMapper();

    public static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());

        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String objectToJson(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException var2) {
                LOGGER.error(ThrowableUtil.getStackTrace(var2));
                return null;
            }
        }
    }

    public static <T> T jsonToObject(String str, Class<T> clz) {
        if (str != null && !str.equals("")) {
            try {
                return objectMapper.readValue(str, clz);
            } catch (IOException var3) {
                LOGGER.error(ThrowableUtil.getStackTrace(var3));
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T> T jsonToObject(String str, TypeReference<T> typeReference) {
        if (str != null && !str.equals("")) {
            try {
                return objectMapper.readValue(str, typeReference);
            } catch (IOException var3) {
                LOGGER.error(ThrowableUtil.getStackTrace(var3));
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Student student = new Student(1, "wlh");
        Department department = new Department("软件开发部", 4);
        Organization organization = new Organization("gzport", 100041, department, Arrays.asList(student));
        System.out.println("对象=>JSON");
        System.out.println(objectToJson(student));
        System.out.println(objectToJson(department));
        System.out.println(objectToJson(organization));
        Student student1 = (Student)jsonToObject("{\"id\":1,\"name\":\"wlh\"}", Student.class);
        List<Department> department1 = (List)jsonToObject("[{\"id\":1,\"name\":\"wlh\",\"password\":\"123456\"},{\"id\":2,\"name\":\"hw\",\"password\":\"456123\"}]", List.class);
        Organization organization1 = (Organization)jsonToObject("{\"name\":\"whut\",\"code\":10497,\"department\":{\"name\":\"软件开发部\",\"floor\":4},\"students\":[{\"id\":1,\"name\":\"wlh\"},{\"id\":2,\"name\":\"hw\"}]}", Organization.class);
        System.out.println("JSON=>对象");
        System.out.println(student1);
        System.out.println(department1);
        System.out.println(organization1);
        JsonNode node = objectMapper.readTree("{\"id\":1,\"name\":\"wlh\"}");
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        JsonNode node1 = objectMapper.readTree("[{\"id\":1,\"name\":\"wlh\",\"password\":\"123456\"},{\"id\":2,\"name\":\"hw\",\"password\":\"456123\"}]");

        JsonNode next;
        String var13;
        for(Iterator elements = node1.elements(); elements.hasNext(); var13 = next.get("password").asText()) {
            next = (JsonNode)elements.next();
            id = next.get("id").asInt();
            name = next.get("name").asText();
        }

        next = objectMapper.readTree("{\"name\":\"whut\",\"code\":10497,\"department\":{\"name\":\"软件开发部\",\"floor\":4},\"students\":[{\"id\":1,\"name\":\"wlh\"},{\"id\":2,\"name\":\"hw\"}]}");
        MyTest myTest = new MyTest(1, "wlh", "123456", true, new Timestamp((new Date()).getTime()), 1, 2, 3);
        System.out.println(objectToJson(myTest));
        CustomSerializer customSerializer = new CustomSerializer(1L, "wlh");
        String customSerializerJson = objectToJson(customSerializer);
        System.out.println(customSerializerJson);
        CustomSerializer customDeSerializer = (CustomSerializer)jsonToObject(customSerializerJson, CustomSerializer.class);
        System.out.println(customDeSerializer.getId());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Student{
        private int id;
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    static class Department{
        private String name;
        private int code;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    static class Organization{
        private String name;
        private int code;
        private Department department;
        private List<Student> studentList;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    static class MyTest{
        private int id;
        private String name;
        private String password;
        private boolean enable;
        private Timestamp createTime;
        private int v1;
        private int v2;
        private int v3;
    }
}
