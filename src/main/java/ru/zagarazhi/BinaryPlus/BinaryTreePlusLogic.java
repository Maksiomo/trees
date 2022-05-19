package ru.zagarazhi.BinaryPlus;

import java.util.ArrayList;

public class BinaryTreePlusLogic {

    public static Tree initTree(int elementsPerLeaf, int keys, float[] values) {
        Tree newTree = new Tree();
        ArrayList<TreeLeaf> dynamicLeaf = new ArrayList<TreeLeaf>();
        ArrayList<Node> dynamicNode = parseToNodeList(keys, values);
        ArrayList<Node> initialValues = parseToNodeList(keys, values);
        int levelElementSize = keys;
        do {
            dynamicNode = null;
            levelElementSize = (levelElementSize / elementsPerLeaf) + 1;
            for (int i = 0; i < levelElementSize; i++) {

            }

        } while (levelElementSize > elementsPerLeaf);

        return newTree;
    }

    private static ArrayList<Node> parseToNodeList(int keys, float[] values) {
        ArrayList<Node> initialValues = new ArrayList<Node>();
        Node dynamicNode = new Node();
        Node buffer = null;
        for (int i = 0; i < keys; i++) {
            dynamicNode.data = values[i];
            dynamicNode.idNode = i;
            dynamicNode.next = null;
            if (buffer != null) {
                buffer = initialValues.get(i - 1);
                buffer.next = dynamicNode;
                initialValues.set(i - 1, buffer);
            }
            buffer = dynamicNode;
            initialValues.add(dynamicNode);
        }
        return initialValues;
    }

    private static TreeLeaf parseBatchToLeaf(ArrayList<Node> batch, int key) {
        TreeLeaf leaf = new TreeLeaf();
        leaf.idLeaf = key;
        leaf.nodes = batch;
        return leaf;
    }
}
