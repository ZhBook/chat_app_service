package com.tensua.blogservice.arithmetic.shell;

import java.util.Arrays;

/**
 * 希尔排序，也称递减增量排序算法，是插入排序的一种更高效的改进版本。但希尔排序是非稳定排序算法。
 * <p>
 * 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
 * <p>
 * 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率；
 * 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位；
 * 希尔排序的基本思想是：先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，待整个序列中的记录“基本有序”时，再对全体记录进行依次直接插入排序。
 *
 * @author zhooke
 * @since 2025/1/7 16:21
 **/
public class ShellSortClass {
    public static void main(String[] args) {
        int[] arr = {12, 23, 43, 11, 67, 13, 32, 69, 9};
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        int[] sortArrResult = shellSort(arrCopy);
        System.out.println("原数据：" + Arrays.toString(arr));
        System.out.println("排序后：" + Arrays.toString(sortArrResult));
    }

    public static int[] shellSort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                int tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = tmp;
            }
            gap = (int) Math.floor(gap / 3);
        }

        return arr;
    }
}
