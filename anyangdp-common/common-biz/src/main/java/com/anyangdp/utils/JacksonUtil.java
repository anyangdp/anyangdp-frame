package com.anyangdp.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JacksonUtil {
    public static ObjectMapper objectMapper = new ObjectMapper()//
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)//
            // .setSerializationInclusion(JsonInclude.Include.ALWAYS)//
            // .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)//
            // .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)//
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) //
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS) //
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) //
            ;

    public static XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        // Use JAXB-then-Jackson annotation introspector
/*        AnnotationIntrospector intr = XmlAnnotationIntrospector.Pair.instance(
                new XmlJaxbAnnotationIntrospector(TypeFactory.defaultInstance()),new JacksonAnnotationIntrospector());
        xmlMapper.setAnnotationIntrospector(intr);*/

    }


    /**
     * javaBean,list,array convert to json string
     */
    public static String toJson(Object obj) {
        try {
            return JacksonUtil.objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("obj to json failed!!!");
        }
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return JacksonUtil.objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, TypeReference<T> type) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) JacksonUtil.objectMapper.readValue(jsonString, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json string convert to map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> json2Map(String jsonStr) {
        try {
            return JacksonUtil.objectMapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            log.warn(jsonStr + " not a JSON string!! Exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2Map(String jsonStr, Class<T> clazz) {
        Map<String, T> map = null;
        try {
            map = JacksonUtil.objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
        } catch (Exception e) {
            log.warn(jsonStr + " not a JSON string!! Exception:" + e.getMessage());
        }
        return map;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        try {
            List<Map<String, Object>> list = JacksonUtil.objectMapper.readValue(jsonArrayStr,
                    new TypeReference<List<T>>() {
                    });
            for (Map<String, Object> map : list) {
                result.add(JacksonUtil.map2pojo(map, clazz));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map<?, ?> map, Class<T> clazz) {
        return JacksonUtil.objectMapper.convertValue(map, clazz);
    }

    /**
     * json string convert to xml string
     */
    public static String json2Xml(String jsonStr) {
        try {
            JsonNode root = JacksonUtil.objectMapper.readTree(jsonStr);
            String xml = JacksonUtil.xmlMapper.writeValueAsString(root);
            return xml;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * xml string convert to json string
     */
    public static String xml2Json(String xml) {
        JsonParser jp = null;
        JsonGenerator jg = null;
        try {
            StringWriter w = new StringWriter();
            jp = JacksonUtil.xmlMapper.getFactory().createParser(xml);
            jg = JacksonUtil.objectMapper.getFactory().createGenerator(w);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();
            return w.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jp != null) {
                try {
                    jp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (jg != null) {
                try {
                    jg.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * XML To Object
     *
     * @param content
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T xmlToBean(String content, Class<T> cls) throws IOException {
        XmlMapper xml = JacksonUtil.xmlMapper;
        T obj = xml.readValue(content, cls);
        return obj;
    }

    /**
     * @param xmlFile
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T xmlToBean(File xmlFile, Class<T> cls) throws IOException {
        XmlMapper xml = JacksonUtil.xmlMapper;
        T obj = xml.readValue(xmlFile, cls);
        return obj;
    }

    /**
     * @param xmlInputStream
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T xmlToBean(InputStream xmlInputStream, Class<T> cls) throws IOException {
        XmlMapper xml = JacksonUtil.xmlMapper;
        T obj = xml.readValue(xmlInputStream, cls);
        return obj;
    }

    /**
     * 最好加上实现包，不然会出现未知错误
     * <groupId>org.codehaus.woodstox</groupId>
     * <artifactId>woodstox-core-asl</artifactId>
     */
    public static <T> String beanToXml(T bean) throws JsonProcessingException {
        XmlMapper xml = JacksonUtil.xmlMapper;
        String string = xml.writeValueAsString(bean);
        return string;
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调. 如果JSON字符串为null或"null"字符串,返回null.
     * 如果JSON字符串为"[]",返回空集合.
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) JacksonUtil.objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz, Type type) {
        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(clazz,
                TypeFactory.defaultInstance().constructType(type));
        return JacksonUtil.fromJson(json, javaType);
    }

}