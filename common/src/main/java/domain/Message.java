package domain;

import java.io.*;

public class Message {
    public static final int PORT = 1234;
    public static final String HOST = "localhost";

    private String header;
    private String body;
    private int numberOfLines=1;

    public Message() {
    }

    public Message(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public Message(String header, String body, int lines) {
        this.header = header;
        this.body = body;
        numberOfLines = lines;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write((header + System.lineSeparator() + body + System.lineSeparator()).getBytes());
    }

    public void readFrom(InputStream is) throws IOException {
        var br = new BufferedReader(new InputStreamReader(is));
        header = br.readLine();
        body =br.readLine();
    }

    @Override
    public String toString() {
        return "Message{" +
                "header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
