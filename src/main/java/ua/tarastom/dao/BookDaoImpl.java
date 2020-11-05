package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Book;
import ua.tarastom.entity.PriceComparator;
import ua.tarastom.entity.Type;

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
        checkSpreadToAskOrBid();
    }

    private void checkSpreadToAskOrBid() {
        for (int i = 0; i < bidList.size(); i++) {
            BidEntity bidCurrentEntity = bidList.get(i);
            if (bidCurrentEntity.getType().equals(Type.Spread)
                    && i < bidList.indexOf(getBestAskBidEntity())
                    && bidCurrentEntity.getSize()>0) {
                bidCurrentEntity.setType(Type.Ask);
            }
            if (bidCurrentEntity.getType().equals(Type.Spread)
                    && i > bidList.indexOf(getBestBidEntity())
                    && bidCurrentEntity.getSize()>0) {
                bidCurrentEntity.setType(Type.Bid);
            }
        }
    }

    public BidEntity getBestAskBidEntity() {
        BidEntity bestAskEntity = null;
        Optional<BidEntity> max = bidList.stream().filter(e -> e.getType().equals(Type.Ask)).max(new PriceComparator());
        if (max.isPresent()) {
            bestAskEntity = max.get();
        }
        return bestAskEntity;
    }

    public BidEntity getBestBidEntity() {
        BidEntity bestBidEntity = null;
        Optional<BidEntity> min = bidList.stream().filter(e -> e.getType().equals(Type.Bid)).min(new PriceComparator());
        if (min.isPresent()) {
            bestBidEntity = min.get();
        }
        return bestBidEntity;
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
        BidEntity bidEntity = null;
        for (BidEntity entity : bidList) {
            if (entity.getPrice() == value) {
                bidEntity = entity;
                break;
            }
        }
        if (bidEntity == null) {
            bidEntity = new BidEntity(value, 0, Type.Spread);
        }
        return bidEntity;
    }

    @Override
    public void orderAction(String act, int value) {
        if (act.equals("buy")) {
            BidEntity bestAskBidEntity = getBestAskBidEntity();
            orderChoiceAction(act, value, bestAskBidEntity);
        }
        if (act.equals("sell")) {
            BidEntity bestBidEntity = getBestBidEntity();
            orderChoiceAction(act, value, bestBidEntity);
        }
        checkSpreadToAskOrBid();
    }

    private void orderChoiceAction(String act, int value, BidEntity bestEntity) {
        if (bestEntity != null) {
            int dif = bestEntity.getSize() - value;
            action(act, bestEntity, dif);
        }else {
            checkSpreadToAskOrBid();
            throw new RuntimeException("Transaction canceled!");
        }
    }

    private void action(String act, BidEntity bestEntity, int dif) {
        if (dif > 0) {
            bestEntity.setSize(dif);
        } else if (dif == 0) {
            bestEntity.setSize(0);
            bestEntity.setType(Type.Spread);
            checkSpreadToAskOrBid();
        } else {
            bestEntity.setSize(0);
            bestEntity.setType(Type.Spread);
            checkSpreadToAskOrBid();
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
