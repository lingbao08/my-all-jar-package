package com.lingbao.base.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CityEnum {

    //河北省
    X_130109(130109, "藁城区"),

    //陕西省
    X_610425(610425, "礼泉县"),

    //
    ;

    private int code;
    private String cityName;

    public static int[] getAllCode() {
        return ArrayUtils.toPrimitive(Arrays.stream(values())
                .map(city -> city.code).collect(Collectors.toList()).toArray(new Integer[]{}));
    }

    public static String getRandomCode() {
        int[] allCode = CityEnum.getAllCode();
        return String.valueOf(allCode[new Random().nextInt(allCode.length)]);
    }
}
