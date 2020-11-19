package ua.tarastom;

import ua.tarastom.dao.BookDaoImpl;
import ua.tarastom.dao.IBookDao;
import ua.tarastom.entity.Book;
import ua.tarastom.service.IService;
import ua.tarastom.service.ServiceBook;

public class App {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        IService serviceBook = new ServiceBook(new BookDaoImpl(new Book()));
        serviceBook.commandLine("test.txt", "result.txt");
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}