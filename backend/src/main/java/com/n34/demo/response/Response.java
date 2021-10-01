package com.n34.demo.response;

public class Response {
    private Status status;
    private Object payload;

    public Response(Status status) {
        this.status = status;
    }

    public Response() {
    }

    public Response(Status status, Object payload) {
        this.status = status;
        this.payload = payload;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
