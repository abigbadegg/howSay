package com.howsay.howsay;

import java.util.Collections;
import java.util.List;

import com.darcytech.doris.utils.calculate.BulkBeanUtils;
import com.darcytech.doris.utils.calculate.ValueHolderUtils;
import com.darcytech.doris.utils.calculate.valueobject.ValueHolder;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ValueHolderTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class A {
        String name;
        Long age;
        Long countTest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class B {
        String name;
        Long SumTest;
        Long countTest;
    }

    public static void main(String[] args) {
        A a = new A("lyb", 27L, 1L);
        B b = new B("lyb", 2L, 3L);

        ValueHolder holderA = BulkBeanUtils.getValueHolder(a);
        ValueHolder holderB = BulkBeanUtils.getValueHolder(b);

        List<ValueHolder> valueHolders = ValueHolderUtils.joinBy(Collections.singletonList(holderA), Collections.singletonList(holderB), "name");
        List<ValueHolder> valueHolders2 = ValueHolderUtils.joinBy(Collections.singletonList(holderA), Collections.singletonList(holderB), "name");

        valueHolders.addAll(valueHolders2);
        List<ValueHolder> valueHoldersFinal = ValueHolderUtils.mergeBy(valueHolders, Lists.newArrayList("name"), Lists.newArrayList("Test"));
        System.out.println(valueHoldersFinal);


    }
}
