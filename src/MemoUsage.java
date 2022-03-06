import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class MemoUsage {

    private int page = 640;
    private int pageInByte = 128;
    private int pageNum = 0;
    int pages;
    int lastPageElements;
    private int[] arr;
    private boolean[] bMap;
    Random rand = new Random();
    RandomAccessFile raf;
    public MemoUsage(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }
    public void fileGenerator(int elements) throws IOException {
        File newFile = new File("src/test.dat");
        raf = new RandomAccessFile("src/test.dat","rw");
        pages = elements/pageInByte;
        for(int i = 0; i<pages;i++){
            for(int j =0; j<pageInByte; j++){
                raf.writeBoolean(true);
            }
            for(int c = 0; c<pageInByte; c++){
                raf.writeInt(rand.nextInt());
            }
        }
        lastPageElements = elements - pages*pageInByte;
        if(lastPageElements != 0) {
                for(int j =0; j<pageInByte; j++){
                    if(j<lastPageElements) {
                        raf.writeBoolean(true);
                    }else raf.writeBoolean(false);
                }
                for(int c = 0; c<pageInByte; c++){
                    if(c<lastPageElements) {
                        raf.writeInt(rand.nextInt());
                    }else raf.writeInt(0);
                }

        }
    }
    public int GetPageNum()
    {
        return pageNum;
    }
    public void readPage(int pageIndex) throws IOException {
        if(pageIndex>pages){
            if(lastPageElements!=0){
                pageNum = pages+1;
            }
            else pageNum = pages;
            System.out.println("Загружена последняя страница #" + pageNum);
            pageNum--;
        }else pageNum=pageIndex;
        bMap = new boolean[pageInByte];
        arr = new int[pageInByte];

            raf.seek(pageNum*page);
            for(int i = 0; i< bMap.length;i++){
                bMap[i] = raf.readBoolean();
            }
            for(int i = 0; i< arr.length;i++){
                if(bMap[i]==false) {arr[i] = 0; raf.readInt();}
                else arr[i] = raf.readInt();
            }
    }
    public void writePage() throws IOException {

        raf.seek(page*pageNum);
        for(int i=0; i<pageInByte;i++) {raf.writeBoolean(bMap[i]);}
        for(int i = 0; i<pageInByte; i++){ raf.writeInt(arr[i]);}

    }
    public void readElement(int index,int pageIndex) throws Exception {
        readPage(pageIndex);

        if (bMap[index] == false)

            throw new Exception("Пустая ячейка");

        else

            System.out.println(arr[index]);
    }
    public void SetElement(int element, int index,int pageIndex) throws IOException {

        readPage(pageIndex);

        arr[index] = element;

        bMap[index] = true;

        writePage();

    }
    public void RemoveAt(int index, int pageIndex) throws IOException {

        readPage(pageIndex);

        bMap[index] = false;

        writePage();

    }
    public void close() throws IOException {
        raf.close();
    }


}
