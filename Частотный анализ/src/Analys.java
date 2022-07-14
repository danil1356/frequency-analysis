import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Analys {
    public static void main(String[] args) {
        while (true) {
            System.out.println("1 - исслодавание");
            System.out.println("2 - взлом с последующим изменением таблицы шифрования");
            System.out.println("Введите число");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            if (option == 1) {
                new Analys().optionFirst();
            } else if (option == 2) {
                new Analys().optionSecond();
            } else
                System.out.println("Неправильный ввод");
        }
    }

    public void optionFirst() {
        //fileGeneration();
        //encrypt(new File("Pushkin.txt"));
        String pushkinText = readFromPushkin();
        String randomText = readFromRandomText();
        int i = 1000;
        int index = 0;

        int x[] = new int[pushkinText.length() / 1000];
        int y[] =  new int[pushkinText.length() / 1000];

         while(i < pushkinText.length()){
            HashMap map = explore(pushkinText.substring(0,i),randomText);
            x[index] = i / 10;
            y [index] = percentOfEqualAlphabet(map);
             System.out.println(y[index]);
            i+=1000;
            index++;

            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String str = new String();
            for(int j = 0; j < alphabet.length(); j++){
                str += map.get(alphabet.toLowerCase().charAt(j)).toString();
            }
             writeNewTable(str.toUpperCase());
        }
         drawGraphic(x,y);
    }

    public void optionSecond() {
        System.out.println("Введите зашифрованный текст");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        StringBuffer message = new StringBuffer(str);
        System.out.println("Расшифрованный текст");
        String s = message.toString();
        System.out.println(decrypt(message));

        while(true){
            System.out.println("Введите два символа");
            Scanner scanner1 = new Scanner(System.in);
            String string = scanner.nextLine();
            if(string.equals("exit"))
                break;
            char firstChar = string.toUpperCase().charAt(0);
            char secondChar = scanner1.nextLine().toUpperCase().charAt(0);
            int indexOfFirst = getIndexOfCharFromAlphabet(firstChar);
            int indexOfSecond = getIndexOfCharFromAlphabet(secondChar);
            String table = readTable2();
            StringBuffer tableStringBuffer = new StringBuffer(table);
            tableStringBuffer.setCharAt(indexOfFirst,secondChar);
            tableStringBuffer.setCharAt(indexOfSecond,firstChar);
            writeNewTable(tableStringBuffer.toString());
            StringBuffer newMessage = new StringBuffer(s);
            System.out.println(decrypt(newMessage));
            System.out.println(s);
        }
        printTable();
        System.out.println("Расшифрованный текст");
        System.out.println(message);
        System.out.println();
    }

    private HashMap explore(String s1, String s2) {
        HashMap map = generateMap();
        for(int i = 0; i < s1.length();i++){
            if(map.containsKey(s1.toLowerCase().charAt(i))) {
                int value = (int)map.get(s1.toLowerCase().charAt(i));
                map.replace(s1.toLowerCase().charAt(i),value + 1);
            }
        }

        HashMap mapRandomText = generateMap();
        for(int i = 0; i < s2.length();i++){
            if(mapRandomText.containsKey(s2.toLowerCase().charAt(i))) {
                int value = (int)mapRandomText.get(s2.toLowerCase().charAt(i));
                mapRandomText.replace(s2.toLowerCase().charAt(i),value + 1);
            }
        }

        ArrayList<Object> arrayList = (ArrayList<Object>)map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());

        ArrayList<Object>arrayList1 =(ArrayList<Object>) mapRandomText.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());
        HashMap mapTable = new HashMap();

        int index = 0;
        for(Object i : arrayList){
            mapTable.put(arrayList1.get(index).toString().charAt(0),arrayList.get(index).toString().charAt(0));
            index++;
        }

        return mapTable;
    }

    private void fileGeneration() {
        File file = new File("Table.txt");
        StringBuffer stringBuffer = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        int alphabet = 26;
        try {
            FileWriter writer = new FileWriter(file);
            while (alphabet > 0) {
                int index = (int) (Math.random() * alphabet);
                writer.write(stringBuffer.charAt(index));
                alphabet--;
                stringBuffer.deleteCharAt(index);
            }
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String readFromPushkin(){
        BufferedReader reader;
        StringBuffer str = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(new File("Pushkin.txt")));
            String string = "";
            while ((string = reader.readLine()) != null) {
                str.append(string);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return str.toString();
    }

    private String readFromRandomText(){
        BufferedReader reader;
        StringBuffer str = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(new File("RandomText.txt")));
            String string = "";
            while ((string = reader.readLine()) != null) {
                str.append(string);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return  str.toString();
    }

    private void encrypt(File file) {
        BufferedReader reader;
        StringBuffer str = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String string = "";
            while ((string = reader.readLine()) != null) {
                str.append(string);
            }
            writeToFile(encryptText(str));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String encryptText(StringBuffer message){
        StringBuffer string = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("Table.txt")));
            String str = reader.readLine();
            for(int i = 0; i < message.length(); i++){
                int index = -1;
                boolean isUpperCase = Character.isUpperCase(message.charAt(i));

                if(!isUpperCase)
                    message.setCharAt(i,Character.toUpperCase(message.charAt(i)));

                for(int j = 0; j < string.length(); j++){
                    if(message.charAt(i) == string.charAt(j)) {
                        index = j;
                        break;
                    }
                }
                if(index > -1) {
                    if(isUpperCase)
                        message.setCharAt(i, str.charAt(index));
                    else
                        message.setCharAt(i,Character.toLowerCase(str.charAt(index)));
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return message.toString();
    }
    
    private void writeToFile(String str){
        try {
            FileWriter writer = new FileWriter(new File("Pushkin.txt"));
            writer.write(str);
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private HashMap<Character,Integer> generateMap(){
        HashMap map = new HashMap<Character,Integer>();
        map.put('a',0);
        map.put('b',0);
        map.put('c',0);
        map.put('d',0);
        map.put('e',0);
        map.put('f',0);
        map.put('g',0);
        map.put('h',0);
        map.put('i',0);
        map.put('j',0);
        map.put('k',0);
        map.put('l',0);
        map.put('m',0);
        map.put('n',0);
        map.put('o',0);
        map.put('p',0);
        map.put('q',0);
        map.put('r',0);
        map.put('s',0);
        map.put('t',0);
        map.put('u',0);
        map.put('v',0);
        map.put('w',0);
        map.put('x',0);
        map.put('y',0);
        map.put('z',0);
        return map;
    }

    private int percentOfEqualAlphabet(HashMap map){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String encryptAlphabet = readTable();
        int i = 0;
        int count = 0;

        while(i < alphabet.length()){
            if(map.get(alphabet.toLowerCase().charAt(i)).equals(encryptAlphabet.toLowerCase().charAt(i))){
                count++;
            }
            i++;
        }
        return count * 100 / 26;
    }

    private String readTable(){
        String str = new String();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("Table.txt")));
            str = reader.readLine();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    private String readTable2(){
        String str = new String();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("Table2.txt")));
            str = reader.readLine();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    private void drawGraphic(int [] x, int [] y){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Graphic drawPanel = new Graphic(x,y);
        frame.getContentPane().add(drawPanel);
        frame.setSize(100,500);
        frame.setVisible(true);
    }

    public String decrypt(StringBuffer message){
        StringBuffer string = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("Table2.txt")));
            String str = reader.readLine();
            for(int i = 0; i < message.length(); i++) {
                int index = -1;
                boolean isUpperCase = Character.isUpperCase(message.charAt(i));

                if (!isUpperCase)
                    message.setCharAt(i, Character.toUpperCase(message.charAt(i)));

                for (int j = 0; j < str.length(); j++){
                    if(message.charAt(i) == str.charAt(j))
                        index = j;
                }

                if(index > -1){
                    if(isUpperCase)
                        message.setCharAt(i,string.charAt(index));
                    else
                        message.setCharAt(i,Character.toLowerCase(string.charAt(index)));
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return message.toString();
    }

    private int getIndexOfCharFromAlphabet(char c){
        int index = -1;
        Character.toUpperCase(c);
        String str = readTable2();

        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == c)
                index = i;
        }

        return index;
    }

    private void writeNewTable(String str){
        try {
            FileWriter writer = new FileWriter(new File("Table2.txt"));
            writer.write(str);
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void printTable(){
        StringBuffer string = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String str = readTable2();
        for(int i = 0; i < str.length(); i++){
            System.out.println(string.charAt(i) + "-> " + str.charAt(i));
        }
    }
}

class Graphic extends JPanel{
    int x[];
    int y[];
    public Graphic(int []x, int [] y){
        this.x = x;
        this.y = y;
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.BLUE);
        g.drawPolyline(x,y,x.length);
    }
}
