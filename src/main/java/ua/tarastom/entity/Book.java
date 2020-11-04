package ua.tarastom.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Book {

    private final List<BidEntity> bidEntityList;

    public Book() {
        bidEntityList = new ArrayList<>();
    }

    public List<BidEntity> getBidEntityList() {
        return bidEntityList;
    }

    @Override
    public String toString() {
        return "Book{" + bidEntityList + '}';
    }
}
