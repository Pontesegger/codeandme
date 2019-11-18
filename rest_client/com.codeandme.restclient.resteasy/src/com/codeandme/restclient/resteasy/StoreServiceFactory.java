package com.codeandme.restclient.resteasy;

import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

public class StoreServiceFactory {

	public static IStoreService createStoreService() throws URISyntaxException {
		ResteasyClient client = new ResteasyClientBuilderImpl().build();
		ResteasyWebTarget target = client.target(new URI("https://petstore.swagger.io/"));
		return target.proxy(IStoreService.class);
	}

//	public static Object test() throws URISyntaxException {
//		IStoreService storeService = createStoreService();
//		return storeService.getInventory();
//	}
//
//	public static Object test2() throws URISyntaxException {
//		IStoreService storeService = createStoreService();
//		return storeService.getOrder(1);
//	}
//
//	public static void test3() throws URISyntaxException {
//		IStoreService storeService = createStoreService();
//		
//		Order order = new Order();
//		order.setPetId(2);
//		order.setComplete(true);
//		order.setShipDate("2019-11-13T19:35");
//		order.setQuantity(4);
//		order.setStatus("placed");
//		
//		storeService.fileOrder(order);
//	}
}
