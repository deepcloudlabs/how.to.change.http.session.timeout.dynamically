package com.example.infrastructure.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
@Service
public class HttpSessionService {
	private static final Predicate<HttpSession> ifSessionTimedout = session -> {
		return ZonedDateTime.now().toEpochSecond() >= session.getLastAccessedTime()
				+ session.getMaxInactiveInterval() / 1_000;

	};
	@Value("${spring.session.timeout}")
	private int defaultSessionTimeout;
	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(HttpSessionService.class);

	public void createSession(final HttpSessionEvent httpSessionEvent) {
		createSession(httpSessionEvent, defaultSessionTimeout);
	}

	public void createSession(final HttpSessionEvent httpSessionEvent, int sessionTimeout) {
		HttpSession httpSession = httpSessionEvent.getSession();
		httpSession.setMaxInactiveInterval(sessionTimeout);
		String sessionId = httpSession.getId();
		logger.info(String.format("Creating new session %s with timeout %d.", sessionId,httpSession.getMaxInactiveInterval()));
		sessions.put(sessionId, httpSession);
		getAllSessions().forEach(session -> logger.info(String.format("Session Id (%s), Session Timeout (%d).",
				session.getId(), session.getMaxInactiveInterval())));
	}

	public void removeSession(final HttpSessionEvent httpSessionEvent) {
		logger.info(String.format("Removing session %s.", httpSessionEvent.getSession().getId()));
		sessions.remove(httpSessionEvent.getSession().getId());
	}

	public void removeSession(final String sessionId) {
		logger.info(String.format("Removing session %s.", sessionId));
		sessions.remove(sessionId);
	}

	public Optional<HttpSession> getSession(final String sessionId) {
		return Optional.ofNullable(sessions.get(sessionId));
	}

	public void setSessionTimeout(final String sessionId, int sessionTimeout) {
		getSession(sessionId).ifPresent(session -> session.setMaxInactiveInterval(sessionTimeout));
	}

	public List<HttpSession> getAllSessions() {
		return new CopyOnWriteArrayList<>(sessions.values());
	}

	@Scheduled(fixedRate = 60_000)
	public void invalidateTimedoutSessions() {
		getAllSessions().stream().filter(ifSessionTimedout).forEach(HttpSession::invalidate);
	}
}
