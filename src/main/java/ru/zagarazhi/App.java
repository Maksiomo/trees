package ru.zagarazhi;

import java.util.Random;

import ru.zagarazhi.BPTree.SearchOutput;

public class App {
    public static void main(String[] args) {
        // 30 - тестовый
        Random random = new Random(30);
        int[] ra = new int[16];
        for (int i = 0; i < ra.length; i++) {
            ra[i] = random.nextInt(151);
        }
        BinaryTree bt = new BinaryTree(ra);
        bt.print();
        bt.tour();
        System.out.println("Искомое число: 123");
        System.out.println("Ближайшее число, меньше или равное искомому: " +
                bt.search(123).toString());
        BPTree bpt = new BPTree(5);
        for (int i = 0; i < 16; i++) {
            bpt.insert(i + 1, ra[i]);
        }
        // bpt.tour();
        bpt.print();
        SearchOutput searchRes = bpt.searchVer2(123);
        System.out.println("Искомое число: 123");
        System.out.println("Ближайшее число, меньше или равное искомому: значение - " + searchRes.closest
                + ", находится на уровне: " + searchRes.level);

    }
}
