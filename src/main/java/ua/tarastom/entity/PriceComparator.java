package ua.tarastom.entity;

import java.util.Comparator;

public class PriceComparator implements Comparator<BidEntity> {

    @Override
    public int compare(BidEntity o1, BidEntity o2) {
        return Integer.compare(o2.getPrice(),  o1.getPrice());
    }
}
