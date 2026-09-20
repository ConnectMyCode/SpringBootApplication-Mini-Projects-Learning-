package org.spring.core.learn.spring14fitclubmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


    /*
     * ================= REACT FRONTEND INTEGRATION: CORS =================
     * Your React app runs on its OWN origin during development - typically
     * http://localhost:3000 (Create React App) or http://localhost:5173
     * (Vite) - while this Spring Boot API runs on http://localhost:8080.
     * Different port = different ORIGIN as far as the browser is concerned.
     *
     * By default, browsers BLOCK JavaScript on one origin from calling an
     * API on a different origin (the Same-Origin Policy) - CORS selectively
     * relaxes this. Without this config, every fetch()/axios call from React
     * to this backend fails in the browser console, even though the request
     * would work fine from Postman/curl - CORS is enforced by the BROWSER.
     *
     * allowCredentials(true) is REQUIRED here because this app uses
     * HttpSession-based login (a session cookie) - the browser only sends
     * that cookie cross-origin if the server explicitly allows credentials.
     *
     * FRONTEND NOTE: every React request that should carry the session
     * cookie needs `credentials: 'include'` (fetch) or `withCredentials: true`
     * (axios).
     */
    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    // PLACEHOLDER: update to your real React dev server port,
                    // and to your deployed frontend's real domain in production.
                    .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }


