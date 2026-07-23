package org.spring.core.learn.spring11singleton.cache;

import org.spring.core.learn.spring11singleton.entity.Employee;

import java.util.HashMap;
import java.util.Map;

public class EmployeeCache {

    private static volatile EmployeeCache instance;

    private final Map<Long, Employee> cache = new HashMap<>();

    private EmployeeCache() { }

    public static EmployeeCache getInstance() {
        if (instance == null) {
            synchronized (EmployeeCache.class) {
                if (instance == null) {
                    instance = new EmployeeCache();
                }
            }
        }
        return instance;
    }

    public void put(Employee e) { cache.put(e.getId(), e); }
    public Employee get(Long id) { return cache.get(id); }
}
