package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;

import java.util.List;

public interface IBookDao {

    void updateAction(BidEntity bidEntity);

    BidEntity queryAction(String query);

    BidEntity queryAction(int value);

    void orderAction(String act, int value);

    List<BidEntity> getBidList();
}
