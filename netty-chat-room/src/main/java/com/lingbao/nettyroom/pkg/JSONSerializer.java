package com.lingbao.nettyroom.pkg;

import com.alibaba.fastjson.JSON;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:17
 **/

public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
