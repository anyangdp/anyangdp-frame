/**
 * For Business Activities.
 * <p>
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 * <p>
 * Create At 2016年8月28日
 */
package com.anyangdp.tools.util;

import com.anyangdp.tools.handler.AbstractDO;
import com.anyangdp.tools.handler.AbstractDTO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author anyangdp
 */
@Slf4j
public class ValueUtil {

//    public final static Hashids hashids = new Hashids("zy-salt", 12);

    private final static DozerBeanMapper mapper = new DozerBeanMapper();

//    static public class LongToHashIdConverter implements CustomConverter {
//        @Override
//        public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
//            existingDestinationFieldValue = hashids.encode((Long) sourceFieldValue);
//            return existingDestinationFieldValue;
//        }
//    }
//
//    static public class HashIdToLongConverter implements CustomConverter {
//        @Override
//        public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
//            existingDestinationFieldValue = idDecode((String) sourceFieldValue);
//            return existingDestinationFieldValue;
//        }
//    }


    static {

        List<String> mappingFiles = new ArrayList();
        mappingFiles.add("dozerJdk8Converters.xml");

        mapper.setMappingFiles(mappingFiles);
        mapper.addMapping(new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(AbstractDO.class, AbstractDTO.class, TypeMappingOptions.oneWay());
                mapping(AbstractDTO.class, AbstractDO.class, TypeMappingOptions.oneWay());

            }
        });
    }

    public static <S, T> T dump(S source, Class<T> targetClass) {
        if (null == source) {
            return null;
        }
        return mapper.map(source, targetClass);
    }


    public static <S, T> T copy(S source, T target) {
        mapper.map(source, target);
        return target;
    }

    public static <S, T> T dump(Optional<S> source, Class<T> targetClass) {
        if (null == source || false == source.isPresent()) {
            return null;
        }
        return dump(source.get(), targetClass);
    }

    public static <S, T> List<T> dumpList(Collection<S> sourceList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }
        return sourceList.stream().map(v -> dump(v, clazz)).collect(Collectors.toList());
    }

    public static boolean contains(Collection<String> source, String target) {

        for (String key : source) {
            if (key.equalsIgnoreCase(target)) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public static boolean contains(String[] source, String target) {

        for (String key : source) {
            if (key.equalsIgnoreCase(target)) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public static int parseStringToInteger(String string) {
        int result = 0;

        if (StringUtils.hasText(string)) {
            try {
                result = NumberFormat.getInstance().parse(string).intValue();
            } catch (ParseException e) {
                log.error("!!! Parse string fail {} !!!", string);
            }
        }

        return result;
    }

    public static byte[] writeObjectToByte(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            log.error("!!! Write object to bytes failed !!");
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("!!! Close OutputStream failed !!");
                }
            }
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("!!! Close ObjectOutput failed !!");
                }
            }
        }

        return bytes;
    }

    public static Object readObjectFromByte(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object object = null;
        try {
            in = new ObjectInputStream(bis);
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("!!! Read object from bytes failed !!");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("!!! Close InputStream failed !!");
                }
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("!!! Close ObjectInput failed !!");
                }
            }
        }

        return object;
    }
}
