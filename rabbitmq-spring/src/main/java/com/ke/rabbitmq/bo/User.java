package com.ke.rabbitmq.bo;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private String sex;

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
