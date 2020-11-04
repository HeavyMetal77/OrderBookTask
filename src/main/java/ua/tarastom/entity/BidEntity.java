package ua.tarastom.entity;

public class BidEntity {
    private int price;
    private int size;
    private Type type;

    public BidEntity(int price, int size,  Type type){
        this.price = price;
        this.size = size;
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "{" + price + ", " + size + ", " + type + '}';
    }
}
