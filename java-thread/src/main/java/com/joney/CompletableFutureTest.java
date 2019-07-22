package com.joney;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureTest {
	
	public static void main(String[] args) throws Exception {
		CompletableFutureTest.thenApply();
	}

	//�޷���ֵ
	public static void runAsync() throws Exception {
	    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	        try {
	            TimeUnit.SECONDS.sleep(1);
	        } catch (InterruptedException e) {
	        }
	        System.out.println("run end ...");
	    });
	    
	    future.get();
	}

	//�з���ֵ
	public static void supplyAsync() throws Exception {         
	    CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
	        try {
	            TimeUnit.SECONDS.sleep(1);
	        } catch (InterruptedException e) {
	        }
	        System.out.println("run end ...");
	        return System.currentTimeMillis();
	    });

	    long time = future.get();
	    System.out.println("time = "+time);
	}
	
	/**
	 * CompletableFuture�ļ�������ɣ������׳��쳣��ʱ�򣬿���ִ���ض���Action
	 * whenComplete �� whenCompleteAsync ������
	   whenComplete����ִ�е�ǰ������߳�ִ�м���ִ�� whenComplete ������
	   whenCompleteAsync����ִ�а� whenCompleteAsync �����������ύ���̳߳�������ִ��
	 * @throws Exception
	 */
	public static void whenComplete() throws Exception {
	    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	        try {
	            TimeUnit.SECONDS.sleep(3);
	        } catch (InterruptedException e) {
	        }
//	        if(new Random().nextInt()%2>=0) {
//	            int i = 12/0;
//	        }
	        System.out.println("run end ...");
	    });
	    
	    future.whenComplete(new BiConsumer<Void, Throwable>() {
	        @Override
	        public void accept(Void t, Throwable action) {
	            System.out.println("ִ����ɣ�");
	        }
	        
	    });
	    future.exceptionally(new Function<Throwable, Void>() {
	        @Override
	        public Void apply(Throwable t) {
	            System.out.println("ִ��ʧ�ܣ�"+t.getMessage());
	            return null;
	        }
	    });
	    System.out.println("���߳�ִ��ǰ��ȡ���");
	    future.get();
	    System.out.println("���߳�ִ�н���");
	    //TimeUnit.SECONDS.sleep(2);
	}
	
	/**
	 * ��һ���߳�������һ���߳�ʱ������ʹ�� thenApply ���������������̴߳��л�
	 * �ڶ�������������һ������Ľ��
	 * @throws Exception
	 */
	private static void thenApply() throws Exception {
	    CompletableFuture<Long> future = CompletableFuture.supplyAsync(new Supplier<Long>() {
	        @Override
	        public Long get() {
	            long result = new Random().nextInt(100);
	            System.out.println("result1="+result);
	            return result;
	        }
	    }).thenApply(new Function<Long, Long>() {
	        @Override
	        public Long apply(Long t) {
	        	System.out.println("t=" + t);
	            long result = t*5;
	            System.out.println("result2="+result);
	            return result;
	        }
	    });
	    System.out.println("���߳�ִ��ǰ��ȡ���");
	    long result = future.get();
	    System.out.println(result);
	    System.out.println("���߳�ִ�н���");
	}
	
	/**
	 * thenCombine ��� ���� CompletionStage ������ִ����ɺ󣬰���������Ľ��һ�齻�� thenCombine ������
	 * @throws Exception
	 */
	private static void thenCombine() throws Exception {
		long startTime = System.currentTimeMillis();
	    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
	        @Override
	        public String get() {
	        	try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            return "hello";
	        }
	    });
	    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
	        @Override
	        public String get() {
	        	try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            return "hello";
	        }
	    });
	    CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
	        @Override
	        public String apply(String t, String u) {
	            return t+" "+u;
	        }
	    });
	    Long endTime = System.currentTimeMillis();
	    System.out.println("��ʱ��" + (endTime - startTime) + "ms,���:" + result.get());
	}
}
