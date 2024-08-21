package com.example.anitracker.vnObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VNRequestBody {
    String sort, fields;
    int results, page;
    Boolean reverse;
    List<Object> filters;
    public VNRequestBody(String sort, boolean reverse,
                         int results, int page, String fields, List<Object> filters){
        this.sort = sort;
        this.reverse = reverse;
        this.fields = fields;
        this.page = page;
        this.results = results;
        this.filters = filters;
    }
}
