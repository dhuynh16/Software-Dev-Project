package com.companyz.ems.security;

import com.companyz.ems.user.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final long TIMEOUT_SECONDS = 1800;
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public Session createSession(User user) {
        Session s = new Session(user);
        sessions.put(s.getId(), s);
        return s;
    }

    public Session getSession(String id) {
        Session s = sessions.get(id);
        if (s == null) return null;
        if (s.isExpired(TIMEOUT_SECONDS)) {
            sessions.remove(id);
            return null;
        }
        s.touch();
        return s;
    }

    public void destroySession(String id) {
        sessions.remove(id);
    }
}

