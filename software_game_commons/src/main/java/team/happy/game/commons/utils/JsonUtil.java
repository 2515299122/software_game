package team.happy.game.commons.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.happy.game.commons.entity.ActionStatus;
import team.happy.game.commons.entity.Direction;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.entity.User;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 冰冰
 * @date 2020/11/21 上午8:46
 */
public class JsonUtil {

    public static <K, V> String toJson(Map<K, V> obj) {
        String jsonString;
        try {
            if (obj == null) {
                return "";
            }
            StringWriter sw = new StringWriter();
            JsonGenerator gen = new JsonFactory().createGenerator(sw);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(gen, obj);
            jsonString = sw.toString();
            sw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

        return jsonString;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            if (json == null || "".equals(json)) {
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(json, Map.class);
            if (result == null) {
                return new HashMap<>(16);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static String toJson(Object object) {
        String json;
        try {
            if (object == null) {
                return "";
            }
            StringWriter sw = new StringWriter();
            JsonGenerator gen = new JsonFactory().createGenerator(sw);
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(gen, object);

            json = sw.toString();
            sw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            json = "";
        }
        return json;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        T obj;
        try {
            if (json == null || "".equals(json)) {
                return null;
            }
            JsonFactory jsonFactory = new MappingJsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(json);
            ObjectMapper mapper = new ObjectMapper();
            obj = mapper.readValue(jsonParser, clazz);

        } catch (Exception ex) {
            ex.printStackTrace();
            obj = null;
        }
        return obj;

    }
    public static <T>T toObject(Object map,Class<T> clazz){
        T obj;
        try {
            if (map == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            obj=mapper.convertValue(map,clazz);

        } catch (Exception ex) {
            ex.printStackTrace();
            obj = null;
        }
        return obj;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (json == null || "".equals(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<T> list = new ArrayList<>();
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            list = mapper.readValue(json, type);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public static byte[] toByteArray(Object object){
        String json = toJson(object);
        return json.getBytes();
    }
    public static  byte[] toByteArray(String json){
        return json.getBytes();
    }

    public static void main(String[] args) {
        char[][] chars = new char[20][20];
        for (int i = 0; i < chars.length; i++) {
            for (int i1 = 0; i1 < chars[i].length; i1++) {
                chars[i][i1]='0';
            }
        }
        Message<Direction> message = new Message<Direction>(ActionStatus.UN_KONW, Direction.DOWN);
        String json = JsonUtil.toJson(message);
        System.out.println(json);
        Message message1 = JsonUtil.toObject(json, Message.class);
        System.out.println(message1);

    }

}
