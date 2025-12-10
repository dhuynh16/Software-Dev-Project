package com.companyz.ems.security;

import com.companyz.ems.user.User;
import java.time.Instant;
import java.util.UUID;

public class Session {
    private final String id;
    private final User user;
    private final Instant createdAt;
    private Instant lastAccess;

    public Session(User user) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.createdAt = Instant.now();
        this.lastAccess = createdAt;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public UserRole getRole() { return user.getRole(); }

    public void touch() { lastAccess = Instant.now(); }

    public boolean isExpired(long timeoutSeconds) {
        return lastAccess.plusSeconds(timeoutSeconds).isBefore(Instant.now());
    }
}

