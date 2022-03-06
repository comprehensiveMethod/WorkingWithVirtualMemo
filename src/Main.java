import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int index,element,pageIndex;
        MemoUsage memoUsage = new MemoUsage("src/1000.dat");
        System.out.println("Choose the action: \n 1- Use a starter file\n 2- Create a new file");
        char c = (char)System.in.read();
        switch (c){
            case'1': break;
            case'2':
                System.out.println("Input the number of elements");
                int elements = scanner.nextInt();
                memoUsage.fileGenerator(elements);
        }
        for(;;){
            System.out.println("Choose the action: ");
            System.out.println("1 - Read element on a certain number\n" +
                    "2 - Set element on a certain number\n" +
                    "3 - Remove element on a certain number\n" +
                    "9 - exit");
            char ch = (char)System.in.read();
            switch(ch){
                case '1':
                    System.out.println("Write down the page");
                    pageIndex = scanner.nextInt();
                    System.out.println("Write down the number of element");
                    index = scanner.nextInt();
                    System.out.println("the element is:");
                    try {
                        memoUsage.readElement(index,pageIndex);
                    }catch (Exception e){
                        System.out.println("Error: "+e.getMessage());
                    }
                    break;
                case '2':
                    System.out.println("Write down the page");
                    pageIndex = scanner.nextInt();
                    System.out.println("Write down the index of element");
                    index = scanner.nextInt();
                    System.out.println("Now write the element that will be placed instead of current element:");
                    element = scanner.nextInt();
                    memoUsage.SetElement(element,index,pageIndex);
                    break;
                case '3':
                    System.out.println("Write down the page");
                    pageIndex = scanner.nextInt();
                    System.out.println("Write down the index of element");
                    index = scanner.nextInt();
                    memoUsage.RemoveAt(index,pageIndex);
                    break;
                case '9':
                    memoUsage.close();
                    return;
                default:
                    break;
            }
        }






    }
}
