package com.poiji.deserialize.model;

import com.poiji.annotation.ExcelCell;

public class EmployeeComment {

    @ExcelCell(1)
    protected Long employeeId;

    @ExcelCell(2)
    protected String name = "";

    @ExcelCell(3)
    protected String surname;

    @ExcelCell(4)
    protected Integer age;

    @ExcelCell(5)
    protected Boolean single;

    @ExcelCell(6)
    protected String birthday;

    /*
        We normally don't need getters and setters to map excel cells to fields
     */

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", single=" + single +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
