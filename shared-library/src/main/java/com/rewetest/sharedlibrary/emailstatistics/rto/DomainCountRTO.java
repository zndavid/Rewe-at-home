package com.rewetest.sharedlibrary.emailstatistics.rto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainCountRTO {
    private String domain;
    private Number count;
}
