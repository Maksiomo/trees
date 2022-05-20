package ru.zagarazhi;

public class BPTree {

	private class TreeNode {
		public boolean isLeaf;
		public int[] keys;
		public int count;
		public TreeNode[] child;
		public int[] values;
		public TreeNode next; // только для нижнего уровня
		public TreeNode head; // ссылка на родителя

		public TreeNode(int degree) {
			this.isLeaf = false;
			this.count = 0;
			this.keys = new int[degree];
			this.values = new int[degree + 1];
			this.child = new TreeNode[degree + 1];
			this.next = null;
		}

		@Override
		public String toString() {
			return "isLeaf:" + isLeaf + "\n" + "count:" + count + "\n" + "keys:" + keys.toString() + "\n" + "values:"
					+ values.toString()
					+ "\n" + "isLeaf:" + child.toString() + "\n";
		}
	}

	public class SearchOutput {
		public int closest;
		public int level;

		public SearchOutput(int closest, int level) {
			this.closest = closest;
			this.level = level;
		}
	}

	private TreeNode root;
	// private TreeNode groundNode;
	private int degree;

	public TreeNode getRoot() {
		return this.root;
	}

	public BPTree(int degree) {
		this.degree = degree;
	}

	private TreeNode findParent(TreeNode cursor, TreeNode child) {
		TreeNode parent = null;
		if (cursor.isLeaf | (cursor.child[0]).isLeaf)
			return null;
		for (int i = 0; i < cursor.count + 1; i++) {
			if (cursor.child[i] == child) {
				parent = cursor;
				return parent;
			} else {
				parent = findParent(cursor.child[i], child);
				if (parent != null) {
					return parent;
				}
			}
		}
		return parent;
	}

	private void insertInternal(int x, TreeNode cursor, TreeNode child) {
		int i = 0;
		int j = 0;
		if (cursor.count < this.degree) {
			while (x > cursor.keys[i] && i < cursor.count) {
				i++;
			}
			for (j = cursor.count; j > i; j--) {
				cursor.keys[j] = cursor.keys[j - 1];
			}
			for (j = cursor.count + 1; j > i + 1; j--) {
				cursor.child[j] = cursor.child[j - 1];
			}
			cursor.keys[i] = x;
			cursor.count++;
			cursor.child[i + 1] = child;
		} else {
			TreeNode newInternal = new TreeNode(this.degree);
			int[] virtualKey = new int[this.degree + 1];
			TreeNode[] virtualPtr = new TreeNode[this.degree + 2];
			for (i = 0; i < this.degree; i++) {
				virtualKey[i] = cursor.keys[i];
			}
			for (i = 0; i < this.degree + 1; i++) {
				virtualPtr[i] = cursor.child[i];
			}
			i = 0;
			while (x > virtualKey[i] && i < this.degree) {
				i++;
			}
			for (j = this.degree + 1; j > i; j--) {
				virtualKey[j] = virtualKey[j - 1];
			}
			virtualKey[i] = x;
			for (j = this.degree + 2; j > i + 1; j--) {
				virtualPtr[j] = virtualPtr[j - 1];
			}
			virtualPtr[i + 1] = child;
			newInternal.isLeaf = false;
			cursor.count = (this.degree + 1) / 2;
			newInternal.count = this.degree - (this.degree + 1) / 2;
			for (i = 0, j = cursor.count + 1; i < newInternal.count; i++, j++) {
				newInternal.keys[i] = virtualKey[j];
			}
			for (i = 0, j = cursor.count + 1; i < newInternal.count + 1; i++, j++) {
				newInternal.child[i] = virtualPtr[j];
			}
			if (cursor == this.root) {
				this.root = new TreeNode(this.degree);
				this.root.keys[0] = cursor.keys[cursor.count];
				this.root.child[0] = cursor;
				this.root.child[1] = newInternal;
				this.root.isLeaf = false;
				this.root.count = 1;
			} else {
				insertInternal(cursor.keys[cursor.count], findParent(this.root, cursor), newInternal);
			}
		}
	}

	public void insert(int x, int value) {
		if (this.root == null) {
			this.root = new TreeNode(this.degree);
			this.root.isLeaf = true;
			this.root.count = 1;
			this.root.keys[0] = x;
			this.root.values[0] = value;
		} else {
			int i = 0;
			int j = 0;
			TreeNode cursor = this.root;
			TreeNode parent = null;
			while (!cursor.isLeaf) {
				parent = cursor;
				for (i = 0; i < cursor.count; i++) {
					if (x < cursor.keys[i]) {
						cursor = cursor.child[i];
						break;
					}
					if (i == cursor.count - 1) {
						cursor = cursor.child[i + 1];
						break;
					}
				}
			}
			if (cursor.count < this.degree) {
				i = 0;
				while (x > cursor.keys[i] && i < cursor.count) {
					i++;
				}
				for (j = cursor.count; j > i; j--) {
					cursor.keys[j] = cursor.keys[j - 1];
				}
				cursor.keys[i] = x;
				cursor.values[i] = value;
				cursor.count++;
				cursor.child[cursor.count] = cursor.child[cursor.count - 1];
				cursor.child[cursor.count - 1] = null;
			} else {
				TreeNode newLeaf = new TreeNode(this.degree);
				int[] virtualNode = new int[this.degree + 2];
				int[] bufferValues = new int[this.degree + 2];
				for (i = 0; i < this.degree; i++) {
					virtualNode[i] = cursor.keys[i];
					bufferValues[i] = cursor.values[i];
				}
				i = 0;
				while (x > virtualNode[i] && i < this.degree) {
					i++;
				}
				for (j = this.degree + 1; j > i; j--) {
					virtualNode[j] = virtualNode[j - 1];
					bufferValues[j] = bufferValues[j - 1];
				}
				virtualNode[i] = x;
				bufferValues[i] = value;
				newLeaf.isLeaf = true;
				cursor.count = (this.degree + 1) / 2;
				newLeaf.count = this.degree + 1 - (this.degree + 1) / 2;
				cursor.child[cursor.count] = newLeaf;
				newLeaf.child[newLeaf.count] = cursor.child[this.degree];
				cursor.child[this.degree] = null;
				for (i = 0; i < cursor.count; i++) {
					cursor.keys[i] = virtualNode[i];
					cursor.values[i] = bufferValues[i];
				}
				for (i = 0, j = cursor.count; i < newLeaf.count; i++, j++) {
					newLeaf.keys[i] = virtualNode[j];
					newLeaf.values[i] = bufferValues[j];
				}
				if (cursor == this.root) {
					TreeNode newRoot = new TreeNode(this.degree);
					newRoot.keys[0] = newLeaf.keys[0];
					newRoot.child[0] = cursor;
					newRoot.child[1] = newLeaf;
					newRoot.isLeaf = false;
					newRoot.count = 1;
					newRoot.values[0] = value;
					this.root = newRoot;
				} else {
					insertInternal(newLeaf.keys[0], parent, newLeaf);
				}
			}
		}
		// System.out.println("/////////////////////////////////////////// - Iteration "
		// + x);
		// print();
	}

