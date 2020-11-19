package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Book;
import ua.tarastom.entity.Type;

import java.util.TreeSet;

public class BookDaoImpl implements IBookDao {
    private final TreeSet<BidEntity> askList;
    private final TreeSet<BidEntity> bidList;

    public BookDaoImpl(Book book) {
        bidList = book.getBidList();
        askList = book.getAskList();
    }

    @Override
    public void updateAction(BidEntity bidEntity) {
        if (bidEntity.getType().equals(Type.Bid)) {
            BidEntity entity = bidList.floor(bidEntity);
            if (entity != null && entity.getPrice() == bidEntity.getPrice()) {
                entity.setSize(entity.getSize() + bidEntity.getSize());
            } else {
                bidList.add(bidEntity);
            }
        } else {
            BidEntity entity = askList.floor(bidEntity);
            if (entity != null && entity.getPrice() == bidEntity.getPrice()) {
                entity.setSize(entity.getSize() + bidEntity.getSize());
            } else {
                askList.add(bidEntity);
            }
        }
    }

    public BidEntity getBestAskEntity() {
        return askList.size() > 0 ? askList.first() : null;
    }

    public BidEntity getBestBidEntity() {
        return bidList.size() > 0 ? bidList.first() : null;
    }

    @Override
    public BidEntity queryAction(String query) {
        BidEntity bidEntity;
        if (query.equals("best_ask")) {
            bidEntity = getBestAskEntity();
        } else if (query.equals("best_bid")) {
            bidEntity = getBestBidEntity();
        } else {
            throw new RuntimeException("Wrong arguments!");
        }
        return bidEntity;
    }

    @Override
    public BidEntity queryAction(int value) {
        BidEntity bidFloor = bidList.floor(new BidEntity(value, 0, null));
        if (bidFloor != null && bidFloor.getPrice() == value) {
            return bidFloor;
        }
        BidEntity askFloor = askList.floor(new BidEntity(value, 0, null));
        if (askFloor != null && askFloor.getPrice() == value) {
            return askFloor;
        }
        return null;
    }

    @Override
    public void orderAction(String act, int value) {
        if (act.equals("buy")) {
            orderChoiceAction(act, value, getBestAskEntity());
        } else if (act.equals("sell")) {
            orderChoiceAction(act, value, getBestBidEntity());
        } else {
            throw new RuntimeException("Wrong arguments!");
        }
    }

    private void orderChoiceAction(String act, int value, BidEntity bestEntity) {
        if (bestEntity != null) {
            switch (bestEntity.getType()) {
                case Bid:
                    actionEntity(act, bestEntity, bestEntity.getSize() - value, bidList);
                    break;
                case Ask:
                    actionEntity(act, bestEntity, bestEntity.getSize() - value, askList);
                    break;
            }
        } else {
            throw new RuntimeException("Transaction canceled or partially completed!");
        }
    }


    private void actionEntity(String act, BidEntity bestEntity, int dif, TreeSet<BidEntity> list) {
        if (dif > 0) {
            bestEntity.setSize(dif);
        } else if (dif == 0) {
            list.remove(bestEntity);
        } else {
            list.remove(bestEntity);
            orderAction(act, -dif);
        }
    }

    @Override
    public String toString() {
        return "Book{" + bidList + '}';
    }

    @Override
    public TreeSet<BidEntity> getBidList() {
        return bidList;
    }

    @Override
    public TreeSet<BidEntity> getAskList() {
        return askList;
    }
}