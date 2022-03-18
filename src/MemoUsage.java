import java.io.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Random;

public class MemoUsage {

    private int page = 640; //кол-во байт в страницк
    private int pageInByte = 128; //кол-во элементов в странице
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
    public void fileGenerator(int elements) throws IOException { //заполнение
        File newFile = new File("src/test.dat");
        raf = new RandomAccessFile("src/test.dat","rw");
        pages = elements/pageInByte;
        for(int i = 0; i<pages;i++){ //сначала битовая карта
            for(int j =0; j<pageInByte; j++){
                raf.writeBoolean(true);
            }
            for(int c = 0; c<pageInByte; c++){ //потом инты
                raf.writeInt(rand.nextInt());
            }
        }
        lastPageElements = elements - pages*pageInByte;
        if(lastPageElements != 0) { //ну тут просто высчитывается кол-во элементов на последней странице и таким же алгоритмом
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

            raf.seek(pageNum*page); //ставим точку на определенный номер байта
            for(int i = 0; i< bMap.length;i++){ //и выкачиваем сначала битовую карту
                bMap[i] = raf.readBoolean();
            }
            for(int i = 0; i< arr.length;i++){//потом инты(сверяемся с битовой картой)
                if(bMap[i]==false) {arr[i] = 0; raf.readInt();}
                else arr[i] = raf.readInt();
            }
    }
    public void writePage() throws IOException {

        raf.seek(page*pageNum);//записываем сначала карту потом значения
        for(int i=0; i<pageInByte;i++) {raf.writeBoolean(bMap[i]);}
        for(int i = 0; i<pageInByte; i++){ raf.writeInt(arr[i]);}

    }
    public void readElement(int index,int pageIndex) throws Exception {
        readPage(pageIndex);
        if(index<0||index>127){
            throw new Exception("В странице 128 элементов");
        }
        if (bMap[index] == false)

            throw new Exception("Пустая ячейка");

        else

            System.out.println(arr[index]);
    }
    public void SetElement(int element, int index,int pageIndex) throws Exception {
        if(index<0||index>127){
            throw new Exception("В странице 128 элементов");
        }
        readPage(pageIndex);

        if(arr[index]!= 0){//подтверждение действия
            System.out.println("В ячейке уже находится значение, перезаписать?\n 1 - yes \n anything different - no");
            char c = (char)System.in.read();
            switch(c){
                case '1':
                    arr[index] = element;
                    bMap[index] = true;
                    writePage();
                    break;
                default:
                    System.out.println("Отмена");
                    break;

            }
        }



    }
    public void RemoveAt(int index, int pageIndex) throws Exception {

        if (index < 0 || index > 127) {
            throw new Exception("В странице 128 элементов");
        }
        readPage(pageIndex);
        if (arr[index] != 0) {
            System.out.println("В ячейке находится значение, удалить??\n 1 - yes \n anything different - no");
            char c = (char) System.in.read();
            switch (c) {
                case '1':
                    bMap[index] = false; //просто ставим фолс на биткарте, когда будет запсываться значение автоматом как 0 запишется
                    writePage();
                    break;
                default:
                    System.out.println("Отмена");
                    break;

            }
        }
    }
    public void close() throws IOException {
        raf.close();
    }


}
