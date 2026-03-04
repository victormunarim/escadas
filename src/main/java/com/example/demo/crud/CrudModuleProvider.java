package com.example.demo.crud;

import java.util.List;
import java.util.Map;

public interface CrudModuleProvider {
    String key();
    CrudModule module();

    List<Map<String, Object>> rows(Map<String, String> params);
}