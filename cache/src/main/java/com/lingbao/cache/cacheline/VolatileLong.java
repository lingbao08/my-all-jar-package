package com.lingbao.cache.cacheline;

import sun.misc.Contended;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 3/27/21 10:52
 **/

@Contended
public class VolatileLong {
    public volatile long value = 0L;
}
