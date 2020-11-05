package ua.tarastom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import ua.tarastom.dao.BookDaoImpl;
import ua.tarastom.dao.IBookDao;
import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Book;
import ua.tarastom.entity.Type;
import ua.tarastom.service.ServiceBook;

import java.io.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private IBookDao bookDaoImpl;
    private ServiceBook serviceBook;
    BidEntity bidEntity1;
    BidEntity bidEntity2;
    BidEntity bidEntity3;
    BidEntity bidEntity4;
    BidEntity bidEntity5;
    BufferedReader bufferedReader;
    String resultFile;
    String appTest;

    @Before
    public void setUp() {
        bookDaoImpl = new BookDaoImpl(new Book());
        serviceBook = new ServiceBook(bookDaoImpl);
        bidEntity1 = new BidEntity(8, 7, Type.Bid);
        bidEntity2 = new BidEntity(9, 1, Type.Ask);
        bidEntity3 = new BidEntity(6, 3, Type.Bid);
        bidEntity4 = new BidEntity(10, 5, Type.Ask);
        bidEntity5 = new BidEntity(7, 4, Type.Bid);
        resultFile = "result.txt";
        appTest = "appTest.txt";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resultFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initTestData(String data) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(appTest)));
            bufferedWriter.write(data);
            bufferedWriter.flush();
            new BufferedReader(new InputStreamReader(new FileInputStream(resultFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder resultTestData() {
        StringBuilder resultString = new StringBuilder();
        String s;
        boolean flag = true;
        while (flag) {
            try {
                if (bufferedReader == null || (s = bufferedReader.readLine()) == null) {
                    flag = false;
                } else {
                    resultString.append(s).append("\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    @Test
    public void testUpdateBook() {
        //given

        //when
        bookDaoImpl.updateAction(bidEntity1);
        bookDaoImpl.updateAction(bidEntity2);
        bookDaoImpl.updateAction(bidEntity3);
        bookDaoImpl.updateAction(bidEntity4);
        bookDaoImpl.updateAction(bidEntity5);

        //then
        assertEquals("Book{[{10, 5, Ask}, {9, 1, Ask}, {8, 7, Bid}, {7, 4, Bid}, {6, 3, Bid}]}", bookDaoImpl.toString());
    }

    @Test
    public void testQueryBestAsk() {
        //given

        //when
        bookDaoImpl.updateAction(bidEntity1);
        bookDaoImpl.updateAction(bidEntity2);
        bookDaoImpl.updateAction(bidEntity3);
        bookDaoImpl.updateAction(bidEntity4);
        bookDaoImpl.updateAction(bidEntity5);
        BidEntity bidEntity = bookDaoImpl.queryAction("best_ask");

        //then
        assertEquals("{9, 1, Ask}", bidEntity.toString());
    }

    @Test
    public void testQueryBestBid() {
        //given

        //when
        bookDaoImpl.updateAction(bidEntity1);
        bookDaoImpl.updateAction(bidEntity2);
        bookDaoImpl.updateAction(bidEntity3);
        bookDaoImpl.updateAction(bidEntity4);
        bookDaoImpl.updateAction(bidEntity5);
        BidEntity bidEntity = bookDaoImpl.queryAction("best_bid");

        //then
        assertEquals("{8, 7, Bid}", bidEntity.toString());
    }

    @Test
    public void testQuerySize() {
        //given

        //when
        bookDaoImpl.updateAction(bidEntity1);
        bookDaoImpl.updateAction(bidEntity2);
        bookDaoImpl.updateAction(bidEntity3);
        bookDaoImpl.updateAction(bidEntity4);
        bookDaoImpl.updateAction(bidEntity5);
        int size1 = 9;
        int size2 = 8;
        BidEntity bidEntity1 = bookDaoImpl.queryAction(size1);
        BidEntity bidEntity2 = bookDaoImpl.queryAction(size2);

        //then
        assertEquals("{9, 1, Ask}", bidEntity1.toString());
        assertEquals("{8, 7, Bid}", bidEntity2.toString());
    }

    @Test
    public void testServiceBookNormal() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" + "10,2\n" + "1\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookQueryBest() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "q,best_bid\n" +
                "q,best_ask\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "10,1\n" +
                "11,5\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookUpdateBid() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "q,best_ask\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "10,3\n" +
                "11,5\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookUpdateAsk() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "q,best_ask\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "10,3\n" +
                "11,10\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookOrderBidMore() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,10,2,bid\n" +
                "o,sell,3\n" +
                "q,best_bid\n" +
                "q,best_ask\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "9,1\n" +
                "11,10\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookOrderAskMore() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,10,2,bid\n" +
                "o,sell,3\n" +
                "q,best_bid\n" +
                "q,best_ask\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "o,buy,1\n" +
                "q,best_ask\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "9,1\n" +
                "11,10\n" +
                "11,1\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookOrderAskNull() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,5,ask\n" +
                "u,10,2,bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,10,2,bid\n" +
                "o,sell,3\n" +
                "q,best_bid\n" +
                "q,best_ask\n" +
                "o,buy,10\n" +
                "o,sell,1\n" +
                "q,best_ask\n" +
                "q,best_bid\n" +
                "q,size,10\n" +
                "o,buy,1\n" +
                "o,sell,1\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("1\n" +
                "9,1\n" +
                "11,10\n" +
                "This item is not currently available.\n" +
                "This item is not currently available.\n" +
                "0\n" +
                "Transaction canceled!\n" +
                "Transaction canceled!\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookEmptyBook() {
        //given
        initTestData("q,size,10\n" +
                "q,best_bid\n" +
                "q,best_ask\n" +
                "o,buy,1\n" +
                "o,sell,1\n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("0\n" +
                "This item is not currently available.\n" +
                "This item is not currently available.\n" +
                "Transaction canceled!\n" +
                "Transaction canceled!\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookWrongArguments() {
        //given
        initTestData("u, \n" +
                "o, \n" +
                "q, \n");

        //when
        serviceBook.commandLine(appTest);

        //then
        assertEquals("Wrong arguments!\n" +
                "Wrong arguments!\n" +
                "Wrong arguments!\n", resultTestData().toString());
    }
}
