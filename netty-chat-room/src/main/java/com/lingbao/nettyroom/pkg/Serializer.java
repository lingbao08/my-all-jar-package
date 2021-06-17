package com.lingbao.nettyroom.pkg;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:09
 **/

public interface Serializer {

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * json 序列化
     */
    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();

     interface SerializerAlgorithm {
        /**
         * json 序列化标识
         */
        byte JSON = 1;
    }
}