	public void insertVer2(int insertKey, int value) {
		if (this.root == null) {
			this.root = new TreeNode(this.degree);
			this.root.isLeaf = true;
			this.root.count = 0;
			this.root.keys[0] = insertKey;
			this.root.values[0] = value;
			this.root.child[0] = this.root;
			this.root.head = null;
			// this.groundNode = this.root; TODO: связь элементов по нижнему уровню, не
			// обязательно, но было бы неплохл
		} else if (this.root.count == this.degree - 1) {
			insertBySplittingLeaf(this.root, insertKey, value);
		} else {
			TreeNode newRoot = new TreeNode(this.degree);
			newRoot.isLeaf = true;
			this.root.count++;
			this.root.keys[this.root.count] = insertKey;
			this.root.values[this.root.count] = value;
			this.root.child[this.root.count] = newRoot;
			this.root.child[this.root.count - 1].next = newRoot;
			this.root.child[this.root.count].head = this.root.head;
			// TreeNode temp = this.groundNode;
			// while(temp.next != null) {
			// temp = temp.next;
			// }
		}
	}

	private TreeNode insertBySplittingLeaf(TreeNode node, int insertKey, int value) {
		TreeNode newRoot = node.child[this.degree / 2 + 1];
		newRoot.isLeaf = false;
		newRoot.count = 2;
		newRoot.child[0] = this.root.child[0]; // 0, 1 элементы в первом
		newRoot.child[1] = this.root.child[2]; // 2, 3, 4 элементы во второй
		if (this.root.head != null) {
			this.root.head.count++;
			this.root.head.keys[this.root.head.count] = insertKey;
			this.root.head.values[this.root.head.count] = value;
			this.root.head.child[this.root.head.count] = newRoot;
			this.root.head.child[this.root.head.count - 1].next = newRoot;
			this.root.head.child[this.root.head.count].head = this.root.head.head;
			this.root = this.root.head;
			return this.root.head;
		} else {
			newRoot.head = null;
			this.root = newRoot;
			return newRoot; // возвращаем новую голову
		}
	}

	public void print() {
		if (this.root != null) {
			print(root, 0);
		}
	}

	private void print(TreeNode node, int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
		System.out.println("Level: " + level);
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
		System.out.print("[");
		for (int i = 0; i < node.count; i++) {
			if (node == root) {
				System.out.print(
						(i != 0) ? "; " + node.keys[i]
								: node.keys[i]);

			} else {
				System.out.print(
						(i != 0) ? "; " + node.keys[i] + ", v: " + node.values[i]
								: node.keys[i] + ", v: " + node.values[i]);
			}
		}
		System.out.println("]");
		if (!node.isLeaf) {
			for (int i = 0; i < node.count + 1; i++) {
				print(node.child[i], level + 1);
			}
		}
	}

	public void tour() {
		if (this.root != null) {
			tour(root);
		}
		System.out.println();
	}

	private void tour(TreeNode node) {
		int i = 0;
		while (i < node.count) {
			if (!node.isLeaf) {
				tour(node.child[i]);
			} else {
				System.out.print("  " + node.keys[i]);
			}
			i++;
		}
		if (!node.isLeaf) {
			tour(node.child[i]);
		}
	}

	private SearchOutput searchVer2(TreeNode node, int level, int number, SearchOutput out) {
		if (node != null) {
			for (int i = 0; i < node.count; i++) {
				if (node != root) {
					if ((node.values[i] > out.closest) && (node.values[i] < number)) {
						out.closest = node.values[i];
						out.level = level;
					}
					// System.out.println(out.closest + " " + out.level);
				}
			}
		}
		if ((node != null) && (!node.isLeaf || node.child[0] != null)) {
			for (int i = 0; i < node.count + 1; i++) {
				node.child[i].toString();
				searchVer2(node.child[i], level + 1, number, out);
			}
		}
		return out;
	}

	public SearchOutput searchVer2(int number) {
		SearchOutput out = new SearchOutput(0, 0);
		if (this.root != null) {
			out = searchVer2(this.root, 0, number, out);
		}
		return out;
	}
}
