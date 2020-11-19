package ua.tarastom;

import org.junit.Before;
import org.junit.Test;
import ua.tarastom.dao.BookDaoImpl;
import ua.tarastom.dao.IBookDao;
import ua.tarastom.entity.Book;
import ua.tarastom.service.ServiceBook;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private ServiceBook serviceBook;
    BufferedReader bufferedReader;
    String resultFile;
    String appTest;

    @Before
    public void setUp() {
        IBookDao bookDaoImpl = new BookDaoImpl(new Book());
        serviceBook = new ServiceBook(bookDaoImpl);
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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

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
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("1\n" +
                "9,1\n" +
                "11,10\n" +
                "This item is not currently available.\n" +
                "This item is not currently available.\n" +
                "0\n" +
                "Transaction canceled or partially completed!\n" +
                "Transaction canceled or partially completed!\n", resultTestData().toString());
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
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("0\n" +
                "This item is not currently available.\n" +
                "This item is not currently available.\n" +
                "Transaction canceled or partially completed!\n" +
                "Transaction canceled or partially completed!\n", resultTestData().toString());
    }

    @Test
    public void testServiceBookWrongArguments() {
        //given
        initTestData("u, \n" +
                "o, \n" +
                "q, \n");

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("Wrong arguments!\n" +
                "Wrong arguments!\n" +
                "Wrong arguments!\n", resultTestData().toString());
    }

    @Test
    public void testServiceInitialData() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,12,5,ask\n" +
                "u,7,5,bid\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,9,0,bid\n" +
                "u,11,0,ask\n" +
                "q,best_bid\n" +
                "q,best_ask\n"
        );

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("9,1\n" +
                "10,2\n" +
                "1\n" +
                "10,1\n" +
                "11,5\n", resultTestData().toString());
    }

    @Test
    public void testServiceConvertType() {
        //given
        initTestData("u,9,1,wrong\n" +
                "u,11,0,wrong\n"
        );

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("Wrong arguments!\n" +
                "Wrong arguments!\n", resultTestData().toString());
    }

    @Test
    public void testServiceEmptySplit() {
        //given
        initTestData("\n");

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("Wrong arguments!\n", resultTestData().toString());
    }

    @Test
    public void testServiceOrderBuy() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,12,5,ask\n" +
                "u,7,5,bid\n" +
                "u,10,2,bid\n" +
                "u,9,0,bid\n" +
                "u,11,0,ask\n" +
                "o,sell,10\n" +
                "o,buy,11\n"
        );

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("Transaction canceled or partially completed!\n" +
                "Transaction canceled or partially completed!\n", resultTestData().toString());
    }

    @Test
    public void testServiceQueryAction() {
        //given
        initTestData("u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,13,4,ask\n" +
                "u,14,6,ask\n" +
                "u,7,5,bid\n" +
                "u,9,4,bid\n" +
                "u,10,2,bid\n" +
                "q,size,12\n" +
                "q,size,10\n" +
                "q,size,9\n" +
                "q,size,11\n" +
                "q,best_bid\n" +
                "q,best_ask\n"
        );

        //when
        serviceBook.commandLine(appTest, resultFile);

        //then
        assertEquals("0\n" +
                "2\n" +
                "5\n" +
                "5\n" +
                "10,2\n" +
                "11,5\n", resultTestData().toString());
    }
}
