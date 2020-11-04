package ua.tarastom.service;

import ua.tarastom.entity.Type;

public interface IService {

    void commandLine(String filePath);

    Type convertType(String type);
}
