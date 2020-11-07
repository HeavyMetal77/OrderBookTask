package ua.tarastom;

import java.io.*;

public class GenerateTestData {
    public static int a = 10000;
    public static int b = 10100;
    public static int c = 10;
    public static int d = 50;

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("test.txt"))));
        for (int i = 0; i < 1000000; i++) {
            bufferedWriter.write(randomOperationString() + "\n");
            bufferedWriter.flush();
        }
    }

    private static String updateString() {
        String[] operation = {"ask", "bid"};
        int rndPrice = (int) (Math.random() * (b - a)) + a;
        int rndCount = (int) (Math.random() * (d - c)) + c;
        int rndOperation = (int) (Math.random() * 2);
        if (rndOperation == 0) {
            rndPrice += 105;
        }
        return "u," + rndPrice + "," + rndCount + "," + operation[rndOperation];
    }

    private static String questionString() {
        String[] operation = {"best_bid", "best_ask", "size"};
        int rndPrice = (int) (Math.random() * ((b + 100) - a)) + a;
        int rndOperation = (int) (Math.random() * 3);
        if (rndOperation == 2) {
            return "q," + operation[rndOperation] + "," + rndPrice;
        } else {
            return "q," + operation[rndOperation];
        }
    }

    private static String orderString() {
        int end = d;
        String[] operation = {"buy", "sell"};
        int rndCount = (int) (Math.random() * (end - c)) + c;
        int rndOperation = (int) (Math.random() * 2);
        return "o," + operation[rndOperation] + "," + rndCount;
    }

    private static String randomOperationString() {
        int rndOperation = (int) (Math.random() * 3);
        String operation = "";
        switch (rndOperation) {
            case 0:
                operation = updateString();
                break;
            case 1:
                operation = questionString();
                break;
            case 2:
                operation = orderString();
                break;
        }
        return operation;
    }
}