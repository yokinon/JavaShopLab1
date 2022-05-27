package ru.ystu.cmis.domain;

public class Basket {
    private Integer id;
    private Sneakers sneakers;
    private int count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Sneakers getSneakers() {
        return sneakers;
    }

    public void setSneakers(Sneakers sneakers) {
        this.sneakers = sneakers;
    }
}
