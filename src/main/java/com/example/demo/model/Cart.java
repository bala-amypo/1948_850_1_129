package com.example.demo.model;

public class Cart {

    private Long id;
    private Long userId;
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
