/*******************************************************************************
 * Copyright 2020 BioCliq Technologies
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.zitlab.palmyra.ds;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class AvlTree<T> {

	private int size = 0;
	private AvlTreeNode<T> root;
	private AvlTreeNode<T> lastNode;
	private AvlTreeNode<T> firstNode;

	public int height(AvlTreeNode<T> N) {
		if (null == N)
			return 0;
		return N.height;
	}

	private int max(int a, int b) {
		return (a > b) ? a : b;
	}

	private AvlTreeNode<T> rightRotate(AvlTreeNode<T> y) {
		AvlTreeNode<T> x = y.left;
		AvlTreeNode<T> T2 = x.right;

		// Perform rotation
		x.right = y;
		y.left = T2;

		// Update heights
		y.height = max(height(y.left), height(y.right)) + 1;
		x.height = max(height(x.left), height(x.right)) + 1;

		// Return new root
		return x;
	}

	private AvlTreeNode<T> leftRotate(AvlTreeNode<T> x) {
		AvlTreeNode<T> y = x.right;
		AvlTreeNode<T> T2 = y.left;

		// Perform rotation
		y.left = x;
		x.right = T2;

		// Update heights
		x.height = max(height(x.left), height(x.right)) + 1;
		y.height = max(height(y.left), height(y.right)) + 1;

		// Return new root
		return y;
	}

	int getBalance(AvlTreeNode<T> N) {
		if (N == null)
			return 0;

		return height(N.left) - height(N.right);
	}

	public void insert(String key, T value) {
		root = insert(root, key, value);
	}

	public void clear() {
		root = null;
		size = 0;
		firstNode = null;
		lastNode = null;
	}

	public AvlTreeNode<T> get(String key) {
		return get(root, key);
	}

	public void remove(String key) {
		remove(root, key);
	}

	private AvlTreeNode<T> get(AvlTreeNode<T> node, String key) {
		if (null == node)
			return null;

		int status = node.compare(key);
		if (status < 0)
			return get(node.left, key);
		else if (status > 0)
			return get(node.right, key);
		else
			return node;
	}

	AvlTreeNode<T> remove(AvlTreeNode<T> root, String key) {
		if (root == null)
			return root;
		int status = root.compare(key);
		if (status > 0)
			root.left = remove(root.left, key);
		else if (status < 0)
			root.right = remove(root.right, key);
		else {
			if ((root.left == null) || (root.right == null)) {
				AvlTreeNode<T> temp = null;
				if (temp == root.left)
					temp = root.right;
				else
					temp = root.left;

				if (temp == null) {
					temp = root;
					root = null;
				} else
					root = temp;
			} else {
				AvlTreeNode<T> temp = minValueNode(root.right);
				root.key = temp.key;
				root.right = remove(root.right, temp.key);
			}

			size--;
			AvlTreeNode<T> temp = root.prev;
			if (null != temp) {				
				if(null != root.next) {
					temp.next = root.next;
					root.next.prev = temp;
				}else
					temp.next = null;				
			}
		}

		if (root == null)
			return root;

		root.height = max(height(root.left), height(root.right)) + 1;

		int balance = getBalance(root);

		if (balance > 1 && getBalance(root.left) >= 0)
			return rightRotate(root);

		if (balance > 1 && getBalance(root.left) < 0) {
			root.left = leftRotate(root.left);
			return rightRotate(root);
		}

		if (balance < -1 && getBalance(root.right) <= 0)
			return leftRotate(root);

		if (balance < -1 && getBalance(root.right) > 0) {
			root.right = rightRotate(root.right);
			return leftRotate(root);
		}

		return root;
	}

	AvlTreeNode<T> minValueNode(AvlTreeNode<T> node) {
		AvlTreeNode<T> current = node;

		/* loop down to find the leftmost leaf */
		while (current.left != null)
			current = current.left;

		return current;
	}

	AvlTreeNode<T> insert(AvlTreeNode<T> node, String key, T value) {

		/* 1. Perform the normal BST insertion */
		if (null == node) {
			size++;
			AvlTreeNode<T> newNode = new AvlTreeNode<T>(key, value);
			if (null != lastNode) {
				newNode.prev = lastNode;
				lastNode.next = newNode;
			}
			lastNode = newNode;
			if(null == firstNode)
				firstNode = newNode;
			return newNode;
		}

		int status = node.compare(key);
		if (status < 0)
			node.left = insert(node.left, key, value);
		else if (status > 0)
			node.right = insert(node.right, key, value);
		else // Duplicate keys not allowed
			return node;

		/* 2. Update height of this ancestor node */
		node.height = 1 + max(height(node.left), height(node.right));

		/*
		 * 3. Get the balance factor of this ancestor node to check whether this node
		 * became unbalanced
		 */
		int balance = getBalance(node);

		if (0 == balance)
			return node;

		else if (balance > 2) {
			status = node.left.compare(key);
			if (status < 0)
				return rightRotate(node);
			else if (status > 0) {
				node.left = leftRotate(node.left);
				return rightRotate(node);
			}
		} else if (balance < -2) {
			status = node.right.compare(key);
			if (status < 0) {
				return leftRotate(node);
			} else if (status > 0) {
				node.right = rightRotate(node.right);
				return leftRotate(node);
			}
		}
		return node;
	}

	public void preOrder() {
		preOrder(root);
	}

	void preOrder(AvlTreeNode<T> node) {
		if (node != null) {
			System.out.println(node.key + " " + node.value + " " + node.height);
			preOrder(node.left);
			preOrder(node.right);
		}
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<T> {
		AvlTreeNode<T> current;

		// prevent creating a synthetic constructor
		Itr() {
		}

		public boolean hasNext() {
			return null != current.next;
		}

		public T next() {
			AvlTreeNode<T> result = current.next;
			current = result;
			return result.getValue();
		}

		public void remove() {
			AvlTree.this.remove(current.key);
			current = current.getNext();
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			AvlTreeNode<T> current = root;
			while (current != null) {
				action.accept(current.getValue());
				current = current.getNext();
			}
		}

		private final void checkForComodification() {

		}
	}
}
