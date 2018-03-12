package com.anyangdp.handler;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageDTO {

    private int totalPages;

    private long totalElements;

    private int number;

    private int size;

    private int numberOfElements;
}
