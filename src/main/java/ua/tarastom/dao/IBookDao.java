package ua.tarastom.dao;

import ua.tarastom.entity.BidEntity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface IBookDao {

    void updateAction(BidEntity bidEntity);

    BidEntity queryAction(String query);

    BidEntity queryAction(int value);

    void orderAction(String act, int value);

    TreeSet<BidEntity> getBidList();

    TreeSet<BidEntity> getAskList();
}
