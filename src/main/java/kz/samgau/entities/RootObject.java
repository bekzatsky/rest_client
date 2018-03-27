package kz.samgau.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RootObject {

    private Long total;
    private Long limit;
    private String next_page;
    private ArrayList<Item> items;
}
