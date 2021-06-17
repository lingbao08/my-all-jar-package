package com.lingbao.cache.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 3/27/21 10:42
 **/

public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //如果遇到反序列化autoType is not support错误，请添加并修改一下包名到bean文件路径
        // ParserConfig.getGlobalInstance().addAccept("com.xxxxx.xxx");
    }

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        if (t instanceof String)
            return ((String) t).getBytes(DEFAULT_CHARSET);
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }


    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        String word = new String(bytes, DEFAULT_CHARSET);
        if(!word.startsWith("{")){
            return (T) word;
        }
        return JSON.parseObject(word, clazz);
    }

}
