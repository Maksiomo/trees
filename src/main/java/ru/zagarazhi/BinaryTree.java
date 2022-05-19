package ru.zagarazhi;

public class BinaryTree {

    private int key;
    private BinaryTree left = null;
    private BinaryTree right = null;
    private int level;

    public BinaryTree(int key) {
        this.key = key;
        this.level = 0;
    }

    private BinaryTree(int key, int level) {
        this.key = key;
        this.level = level;
    }

    public BinaryTree(int[] keys) {
        this(keys[0]);
        for(int i = 1; i < keys.length; i++){
            this.addElement(keys[i]);
        }
    }

    public void print() {
        for(int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(key);
        if(left != null){
            left.print();
        }
        if (right != null) {
            right.print();
        }
    }

    public void tour(){
        if(left != null){
            left.tour();
        }
        if (right != null) {
            right.tour();
        }
        System.out.println("Level: " + level + ", Key: " + key);
    }

    public BinaryTree search(int number) {
        if(this.key == number || (this.left == null && this.right == null)) return this;
        BinaryTree left, right, temp;
        if(this.right == null) {
            left = this.left.search(number);
            if(left.key == number) return left;
            temp = (Math.abs(number - left.key) < Math.abs(number - this.key))? left : this;
            return (number - temp.key > 0)?
                        temp
                        : (number - left.key > 0)?
                            left
                            : (number - this.key > 0)? this : temp;
        }
        if(this.left == null) {
            right = this.right.search(number);
            if(right.key == number) return right;
            temp = (Math.abs(number - right.key) < Math.abs(number - this.key))? right : this;
            return (number - temp.key > 0)?
                        temp
                        : (number - right.key > 0)?
                            right
                            : (number - this.key > 0)? this : temp;
        }
        left = this.left.search(number);
        if(left.key == number) return left;
        right = this.right.search(number);
        if(right.key == number) return right;
        temp = (Math.abs(number - left.key) < Math.abs(number - right.key))?
                    (Math.abs(number - left.key) < Math.abs(number - this.key))? left : this
                    : (Math.abs(number - right.key) < Math.abs(number - this.key))? right : this;
        return (number - temp.key > 0)?
                    temp 
                    : (number - left.key > 0)?
                        (number - right.key > 0)?
                            ((number - right.key) < (number - left.key)) ? right : left 
                            : (number - this.key > 0)?
                                ((number - left.key) < (number - this.key)) ? left : this
                                : left
                        : (number - right.key > 0)?
                            (number - this.key > 0)?
                                ((number - right.key) < (number - this.key)) ? right : this
                                : right
                            : (number - this.key > 0)? this : temp;
    }

    public void addElement(int key) {
        if(key < this.key){
            if(left == null){
                left = new BinaryTree(key, this.level + 1);
                return;
            }
            left.addElement(key);
            return;
        }
        if(right == null) {
            right = new BinaryTree(key, this.level + 1);
            return;
        }
        right.addElement(key);
    }

    @Override
    public String toString(){
        return "Level: " + level + ", Key: " + key;
    }
}
