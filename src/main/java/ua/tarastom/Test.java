package ua.tarastom;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("test.txt"))));
        for (int i = 0; i < 1000000; i++) {
            bufferedWriter.write("u,9,1,bid\n" +
                    "u,11,5,ask\n" +
                    "q,best_bid\n" +
                    "u,10,2,bid\n" +
                    "q,best_bid\n" +
                    "o,sell,1\n" +
                    "q,size,10\n");
            bufferedWriter.flush();
        }
    }
}
//u,9,1,bid
//u,11,5,ask
//q,best_bid
//u,10,2,bid
//q,best_bid
//o,sell,1
//q,size,10