package net.aohayou.collector.model;

import android.support.annotation.NonNull;

public class Collection implements Comparable<Collection> {
    @NonNull private final String name;

    public Collection(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Collection another) {
        return name.compareTo(another.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collection that = (Collection) o;
        return name.equals(that.name);
    }
}
