package com.test.rest;

import com.test.dao.TestBeanDAO;
import com.test.data.TestBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/test")
@Produces("application/json;charset=utf-8")
@Api(value = "test", description = "Test service")
public class TestResource {

    private TestBeanDAO testBeanDAO;

    public TestResource() {
        this.testBeanDAO = new TestBeanDAO();
    }

    @GET
    @ApiOperation("list test objects")
    public Response list() {
        return Response.ok(this.testBeanDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation("get test object")
    public Response get(@PathParam("id") Long id) {
        TestBean bean = this.testBeanDAO.get(id);
        if (bean == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(bean).build();
    }

    @POST
    @Consumes("application/json;charset=utf-8")
    @ApiOperation("save test object")
    public Response save(TestBean bean) {
        this.testBeanDAO.save(bean);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("delete test object")
    public Response delete(@PathParam("id") Long id) {
        TestBean bean = this.testBeanDAO.get(id);
        if (bean == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.testBeanDAO.delete(bean);
        return Response.ok().build();
    }
}
