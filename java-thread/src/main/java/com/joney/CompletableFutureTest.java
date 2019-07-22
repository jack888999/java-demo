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

	//无返回值
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

	//有返回值
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
	 * CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action
	 * whenComplete 和 whenCompleteAsync 的区别：
	   whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
	   whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行
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
	            System.out.println("执行完成！");
	        }
	        
	    });
	    future.exceptionally(new Function<Throwable, Void>() {
	        @Override
	        public Void apply(Throwable t) {
	            System.out.println("执行失败！"+t.getMessage());
	            return null;
	        }
	    });
	    System.out.println("主线程执行前获取结果");
	    future.get();
	    System.out.println("主线程执行结束");
	    //TimeUnit.SECONDS.sleep(2);
	}
	
	/**
	 * 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化
	 * 第二个任务依赖第一个任务的结果
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
	    System.out.println("主线程执行前获取结果");
	    long result = future.get();
	    System.out.println(result);
	    System.out.println("主线程执行结束");
	}
	
	/**
	 * thenCombine 会把 两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理
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
	    System.out.println("耗时：" + (endTime - startTime) + "ms,结果:" + result.get());
	}
}
