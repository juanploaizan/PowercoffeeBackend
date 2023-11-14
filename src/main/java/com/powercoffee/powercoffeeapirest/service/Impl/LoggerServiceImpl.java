package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.service.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

@Service
public class LoggerServiceImpl implements LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerServiceImpl.class);

    private final String logFileName = "powercoffee.log";
    private final String logDirectory = "logs";
    private final String logFilePath = Paths.get(logDirectory, logFileName).toAbsolutePath().toString();

    @Override
    public void logAction(String entityName, String actionType, Object previousValue, Object actualValue, Integer userId) {
        LocalDateTime currentDate = LocalDateTime.now();

        String logMessage = String.format("date: %s, entityName: %s, actionType: %s, previousValue: %s, actualValue: %s, userId: %s",
                currentDate, entityName, actionType, previousValue, actualValue, userId);

        logger.info(logMessage);

        logToFile(logMessage);
    }

    private void logToFile(String logMessage) {
        try {
            Files.createDirectories(Paths.get(logDirectory)); // Crea el directorio si no existe
            Files.writeString(Paths.get(logFilePath), logMessage + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error en la generación del archivo log");
            e.printStackTrace();
        }
    }

}
