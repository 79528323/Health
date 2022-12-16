package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.BaseModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class RateWarnHelpModel extends BaseModel<RateWarnHelpModel.DataDTO> {

    public static class DataDTO {
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
