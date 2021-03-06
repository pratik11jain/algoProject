import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

// Java program for deletion in AVL Tree
// http://www.geeksforgeeks.org/avl-tree-set-2-deletion/
class Node
{
    public String key;
    int height;
    Node left, right;

    Node(String d)
    {
        key = d;
        height = 1;
    }
}

class AVLTree
{
    Node root;

    // A utility function to get height of the tree
    int height(Node N)
    {
        if (N == null)
            return 0;
        return N.height;
    }

    // A utility function to get maximum of two integers
    public String max(String a, String b)
    {
        return (a.compareTo(b) > 0) ? a : b;
    }

    public int max(int a, int b)
    {
        return (a > b) ? a : b;
    }

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    Node rightRotate(Node y)
    {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x)
    {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        //  Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get Balance factor of node N
    public int getBalance(Node N)
    {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    public Node insert(Node node, String key)
    {
        /* 1.  Perform the normal BST rotation */
        if (node == null)
            return (new Node(key));

        if (key.compareTo(node.key) < 0)
            node.left = insert(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = insert(node.right, key);
        else // Equal keys not allowed
            return node;

        /* 2. Update height of this ancestor node */
        node.height = 1 + max(height(node.left),
                height(node.right));

        /* 3. Get the balance factor of this ancestor
           node to check whether this node became
           Wunbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then
        // there are 4 cases Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0)
        {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0)
        {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }


    public boolean ifExists(Node node, String key)
    {
        /* 1.  Perform the normal BST rotation */
        if (node == null)
           return false;

        if (key.compareTo(node.key) < 0)
            return ifExists(node.left, key);
        else if (key.compareTo(node.key) > 0)
            return ifExists(node.right, key);
        else // Equal keys not allowed
            return true;
    }

    public ArrayList<String> autoComplete(Node node, String word){
        ArrayList<String> list = new ArrayList<>();
        if (node == null) {
            return list;
        }
        word = word.toLowerCase();
        int count = 10;
        ArrayList<Node> fringe = new ArrayList<>();
        fringe.add(node);
        while(!fringe.isEmpty() && count > 0) {
            Node temp = fringe.remove(0);
            if (temp == null) {
                continue;
            }
            if (temp.key.startsWith(word)) {
                list.add(temp.key);
                count--;
            }

            if (temp.key.startsWith(word)) {
                fringe.add(temp.right);
                fringe.add(temp.left);
            }
            else if (temp.key.compareTo(word) < 0) {

                fringe.add(temp.right);
            }
            else {
                fringe.add(temp.left);

            }

        }

        return list;
    }

    /* Given a non-empty binary search tree, return the
       node with minimum key value found in that tree.
       Note that the entire tree does not need to be
       searched. */
    Node minValueNode(Node node)
    {
        Node current = node;

        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;

        return current;
    }

    public Node deleteNode(Node root, String key)
    {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key.compareTo(root.key) < 0)
            root.left = deleteNode(root.left, key);

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key.compareTo(root.key) > 0)
            root.right = deleteNode(root.right, key);

            // if key is same as root's key, then this is the node
            // to be deleted
        else
        {

            // node with only one child or no child
            if ((root.left == null) || (root.right == null))
            {
                Node temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                // No child case
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else   // One child case
                    root = temp; // Copy the contents of
                // the non-empty child
            }
            else
            {

                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                Node temp = minValueNode(root.right);

                // Copy the inorder successor's data to this node
                root.key = temp.key;

                // Delete the inorder successor
                root.right = deleteNode(root.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = max(height(root.left), height(root.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        //  this node became unbalanced)
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public HashSet<String> readWords(String fileName) {
        HashSet<String> words = new HashSet<String>();
        int i = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for(String line; (line = br.readLine()) != null; ) {
                if (line.matches("[a-zA-Z]+")) {
                    words.add(line);
                    System.out.println(i++);
                }
            }
            // line is not visible here.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public ArrayList<Long> testOperations(HashSet<String> insertWords, HashSet<String> searchWords, HashSet<String> deleteWords, HashSet<String> autoCompleteWords) {
        AVLTree tree = new AVLTree();
        ArrayList<Long> results = new ArrayList<Long>();


        // Insertion
        long startTime = System.nanoTime();
        for (String word : insertWords)
        {
            tree.root = tree.insert(tree.root, word);
        }
        long endTime = System.nanoTime();
        results.add(endTime-startTime);


        // If Exists
        startTime = System.nanoTime();
        for (String word : searchWords)
        {
            tree.ifExists(tree.root, word);
        }
        endTime = System.nanoTime();
        results.add(endTime-startTime);


        // AutoComplete
        startTime = System.nanoTime();
        for (String word : autoCompleteWords)
        {
            tree.autoComplete(tree.root, word);
        }
        endTime = System.nanoTime();
        results.add(endTime-startTime);


        // Delete
        startTime = System.nanoTime();
        for (String word : deleteWords)
        {
            tree.ifExists(tree.root, word);
        }
        endTime = System.nanoTime();
        results.add(endTime-startTime);

        return results;
    }

    public static void main(String[] args)
    {
        AVLTree tree = new AVLTree();

        /* Constructing tree given in the above figure */

        HashSet<String> words = tree.readWords("D:\\dicitionary.txt");
        long startTime = System.nanoTime();
        for (String word : words)
            tree.root = tree.insert(tree.root, word);
        long endTime = System.nanoTime();
        System.out.println("Initialization:" + (endTime-startTime));

        System.out.println(tree.ifExists(tree.root, "allowed"));



        startTime = System.nanoTime();
        System.out.println(tree.autoComplete(tree.root, "allow"));
        endTime = System.nanoTime();
        System.out.println("AutoComplete:" + (endTime-startTime));

    }
}

// This code has been contributed by Mayank Jaiswal