package com.cllover.nio;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

/*
 * @Author chenqiang
 * @Description //TODO 18609
 * @Date 11:10 AM 7/25/2020
 * @Param 
 * @return  
        **/

public class NIO {
    public static void main(String[] args) {
        System.out.println("-------------allocate开辟-------------");
        //给缓冲区开辟指定大小的区域
        ByteBuffer bf = ByteBuffer.allocate(1024);

        //存储位置
        System.out.println("position:---"+bf.position());
        //存储限制大小
        System.out.println("limit:---"+bf.limit());
        //存储容量
        System.out.println("capacity:---"+bf.capacity());

        //为缓存区中存入数据
        System.out.println("-------------put写入-------------");
        //定义一个数据
        String str= "123456";
        bf.put(str.getBytes()); //将结果存储到新的字节数组中。
        System.out.println("position:---"+bf.position());
        System.out.println("limit:---"+bf.limit());
        System.out.println("capacity:---"+bf.capacity());


        //写入 后切换为读取状态
        System.out.println("-------------flip切换-------------");
        /*
        * 将limit = position
        *  令 position  = 0
        * */
        bf.flip();   //切换读取状态
        System.out.println("position:---"+bf.position());
        System.out.println("limit:---"+bf.limit());
        System.out.println("capacity:---"+bf.capacity());

        //切换成功后进行读取
        System.out.println("-------------get读取-------------");
        /*
        * 从零开始读 ，读6个长度停止
        * */
        bf.get(str.getBytes(), 0, 6);
        System.out.println("position:---"+bf.position());
        System.out.println("limit:---"+bf.limit());
        System.out.println("capacity:---"+bf.capacity());

        //清空缓存区
        System.out.println("-------------rewind重复度-------------");

        bf.rewind();
        System.out.println("position:---"+bf.position());
        System.out.println("limit:---"+bf.limit());
        System.out.println("capacity:---"+bf.capacity());

        //清空缓存区
        System.out.println("-------------clear清空-------------");

        bf.clear();
        System.out.println("position:---"+bf.position());
        System.out.println("limit:---"+bf.limit());
        System.out.println("capacity:---"+bf.capacity());
        //查看实际是否存在数据，并未被清除，只是被遗忘了。
        System.out.println((char)bf.get(0));
        System.out.println((char)bf.get(1));
    }


}
