package ua.tarastom.service;

import ua.tarastom.entity.Type;

public interface IService {

    void commandLine(String testFile, String resultFile);

    Type convertType(String type);
}
