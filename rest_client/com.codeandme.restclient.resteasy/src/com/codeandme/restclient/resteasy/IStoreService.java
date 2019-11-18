package com.codeandme.restclient.resteasy;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v2/store")
public interface IStoreService {

	@GET
	@Path("inventory")
	@Produces(MediaType.APPLICATION_JSON)
	Map<String, String> getInventory();

	@POST
	@Path("order")
	@Consumes(MediaType.APPLICATION_JSON)
	void fileOrder(Order order);

	@GET
	@Path("order/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	Order getOrder(@PathParam("orderId") int orderId);

	@DELETE
	@Path("order/{orderId}")
	Response.Status deleteOrder(@PathParam("orderId") int orderId);
}
