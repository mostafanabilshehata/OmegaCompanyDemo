package com.armada.mostafa.omegacompanydemo;

/**
 * Created by pc on 12/10/2016.
 */
public class Employee {
    public String name;
    public String code;

    public Employee() {
    }

    public Employee(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
