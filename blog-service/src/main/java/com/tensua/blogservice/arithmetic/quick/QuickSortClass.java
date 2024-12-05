package com.tensua.blogservice.arithmetic.quick;

import java.util.Arrays;

/**
 * 快速排序是由东尼·霍尔所发展的一种排序算法。在平均状况下，排序 n 个项目要 Ο(nlogn) 次比较。在最坏状况下则需要 Ο(n2) 次比较，但这种状况并不常见。
 * 事实上，快速排序通常明显比其他 Ο(nlogn) 算法更快，因为它的内部循环（inner loop）可以在大部分的架构上很有效率地被实现出来。
 * 快速排序使用分治法（Divide and conquer）策略来把一个串行（list）分为两个子串行（sub-lists）。
 * 快速排序又是一种分而治之思想在排序算法上的典型应用。本质上来看，快速排序应该算是在冒泡排序基础上的递归分治法。
 * 快速排序的名字起的是简单粗暴，因为一听到这个名字你就知道它存在的意义，就是快，而且效率高！它是处理大数据最快的排序算法之一了。
 * 虽然 Worst Case 的时间复杂度达到了 O(n²)，但是人家就是优秀，在大多数情况下都比平均时间复杂度为 O(n logn) 的排序算法表现要更好，可是这是为什么呢，我也不知道。
 * 好在我的强迫症又犯了，查了 N 多资料终于在《算法艺术与信息学竞赛》上找到了满意的答案：
 * 快速排序的最坏运行情况是 O(n²)，比如说顺序数列的快排。但它的平摊期望时间是 O(nlogn)，且 O(nlogn) 记号中隐含的常数因子很小，比复杂度稳定等于 O(nlogn) 的归并排序要小很多。
 * 所以，对绝大多数顺序性较弱的随机数列而言，快速排序总是优于归并排序。
 *
 * @author zhooke
 * @since 2024/12/5 10:07
 **/
public class QuickSortClass {

    public static void main(String[] args) {
        int[] arr = {12, 23, 43, 11, 67, 13, 32, 69, 9};
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        int[] sortArrResult = quickSort(arrCopy, 0, arrCopy.length - 1);
        System.out.println("原数据：" + Arrays.toString(arr));
        System.out.println("排序后：" + Arrays.toString(sortArrResult));
    }

    private static int[] quickSort(int[] array, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(array, left, right);
            quickSort(array, left, partitionIndex - 1);
            quickSort(array, partitionIndex + 1, right);
        }
        return array;
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (array[i] < array[pivot]) {
                swap(array, i, index);
                index++;
            }
        }
        swap(array, pivot, index - 1);
        return index - 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
