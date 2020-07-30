package com.cllover.nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * @Author chengpunan
 * @Description //TODO 18609
 * @Date 7:29 PM 7/25/2020
 * @Param 
 * @return
 *
 * 分散读写和聚集读写测试类
 * Disperse：分散读写
 * gather： 聚集读写
 **/
public class DisperseAndGather {

//    分散读写  将通道中的数据分散到缓冲区中，
    @Test
    public void Disperse(){
        RandomAccessFile randomAccessFile = null;
        FileChannel randomAccessFileChannel = null;

        try {
            //随机访问文件流以RW形式访问某文件
            randomAccessFile = new RandomAccessFile("D:/DataTestFile/1.txt","rw");

            //获取通道
            randomAccessFileChannel = randomAccessFile.getChannel();

            //开辟两个缓冲区
            ByteBuffer byteBuffer100 = ByteBuffer.allocate(100);
            ByteBuffer byteBuffer200 = ByteBuffer.allocate(200);

            //分散读取
            ByteBuffer[] byteBuffer = {byteBuffer100,byteBuffer200};

            //存入读取通道
            randomAccessFileChannel.read(byteBuffer);

            for (ByteBuffer bf: byteBuffer) {
                bf.flip();   //切换读写方式
            }
            //转换成字符串
            //public String(byte bytes[], int offset, int length)
            System.out.println(new String(byteBuffer[0].array(),0,byteBuffer[0].limit()));
            System.out.println("----------------------------------");
            System.out.println(new String(byteBuffer[1].array(),0,byteBuffer[1].limit()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFileChannel != null){
                try {
                    randomAccessFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}