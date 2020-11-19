package ua.tarastom.entity;

import java.util.TreeSet;

public class Book {

    private final TreeSet<BidEntity> bidList;
    private final TreeSet<BidEntity> askList;

    public Book() {
        bidList = new TreeSet<>((o1, o2) -> o2.getPrice() - o1.getPrice());
        askList = new TreeSet<>();
    }

    public TreeSet<BidEntity> getAskList() {
        return askList;
    }

    public TreeSet<BidEntity> getBidList() {
        return bidList;
    }

    @Override
    public String toString() {
        return "Book{" + bidList + '}';
    }
}
