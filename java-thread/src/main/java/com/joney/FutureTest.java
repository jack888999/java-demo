package com.joney;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureTest {

	final static ExecutorService executor = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		RpcService rpcService = new RpcService();
		HttpService httpService = new HttpService();
		Future<BigDecimal> future1 = null;
		Future<Integer> future2 = null;
		try {
			long startTime = System.currentTimeMillis();
			future1 = executor.submit(() -> rpcService.getRpcResut());
			future2 = executor.submit(() -> httpService.getHttpResult());
			BigDecimal result1 = future1.get(6000, TimeUnit.MILLISECONDS);
			System.out.println("result1:" + result1);
			Integer result2 = future2.get(6000, TimeUnit.MILLISECONDS);
			System.out.println("result2:" + result2);
			long endTime = System.currentTimeMillis();
			System.out.println("耗时:" + (endTime - startTime) + "ms");
		} catch (Exception e) {
			if(future1 != null) {
				future1.cancel(true);
			}
			if(future2 != null) {
				future2.cancel(true);
			}
			throw new RuntimeException(e);
		}
		System.out.println("运行结束");
	}
	
	static class RpcService {
		BigDecimal getRpcResut() throws Exception {
			Thread.sleep(1000);
			return BigDecimal.ZERO;
		}
	}
	
	static class HttpService {
		Integer getHttpResult() throws Exception {
			Thread.sleep(4000);
			return 6;
		}
	}
}
