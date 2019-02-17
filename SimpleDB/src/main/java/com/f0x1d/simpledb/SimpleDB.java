package com.f0x1d.simpledb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleDB {

    private File db;
    private BufferedWriter writer;

    public SimpleDB(File db){
        this.db = db;

        try {
            writer = new BufferedWriter(new FileWriter(db, true));
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();

        Scanner scanner = new Scanner(db);
        while (scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }

        return lines;
    }

    public String readAllDB() throws IOException {
        String all = "";

        Scanner scanner = new Scanner(db);
        while (scanner.hasNextLine()){
            if (all.isEmpty())
                all = scanner.nextLine();
            else
                all = all + "\n" + scanner.hasNext();
        }

        return all;
    }

    public void addLine(String text) throws IOException {
        if (!readAllDB().isEmpty())
            writer.newLine();

        writer.write(text);
        writer.flush();
    }

    public void removeLine(String text) throws IOException {
        String all = "";

        for (String line : readLines()){
            if (!line.equals(text)){
                if (all.equals(""))
                    all = line;
                else
                    all = all + "\n" + line;
            }
        }

        BufferedWriter writerDelete = new BufferedWriter(new FileWriter(db));
        writerDelete.write(all);
        writerDelete.flush();
        writerDelete.close();
    }

    public void clearDB() throws IOException {
        BufferedWriter writerDelete = new BufferedWriter(new FileWriter(db));
        writerDelete.write("");
        writerDelete.flush();
        writerDelete.close();
    }
}