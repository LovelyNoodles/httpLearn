package org.httpClientLearn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

public class HTTPAsyncClientDemo {
	public static void main(String[] args) throws IOReactorException, InterruptedException, ExecutionException {
		// 具体参数含义下文会讲
		// apache提供了ioReactor的参数配置，这里我们配置IO 线程为1
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(1).build();

		// 根据这个配置创建一个ioReactor
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

		// asyncHttpClient使用PoolingNHttpClientConnectionManager管理我们客户端连接
		PoolingNHttpClientConnectionManager conManager = new PoolingNHttpClientConnectionManager(ioReactor);

		// 设置总共的连接的最大数量
		conManager.setMaxTotal(100);

		// 设置每个路由的连接的最大数量
		conManager.setDefaultMaxPerRoute(100);

		// 创建一个Client
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setConnectionManager(conManager).build();

		// Start the client
		httpclient.start();

		// Execute request
		final HttpGet request1 = new HttpGet("http://www.apache.org/");

		Future<HttpResponse> future = httpclient.execute(request1, null);

		// and wait until a response is received
		HttpResponse response1 = future.get();

		System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());

		// One most likely would want to use a callback for operation result
		final HttpGet request2 = new HttpGet("http://www.apache.org/");

		httpclient.execute(request2, new FutureCallback<HttpResponse>() {

			// Complete成功后会回调这个方法
			public void completed(final HttpResponse response2) {
				System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
			}

			public void failed(final Exception ex) {
				System.out.println(request2.getRequestLine() + "->" + ex);
			}

			public void cancelled() {
				System.out.println(request2.getRequestLine() + " cancelled");
			}

		});
		
		System.out.println("end ...");
	}
}
