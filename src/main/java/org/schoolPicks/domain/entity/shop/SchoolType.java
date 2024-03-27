package org.schoolPicks.domain.entity.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SchoolType {
    NSC("자연과학캠퍼스"),
    HSSC("인문사회캠퍼스");

    private final String text;
}
