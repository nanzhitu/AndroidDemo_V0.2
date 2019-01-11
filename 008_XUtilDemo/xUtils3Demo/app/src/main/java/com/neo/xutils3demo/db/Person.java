package com.neo.xutils3demo.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Neo on 2019/1/10.
 */

/**
 * onCreated = "sql" sql：当第一次创建表需要插入数据时候在此写sql语句例：CREATE UNIQUE INDEX index_name ON person(id,name)
 */
@Table(name ="person",onCreated = "")
public class Person {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private int id;
    @Column(name = "c_name")
    private String name;

    public Person(String name) {
        this.name = name;
    }

    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public Person() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
