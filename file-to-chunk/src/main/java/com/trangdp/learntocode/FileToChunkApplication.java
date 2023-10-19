package com.trangdp.learntocode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileToChunkApplication{

    public static void main(String[] args) throws IOException {
        //the idea is that when handling with large file with high performance,
        //we would avoid I/O operations and want to load the file into memory (JVM memory) for better performance.
        //Processing everything in one go may result in OutOfMemoryException
        //The solution is to load partials of the file into memory and process one by one.

        File file = new File("some/path/to/large-file");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();

        //calculate total of chunk files to be split
        int sizeOfChunk = 1024 * 1024 * 10; //10Mb

        int totalChunk = (int) (file.length() / sizeOfChunk);
        if (file.length() % sizeOfChunk > 0) {
            totalChunk += 1;
        }

        //read file by block of bytes
        int offset = 0; // start at the beginning of the file
        ByteBuffer allocate;

        while (offset < file.length()) {
            System.out.println("position:   " + channel.position());

            allocate = ByteBuffer.allocate(sizeOfChunk);

            if(offset > file.length() - sizeOfChunk) {
                // the last chunk size is smaller than the defined chunk size,
                // so we just allocate exact the remaining size
                allocate = ByteBuffer.allocate((int) file.length() - offset);
            }

            offset += channel.read(allocate);
            byte[] chunk = allocate.array(); // this is the chunk file that we targeted
            System.out.println("chunk: " + chunk.length);
        }

        channel.close();
    }
}
