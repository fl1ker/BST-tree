import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

class Node {
    String word;
    Node left, right;

    public Node(String item) {
        word = item;
        left = right = null;
    }
}

class MyBstTree {
    Node root;

    MyBstTree() {
        root = null;
    }
    void add(String word) {
        Node newNode = new Node(word);

        if (root == null) {
            root = newNode;
            return;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int compareResult = word.compareTo(current.word);

            if (compareResult < 0) {
                current = current.left;
            } else if (compareResult > 0) {
                current = current.right;
            } else {
                return;
            }
        }

        int compareResult = word.compareTo(parent.word);
        if (compareResult < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
    }
    boolean FindWord(String word) {
        Node current = root;

        while (current != null) {
            int compareResult = word.compareTo(current.word);

            if (compareResult == 0) {
                return true;
            } else if (compareResult < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return false;
    }
    Node delete(Node root, String word) {
        Node current = root;
        Node parent = null;

        while (current != null && !current.word.equals(word)) {
            parent = current;

            int compareResult = word.compareTo(current.word);
            if (compareResult < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (current == null) {
            return root;
        }

        if (current.left == null) {
            if (parent == null) {
                return current.right;
            }
            if (parent.left == current) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        } else if (current.right == null) {
            if (parent == null) {
                return current.left;
            }
            if (parent.left == current) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        } else {
            Node minValueNode = MinVal(current.right);
            current.word = minValueNode.word;
            current.right = delete(current.right, minValueNode.word);
        }

        return root;
    }

    Node MinVal(Node root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
    int Prefix(String prefix) {
        return wordsWithPrefix(root, prefix);
    }

    int wordsWithPrefix(Node root, String prefix) {
        if (root == null) {
            return 0;
        }

        int count = 0;
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (current.word.startsWith(prefix)) {
                count++;
            }

            if (current.left != null && prefix.compareTo(current.word) < 0) {
                stack.push(current.left);
            }

            if (current.right != null && prefix.compareTo(current.word) >= 0) {
                stack.push(current.right);
            }
        }
        return count;
    }


    void inorder() {
        inorderRec(root);
    }

    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.word + " ");
            inorderRec(root.right);
        }
    }

    void TreeConsole() {
        Tree(root, 0);
    }

    void Tree(Node root, int spaces) {
        if (root == null)
            return;

        spaces += 5;

        Tree(root.right, spaces);

        System.out.print("\n");
        for (int i = 5; i < spaces; i++)
            System.out.print(" ");

        System.out.print(root.word + "\n");

        Tree(root.left, spaces);
    }

}

public class Main {
    public static void main(String[] args) {
        MyBstTree dictionary = new MyBstTree();

        String filePath = "D:/IDEA java/ASD/ASD 4.1/BST/test.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                char instruction = line.charAt(0);
                String word = line.substring(2);

                switch (instruction) {
                    case 'W':
                        dictionary.add(word);
                        break;
                    case 'U':
                        dictionary.root = dictionary.delete(dictionary.root, word);
                        break;
                    case 'S':
                        boolean exists = dictionary.FindWord(word);
                        System.out.println("Слово '" + word + "' " + (exists ? "существует" : "не существует"));
                        break;
                    case 'L':
                        int count = dictionary.Prefix(word);
                        System.out.println("Количество слов с префиксом '" + word + "': " + count);
                        break;
                    default:
                        System.out.println("Некорректная инструкция: " + instruction);
                        break;
                }
            }
            System.out.println("Структура дерева BST:");
            dictionary.TreeConsole();
            //dictionary.inorder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
