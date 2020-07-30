package com.cllover.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class NoBlockingNIO {
    public static void main(String[] args) throws IOException {
        //获取channel通道   并设置主机号和端口号
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));

        //因为使用非阻塞NIO  所以必须切换为非阻塞
        socketChannel.configureBlocking(false);

        //开辟缓冲区进行存储数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //附加输入：
        Scanner scanner = new Scanner(System.in);
        //通过控制台键入数据
        while (scanner.hasNext()){
            String str = scanner.next();
            //准备工作就绪后，准备发送数据给服务端
            //打印当前日期转为Byte数据传出
            byteBuffer.put((new Date().toString()+":--->"+str).getBytes());
            //切换读写模式
            byteBuffer.flip();
            //写入通道
            socketChannel.write(byteBuffer);
            //完毕时，清除缓冲区内容
            byteBuffer.clear();
        }

    }

    /*
    * 客户端发送数据 通过channel通道
    * */
    @Test
    public void Client() throws IOException {

        //获取channel通道   并设置主机号和端口号
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));

        //因为使用非阻塞NIO  所以必须切换为非阻塞
        socketChannel.configureBlocking(false);

        //开辟缓冲区进行存储数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //准备工作就绪后，准备发送数据给服务端
        //打印当前日期转为Byte数据传出
        byteBuffer.put(new Date().toString().getBytes());
        //切换读写模式
        byteBuffer.flip();
        //写入通道
        socketChannel.write(byteBuffer);
        //完毕时，清除缓冲区内容
        byteBuffer.clear();
        //关闭相关流
        socketChannel.close();

    }

    /*
    * 服务端接收客户端传来的数据
    * */
    @Test
    public void server() throws IOException {

        //获取channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //切换为非堵塞状态
        serverSocketChannel.configureBlocking(false);
        //分配服务端的缓冲区
        ByteBuffer serverByteBuffer = ByteBuffer.allocate(1024);
        //将客户端的InetSocketAddress绑定到通道，不绑定 不统一将获取不到数据
        serverSocketChannel.bind(new InetSocketAddress(8080));
        //获取选择器
        Selector selector = Selector.open();
        //将通道注册到选择器中,并且制定监听方式
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //进行轮询选择器上就绪成功的事件  当存在就绪成功的及进行下一步
        while (selector.select() > 0){
            //对已存在的就绪事件进行迭代
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();

            //有元素就进行下一步
            while (selectionKeyIterator.hasNext()){
                //获取到就绪事件
                SelectionKey next = selectionKeyIterator.next();

                //对获取到的就绪事件判断是何种类型
                if (next.isAcceptable()){

                    //获取连接
                    SocketChannel accept = serverSocketChannel.accept();

                    //将获取到的连接切换为非堵塞模式
                    accept.configureBlocking(false);

                    //将获取到的链接 注册金selector
                    accept.register(selector,SelectionKey.OP_READ);

                    //判断是否准备好读
                }else if (next.isReadable()){

                    //获取已就绪的通道
                    SocketChannel channel = (SocketChannel) next.channel();

                    //分配缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    //读取数据
                    int length = 0 ;
                    while ((length = channel.read(byteBuffer)) > 0){
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0,length));
                        byteBuffer.clear();
                    }


                }

                //完成传输需要取消选择键，防止下次出问题
                selectionKeyIterator.remove();

            }
        }


    }

}