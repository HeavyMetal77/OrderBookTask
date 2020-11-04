package ua.tarastom.service;

import ua.tarastom.dao.IBookDao;
import ua.tarastom.entity.BidEntity;
import ua.tarastom.entity.Type;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceBook implements IService {
    private final IBookDao bookDaoImpl;

    public ServiceBook(IBookDao bookDaoImpl) {
        this.bookDaoImpl = bookDaoImpl;
    }

    @Override
    public void commandLine(String filePath) {
        String resultFile = "result.txt";
        try(InputStream inputStream = new FileInputStream(filePath);
            OutputStream outputStream = new FileOutputStream(resultFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))){

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            String command;
            while ((command = bufferedReader.readLine()) != null) {
                String[] split = command.split(",");
                String typeAction = split[0];
                switch (typeAction) {
                    case "u":
                        executorService.execute(() -> update(split));
                        break;
                    case "q":
                        executorService.execute(() -> {
                            try {
                                bufferedWriter.write(question(split));
                                bufferedWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                                e.getCause();
                            }
                        });
                        break;
                    case "o":
                        executorService.execute(() -> order(split));
                        break;
                    default:
                        bufferedWriter.write("Wrong arguments!");
                        bufferedWriter.flush();
                        break;
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(String[] split) {
        synchronized (this) {
            int price = Integer.parseInt(split[1]);
            int size = Integer.parseInt(split[2]);
            Type convertType = convertType(split[3]);
            BidEntity bidEntity = new BidEntity(price, size, convertType);
            bookDaoImpl.updateAction(bidEntity);
        }
    }

    public String question(String[] split) {
        synchronized (this) {
            String act = split[1];
            String result;
            int size;
            if (split.length == 3) {
                size = Integer.parseInt(split[2]);
                BidEntity bidEntity = bookDaoImpl.queryAction(size);
                result = bidEntity.getSize() + "\n";
            } else if (split.length == 2) {
                BidEntity bidEntity = bookDaoImpl.queryAction(act);
                result = bidEntity.getPrice() + "," + bidEntity.getSize() + "\n";
            } else {
                throw new RuntimeException("Wrong arguments!");
            }
            return result;
        }
    }

    private void order(String[] split) {
        String act = split[1];
        int value = Integer.parseInt(split[2]);
        bookDaoImpl.orderAction(act, value);
    }

    @Override
    public Type convertType(String type) {
        Type theType = null;
        if (type.equals("ask")) {
            theType = Type.Ask;
        }
        if (type.equals("bid")) {
            theType = Type.Bid;
        }
        if (type.equals("spread")) {
            theType = Type.Spread;
        }
        return theType;
    }
}
