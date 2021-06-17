package com.lingbao.nettyroom.pkg.login;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 15:12
 **/
@Data
@NoArgsConstructor
public class MySession {

    private String userId;

    private String userName;

    public MySession(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
