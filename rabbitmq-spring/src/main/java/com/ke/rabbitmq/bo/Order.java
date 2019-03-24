package com.ke.rabbitmq.bo;

import java.io.Serializable;

public class Order implements Serializable {

    /**
     * 订单号
     */
    private String id;

    /**
     * 订单描述
     */
    private String desc;

    public Order(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
