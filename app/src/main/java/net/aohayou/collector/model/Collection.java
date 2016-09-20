package net.aohayou.collector.model;

import android.support.annotation.NonNull;

public class Collection implements Comparable<Collection> {
    private final String name;

    public Collection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Collection another) {
        return name.compareTo(another.getName());
    }
}
