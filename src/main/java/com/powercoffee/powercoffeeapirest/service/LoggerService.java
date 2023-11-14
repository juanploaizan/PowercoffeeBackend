package com.powercoffee.powercoffeeapirest.service;

public interface LoggerService {
    void logAction(String entityName, String actionType, Object previousValue, Object actualValue, Integer userId);
}
