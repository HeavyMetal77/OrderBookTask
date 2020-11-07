package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Book;
import ua.tarastom.entity.Type;

import java.util.Collections;
import java.util.List;

public class BookDaoImpl implements IBookDao {
    private final List<BidEntity> bidList;

    public BookDaoImpl(Book book) {
        bidList = book.getBidEntityList();
    }

    @Override
    public void updateAction(BidEntity bidEntity) {
        int i = Collections.binarySearch(bidList, bidEntity);
        if (i >= 0) {
            BidEntity entity = bidList.get(i);
            entity.setSize(entity.getSize() + bidEntity.getSize());
        } else {
            bidList.add(-(i + 1), bidEntity);
        }
    }

    public BidEntity getBestAskBidEntity() {
//        return bidList.stream().filter(entity -> entity.getType().equals(Type.Ask)).findFirst().orElse(null);
        for (BidEntity entity : bidList) {
            if (entity.getType().equals(Type.Ask)) {
                return entity;
            }
        }
        return null;
    }

    public BidEntity getBestBidEntity() {
        for (int i = bidList.size() - 1; i >= 0; i--) {
            BidEntity entity = bidList.get(i);
            if (entity.getType().equals(Type.Bid)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public BidEntity queryAction(String query) {
        BidEntity bidEntity;
        if (query.equals("best_ask")) {
            bidEntity = getBestAskBidEntity();
        } else if (query.equals("best_bid")) {
            bidEntity = getBestBidEntity();
        } else {
            throw new RuntimeException("Wrong arguments!");
        }
        return bidEntity;
    }

    @Override
    public BidEntity queryAction(int value) {
        int i = Collections.binarySearch(bidList, new BidEntity(value, 0, null));
        if (i >= 0) {
            return bidList.get(i);
        } else {
            return null;
        }
    }

    @Override
    public void orderAction(String act, int value) {
        if (act.equals("buy")) {
            orderChoiceAction(act, value, getBestAskBidEntity());
        } else if (act.equals("sell")) {
            orderChoiceAction(act, value, getBestBidEntity());
        } else {
            throw new RuntimeException("Wrong arguments!");
        }
    }

    private void orderChoiceAction(String act, int value, BidEntity bestEntity) {
        if (bestEntity != null) {
            action(act, bestEntity, bestEntity.getSize() - value);
        } else {
            throw new RuntimeException("Transaction canceled or partially completed!");
        }
    }

    private void action(String act, BidEntity bestEntity, int dif) {
        if (dif > 0) {
            bestEntity.setSize(dif);
        } else if (dif == 0) {
            bidList.remove(bestEntity);
        } else {
            bidList.remove(bestEntity);
            orderAction(act, -dif);
        }
    }

    @Override
    public String toString() {
        return "Book{" + bidList + '}';
    }

    @Override
    public List<BidEntity> getBidList() {
        return bidList;
    }
}