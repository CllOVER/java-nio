package com.cllover.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
/*
 * @Author chenqiang
 * @Description //TODO 18609
 * @Date 11:10 AM 7/25/2020
 * @Param 
 * @return  
        **/

public class ChannelTest {

    /*
    *  直接缓冲区
    * 使用allocateDriect方法操作直接缓冲区
    * */

    @Test
    public void AllocateDriect(){

    }
    /*
    * 通过直接缓冲区的方式进行传输数据
    * 通过使用映射文件的方式MappedByteBuffer传输
    * */
    @Test
    public void Driect(){

        FileChannel fileInChannel = null;
        FileChannel fileOutChannel = null;
        MappedByteBuffer mappedInByteBuffer = null;
        MappedByteBuffer mappedOutByteBuffer = null;
        long start = 0;

        try {
            //耗时查询
             start = System.currentTimeMillis();

             fileInChannel = FileChannel.open(Paths.get("D:/DataTestFile/uml.avi"), StandardOpenOption.READ,StandardOpenOption.WRITE);
             fileOutChannel = FileChannel.open(Paths.get("D:/DataTestFile/um1l1.avi"), StandardOpenOption.READ, StandardOpenOption.WRITE,StandardOpenOption.CREATE);

            //使用内存映射文件 ，杜绝非直接缓存区中的通过用户态 和核心态的相互复制影响性能的问题
            //直接通过本地映射文件。但是一旦传输后，不归程序所管理
             mappedInByteBuffer = fileInChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileInChannel.size());
             mappedOutByteBuffer = fileOutChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileInChannel.size());

            //直接对缓冲区进行读写操作
            //定义一个数组，将映射文件的数据存入其中
            byte[] bt = new byte[mappedInByteBuffer.limit()];
            //先从本地映射文件中读取  后写入传输
            mappedInByteBuffer.get(bt);
            mappedOutByteBuffer.put(bt);


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fileInChannel != null){
                try {
                    fileInChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutChannel != null){
                try {
                    fileOutChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //耗时结束查询
            long end = System.currentTimeMillis();
            System.out.println("总共耗时："+(end-start));

        }


    }
    //使用非直接缓冲区 传输数据
    //通过流的方式
    @Test
    public void NoDirect(){

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream =null;
        FileChannel fileInputStreamChannel = null;
        FileChannel fileOutputStreamChannel = null;
        long start = 0;

        try {
            //查看耗时
             start = System.currentTimeMillis();

            //写入
            fileInputStream = new FileInputStream("D:/DataTestFile/1.iso");
            //读取到
            fileOutputStream = new FileOutputStream("D:/DataTestFile/2.iso");

            //使用通道进行文件传输
            //在传输前先获取传输通道channel
             fileInputStreamChannel = fileInputStream.getChannel();
             fileOutputStreamChannel = fileOutputStream.getChannel();

            //将数据写入缓冲区 -->   开辟缓冲区容量  --> 后使用通道传输
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //将通道中的数据送入缓冲区  数据为空时跳出循环
            while(fileInputStreamChannel.read(buf) != -1){
                //切换读写模式
                buf.flip();
                //读出缓冲区的数据写入通道中
                fileOutputStreamChannel.write(buf);
                //清空缓冲区
                buf.clear();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //最终需要执行的步骤 需要关闭流  和通道
        finally {
            //写入流关闭
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //读出流关闭
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           if (fileInputStreamChannel != null){
               try {
                   fileInputStreamChannel.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
            //读出通道关闭
            if (fileOutputStreamChannel != null){
                try {
                    fileOutputStreamChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //查看耗时
            long end = System.currentTimeMillis();
            System.out.println("总共耗时："+(end-start));
        }


    }


}
