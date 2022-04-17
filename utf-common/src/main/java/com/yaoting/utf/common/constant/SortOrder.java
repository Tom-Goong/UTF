package com.yaoting.utf.common.constant;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum SortOrder  {
    asc, desc, unsorted;

    public static boolean isSort(SortOrder sortOrder) {
        if (Objects.isNull(sortOrder) || sortOrder.equals(unsorted)) {
            return false;
        }
        return true;
    }

    public boolean isAsc() {
        return this.equals(asc);
    }
}
