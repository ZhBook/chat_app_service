package com.tensua.blogservice.arithmetic.selection;

import java.util.Arrays;

/**
 * 选择排序是一种简单直观的排序算法，无论什么数据进去都是 O(n²) 的时间复杂度。
 * 所以用到它的时候，数据规模越小越好。唯一的好处可能就是不占用额外的内存空间了吧。
 *
 * 1.首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置
 * 2.再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
 * 3.重复第二步，直到所有元素均排序完毕。
 * @author zhooke
 * @since 2024/12/16 13:59
 **/
public class SelectionSortClass {
    public static void main(String[] args) {
        int[] arr = {12, 23, 43, 11, 67, 13, 32, 69, 9};
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        int[] sortArrResult = selectionSort(arrCopy);
        System.out.println("原数据：" + Arrays.toString(arr));
        System.out.println("排序后：" + Arrays.toString(sortArrResult));
    }

    public static int[] selectionSort(int[] arr) {

        // 总共要经过 N-1 轮比较
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            // 每轮需要比较的次数 N-i
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    // 记录目前能找到的最小值元素的下标
                    min = j;
                }
            }
            // 将找到的最小值和i位置所在的值进行交换
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
        return arr;
    }
}
