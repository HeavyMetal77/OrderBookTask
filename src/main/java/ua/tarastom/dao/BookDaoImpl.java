package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Book;
import ua.tarastom.entity.PriceComparator;
import ua.tarastom.entity.Type;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements IBookDao {
    private final List<BidEntity> bidList;

    public BookDaoImpl(Book book) {
        bidList = book.getBidEntityList();
    }

    @Override
    public void updateAction(BidEntity bidEntity) {
        boolean flag = true;
        for (BidEntity entity : bidList) {
            if (entity.getPrice() == bidEntity.getPrice()) {
                entity.setSize(entity.getSize() + bidEntity.getSize());
                entity.setType(bidEntity.getType());
                flag = false;
                break;
            }
        }
        if (flag) {
            bidList.add(bidEntity);
            bidList.sort(new PriceComparator());
        }
        checkSpreadToAsk();
    }

    private void checkSpreadToAsk() {
        int indexPositionBestAsk = bidList.indexOf(getBestAskBidEntity());
        int indexPositionBestBidEntity = bidList.indexOf(getBestBidEntity());
        for (int i = 0; i < bidList.size(); i++) {
            if (bidList.get(i).getType().equals(Type.Spread) && i < indexPositionBestAsk) {
                bidList.get(i).setType(Type.Ask);
            }
            if (bidList.get(i).getType().equals(Type.Spread) && i > indexPositionBestBidEntity) {
                bidList.get(i).setType(Type.Bid);
            }
        }
    }

    public BidEntity getBestAskBidEntity() {
        BidEntity bidEntity = null;
        Optional<BidEntity> max = bidList.stream().filter(e -> e.getType().equals(Type.Ask)).max(new PriceComparator());
        if (max.isPresent()) {
            bidEntity = max.get();
        }
        return bidEntity;
    }

    public BidEntity getBestBidEntity() {
        BidEntity bidEntity = null;
        Optional<BidEntity> min = bidList.stream().filter(e -> e.getType().equals(Type.Bid)).min(new PriceComparator());
        if (min.isPresent()) {
            bidEntity = min.get();
        }
        return bidEntity;
    }

    @Override
    public BidEntity queryAction(String query) {
        BidEntity bidEntity = null;
        if (query.equals("best_ask")) {
            bidEntity = getBestAskBidEntity();
        }
        if (query.equals("best_bid")) {
            bidEntity = getBestBidEntity();
        }
        return bidEntity;
    }

    @Override
    public BidEntity queryAction(int value) {
        BidEntity bestBidEntity = getBestBidEntity();
        BidEntity bestAskBidEntity = getBestAskBidEntity();
        BidEntity bidEntity = null;
        for (BidEntity entity : bidList) {
            if (entity.getPrice() == value) {
                bidEntity = entity;
                break;
            } else {
                if (value > bestAskBidEntity.getPrice()) {
                    bidEntity = new BidEntity(value, 0, Type.Ask);
                }
                if (value < bestBidEntity.getPrice()) {
                    bidEntity = new BidEntity(value, 0, Type.Bid);
                }
                if (value < bestAskBidEntity.getPrice() && value > bestBidEntity.getPrice()) {
                    bidEntity = new BidEntity(value, 0, Type.Spread);
                }
            }
        }
        return bidEntity;
    }

    @Override
    public void orderAction(String act, int value) {
        BidEntity bestBidEntity = getBestBidEntity();
        BidEntity bestAskBidEntity = getBestAskBidEntity();
        if (act.equals("buy")) {
            int dif = bestAskBidEntity.getSize() - value;
            action(act, bestAskBidEntity, dif);
        }
        if (act.equals("sell")) {
            int dif = bestBidEntity.getSize() - value;
            action(act, bestBidEntity, dif);
        }
        checkSpreadToAsk();
    }

    private void action(String act, BidEntity bestEntity, int dif) {
        if (dif > 0) {
            bestEntity.setSize(dif);
        } else if (dif == 0) {
            bestEntity.setSize(0);
            bestEntity.setType(Type.Spread);
        } else {
            bestEntity.setSize(0);
            bestEntity.setType(Type.Spread);
            checkSpreadToAsk();
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
