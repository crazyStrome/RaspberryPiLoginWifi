package http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Main {
    static String post = "POST http://auth.dlut.edu.cn/eportal/InterFace.do?method=login HTTP/1.1\n" +
            "Host: auth.dlut.edu.cn\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 671\n" +
            "Origin: http://auth.dlut.edu.cn\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\n" +
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
            "Accept: */*\n" +
            "Referer: http://auth.dlut.edu.cn/eportal/index.jsp?wlanuserip=。。。。\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh-HK;q=0.7,zh;q=0.6\n" +
            "Cookie: EPORTAL_COOKIE_OPERATORPWD=; JSESSIONID=。。。\n" +
            "\n" +
            "userId=账号&password=密码&service=&queryString=wlanuserip%。。。。";

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("auth.dlut.edu.cn", 80));
            socketChannel.write(ByteBuffer.wrap(post.getBytes()));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                String content = Charset.forName("utf-8").decode(buffer).toString();
                println(content);
                buffer.clear();
            }
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static <T> void print(T t) {
        System.out.print(t);
    }
    public static <T> void println(T t) {
        print(t + "\n");
    }
}
