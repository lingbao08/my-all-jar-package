package com.lingbao.nettyroom.entity;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:09
 **/

public interface Command {

    Byte COMMON_RES = 0;


    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;
    Byte MESSAGE_RESPONSE = 4;

    Byte CREATE_GROUP_REQ = 5;
    Byte CREATE_GROUP_RES = 6;

    Byte SEND_GROUP_REQ = 7;
    Byte SEND_GROUP_RES = 8;

    Byte LOGOUT_GROUP_REQ = 9;
    Byte LOGOUT_GROUP_RES = 10;

    Byte LIST_GROUP_REQ = 11;
    Byte LIST_GROUP_RES = 12;

    Byte ADD_TO_GROUP_REQ = 13;
    Byte ADD_TO_GROUP_RES = 14;





}

