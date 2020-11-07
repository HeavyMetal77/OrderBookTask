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
    public void commandLine(String testFile, String resultFile) {
        try (InputStream inputStream = new FileInputStream(testFile);
             OutputStream outputStream = new FileOutputStream(resultFile);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            String command;
            while ((command = bufferedReader.readLine()) != null) {
                String[] split = command.split(",");
                switch (split[0]) {
                    case "u":
                        executorService.execute(() -> {
                            try {
                                update(split);
                            } catch (Exception e) {
                                try {
                                    bufferedWriter.write(e.getMessage() + "\n");
                                    bufferedWriter.flush();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                        });
//                        Thread.sleep(300);
//                        System.out.println("update: " + Arrays.toString(split)+ bookDaoImpl.getBidList());
                        break;
                    case "q":
                        executorService.execute(() -> {
                            try {
                                bufferedWriter.write(question(split));
                                bufferedWriter.flush();
                            } catch (IOException e) {
                                try {
                                    bufferedWriter.write(e.getMessage() + "\n");
                                    bufferedWriter.flush();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                        });
//                        Thread.sleep(300);
//                        System.out.println("question: " + Arrays.toString(split)+ bookDaoImpl.getBidList() + " best: " + question(split));
                        break;
                    case "o":
                        executorService.execute(() -> {
                            try {
                                order(split);
                            } catch (Exception e) {
                                try {
                                    bufferedWriter.write(e.getMessage() + "\n");
                                    bufferedWriter.flush();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                        });
//                        Thread.sleep(300);
//                        System.out.println("order: " + Arrays.toString(split)+ bookDaoImpl.getBidList());
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
            int price, size;
            try {
                price = Integer.parseInt(split[1]);
                size = Integer.parseInt(split[2]);
            } catch (Exception e) {
                throw new RuntimeException("Wrong arguments!");
            }
            Type convertType = convertType(split[3]);
            if (convertType != null) {
                BidEntity bidEntity = new BidEntity(price, size, convertType);
                bookDaoImpl.updateAction(bidEntity);
            } else {
                throw new RuntimeException("Wrong arguments!");
            }
        }
    }

    public String question(String[] split) {
        synchronized (this) {
            String act = split[1];
            String result;
            int size;
            if (split.length == 3) {
                try {
                    size = Integer.parseInt(split[2]);
                } catch (Exception e) {
                    throw new RuntimeException("Wrong arguments!");
                }
                BidEntity bidEntity = bookDaoImpl.queryAction(size);
                if (bidEntity != null) {
                    result = bidEntity.getSize() + "\n";
                } else {
                    result = "0\n";
                }
            } else if (split.length == 2) {
                BidEntity bidEntity;
                try {
                    bidEntity = bookDaoImpl.queryAction(act);
                } catch (Exception e) {
                    return e.getMessage();
                }
                if (bidEntity != null) {
                    result = bidEntity.getPrice() + "," + bidEntity.getSize() + "\n";
                } else {
                    result = "This item is not currently available.\n";
                }
            } else {
                throw new RuntimeException("Wrong arguments!");
            }
            return result;
        }
    }

    private void order(String[] split) {
        synchronized (this) {
            int value;
            try {
                value = Integer.parseInt(split[2]);
            } catch (Exception e) {
                throw new RuntimeException("Wrong arguments!");
            }
            bookDaoImpl.orderAction(split[1], value);
        }
    }

    @Override
    public Type convertType(String type) {
        Type theType;
        if (type.equals("ask")) {
            theType = Type.Ask;
            return theType;
        }
        if (type.equals("bid")) {
            theType = Type.Bid;
            return theType;
        }
        if (type.equals("spread")) {
            theType = Type.Spread;
            return theType;
        }
        return null;
    }
}
