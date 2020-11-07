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
//2369 2345
//2299 2274 2326 2477 2331 2370

//3219 3330 2385

//2442 2358 2382 2366 2308 2320 2268 2360 2318 2310 2330 2324 2285 2310 2204 2246 2293