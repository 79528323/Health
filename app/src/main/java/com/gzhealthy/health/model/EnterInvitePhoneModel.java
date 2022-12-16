package com.gzhealthy.health.model;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class EnterInvitePhoneModel {
    private String msg;
    private int code;
    private DataDTO data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private String member;
        private String memberNickName;
        private String memberAvatar;

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getMemberNickName() {
            return memberNickName;
        }

        public void setMemberNickName(String memberNickName) {
            this.memberNickName = memberNickName;
        }

        public String getMemberAvatar() {
            return memberAvatar;
        }

        public void setMemberAvatar(String memberAvatar) {
            this.memberAvatar = memberAvatar;
        }
    }
}
