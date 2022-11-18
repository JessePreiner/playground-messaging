package com.jessepreiner;

import java.util.UUID;

public record Id(UUID id) {
    @Override
    public String toString() {
        return id.toString();
    }
}
