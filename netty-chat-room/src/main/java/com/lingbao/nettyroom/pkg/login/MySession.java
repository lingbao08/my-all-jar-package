package com.lingbao.nettyroom.pkg.login;

import com.lingbao.nettyroom.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 15:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MySession {

    private Member member;

}
