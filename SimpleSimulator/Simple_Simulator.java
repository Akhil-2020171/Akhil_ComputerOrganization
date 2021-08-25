import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class Simple_Simulator{

    public static int data; // total number of lines
    public static int ld = 0;//

    public static HashMap<String,String> instructionA = new HashMap<String,String>();
    public static HashMap<String,String> instructionB = new HashMap<String,String>();
    public static HashMap<String,String> instructionC = new HashMap<String,String>();
    public static HashMap<String,String> instructionD = new HashMap<String,String>();
    public static HashMap<String,String> instructionE = new HashMap<String,String>();
    public static HashMap<String,String> instructionF = new HashMap<String,String>();
    public static HashMap<String,String> register     = new HashMap<String,String>();
    
    public static HashMap<String,String> memory   = new HashMap<String,String>();
    public static HashMap<String,String> regValue = new HashMap<String,String>();
    
    public static void main(String[] args) throws IOException {
        // Registers
        register.put("R0","000");
        register.put("R1","001");
        register.put("R2","010");
        register.put("R3","011");
        register.put("R4","100");
        register.put("R5","101");
        register.put("R6","110");
        register.put("FLAGS","111");

        // type A
        instructionA.put("add", "00000");
        instructionA.put("sub", "00001");
        instructionA.put("mul", "00110");
        instructionA.put("xor", "01010");
        instructionA.put("or",  "01011");
        instructionA.put("and", "01100");
 
        // type B
        instructionB.put("mov", "00010");
        instructionB.put("rs", "01000");
        instructionB.put("ls", "01001");
         
        // type C
        instructionC.put("mov", "00011");
        instructionC.put("div", "00111");
        instructionC.put("not", "01101");
        instructionC.put("cmp", "01110");
 
        // type D
        instructionD.put("ld",  "00100");
        instructionD.put("st",  "00101"); 
 
        // type E
        instructionE.put("jmp", "01111");
        instructionE.put("jlt", "10000");
        instructionE.put("jgt", "10001");
        instructionE.put("je", "10010");
 
        // type F
        instructionF.put("hlt","10011");

        // initializing the register values as 16bit 0's
        regValue.put("R0", "0000000000000000");
        regValue.put("R1", "0000000000000000");
        regValue.put("R2", "0000000000000000");
        regValue.put("R3", "0000000000000000");
        regValue.put("R4", "0000000000000000");
        regValue.put("R5", "0000000000000000");
        regValue.put("R6", "0000000000000000");
        regValue.put("FLAGS", "0000000000000000");

/////////////////////////////////////////////////////////////////////////
        Scanner input = new Scanner(System.in);

        File outputFile = new File("output.txt");
        PrintWriter output = new PrintWriter(outputFile);

        String line;
        int c = 0; // total number of lines

        while (input.hasNextLine()) {
            line = input.nextLine();
            c++;
            output.println(line);
        }
        data = c;

        input.close();
        output.close();

        Scanner scan1 = new Scanner(outputFile);
        Scanner scan2 = new Scanner(outputFile);

        MultiplePrint(scan1);
        SinglePrint(scan2);
        plot();
        scan1.close();
        scan2.close();

        System.exit(0);
    }

    private static void MultiplePrint(Scanner scan){
        String tempFlags;

        for(int i = 0; i<data ; i++){
            tempFlags = regValue.get(("FLAGS"));

            String s = scan.nextLine();
            String sub = s.substring(0,5);
            String PC = String.format("%8s",Integer.toBinaryString(i)).replaceAll(" ","0");
            Operation op = new Operation();

            if(instructionA.containsValue(sub)){
                String reg1 = s.substring(7,10);
                String reg2 = s.substring(10,13);
                String reg3 = s.substring(13);

                String r1 = "";
                String r2 = "";
                String r3 = "";

                for(Entry<String,String> entry : register.entrySet()){
                    String p = entry.getValue();
                    if(p.equals(reg1)){
                        r1 = r1+  entry.getKey();
                    }
                    if(p.equals(reg2)){
                        r2 = r2 + entry.getKey();
                    }
                    if(p.equals(reg3)){
                        r3 =  r3 + entry.getKey();
                    }
                }

                int v1 = Integer.parseInt(regValue.get(r2),2);
                int v2 = Integer.parseInt(regValue.get(r3),2);

                // for ADDITION
                if(sub.equals("00000")){
                    int value = v1+v2;
                    if(value>=Math.pow(2,16)){
                        regValue.replace("FLAGS","0000000000001000");
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                // for SUBTRACTION
                if(sub.equals("00001")){
                    int value = v1-v2;
                    if(value<0){
                        regValue.replace("FLAGS","0000000000001000");   
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                // for MULTIPICATION
                if(sub.equals("00110")){
                    int value = v1*v2;
                    if(value>=Math.pow(2,16)){
                        regValue.replace("FLAGS","0000000000001000");
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                // for Exclusive OR
                if(sub.equals("01010")){
                    String V1 = regValue.get(r2);
                    String V2 = regValue.get(r3);
                    StringBuffer sb = new StringBuffer();
                    
                    for (int j = 0; j < 16; j++) {
                       sb.append(V1.charAt(i)^V2.charAt(i));
                    }

                    int value = Integer.parseInt(sb.toString(),2);
                    if(value>=Math.pow(2,16)){
                        regValue.replace("FLAGS","0000000000001000");
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                // for OR
                if(sub.equals("01011")){
                    String V1 = regValue.get(r2);
                    String V2 = regValue.get(r3);
                    StringBuffer sb = new StringBuffer();
                    
                    for (int j = 0; j < 16; j++) {
                       sb.append(V1.charAt(i)|V2.charAt(i));
                    }

                    int value = Integer.parseInt(sb.toString(),2);
                    if(value>=Math.pow(2,16)){
                        regValue.replace("FLAGS","0000000000001000");
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                // for AND
                if(sub.equals("01100")){
                    String V1 = regValue.get(r2);
                    String V2 = regValue.get(r3);
                    StringBuffer sb = new StringBuffer();
                    
                    for (int j = 0; j < 16; j++) {
                       sb.append(V1.charAt(i)&V2.charAt(i));
                    }

                    int value = Integer.parseInt(sb.toString(),2);
                    if(value>=Math.pow(2,16)){
                        regValue.replace("FLAGS","0000000000001000");
                    }
                    else{
                        String v = String.format("%16s",Integer.toBinaryString(value)).replaceAll(" ","0");
                        regValue.replace(r1, v);
                    }
                }
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeA(regValue);
                System.out.println(k);
            }
            else if(instructionB.containsValue(sub)){
                String reg1 = s.substring(5, 8);
                String imm = s.substring(8);

                String r1 = "";

                for(Entry<String,String> entry : register.entrySet()){
                    String p = entry.getValue();
                    if(p.equals(reg1)){
                        r1 =  r1 + entry.getKey();
                    }
                }
                // for mov instruction
                if(sub.equals("00010")){
                    int im = Integer.parseInt(imm,2);
                    String value = String.format("%16s",Integer.toBinaryString(im)).replaceAll(" ","0");
                    regValue.replace(r1, value);
                }
                // for right shift
                if(sub.equals("01000")){
                    int r = Integer.parseInt(regValue.get(r1),2);
                    int im = Integer.parseInt(imm,2);
                    int v = r>>im;
                    String value = String.format("%16s",Integer.toBinaryString(v)).replaceAll(" ","0");
                    regValue.replace(r1, value);
                }
                // for left shift
                if(sub.equals("01001")){
                    int r = Integer.parseInt(regValue.get(r1),2);
                    int im = Integer.parseInt(imm,2);
                    int v = r<<im;
                    String value = String.format("%16s",Integer.toBinaryString(v)).replaceAll(" ","0");
                    regValue.replace(r1, value);
                }
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeB(regValue);
                System.out.println(k);
            }
            else if(instructionC.containsValue(sub)){
                String reg1 = s.substring(10, 13);
                String reg2 = s.substring(13);

                String r1 = "";
                String r2 = "";

                for(Entry<String,String> entry : register.entrySet()){
                    String p = entry.getValue();
                    if(p.equals(reg1)){
                        r1 =  r1 + entry.getKey();
                    }
                    if(p.equals(reg2)){
                        r2 =  r2 + entry.getKey();
                    }
                }

                // for mov instruction
                if(sub.equals("00011")){
                    String value = regValue.get(r2);
                    regValue.replace(r1, value);
                }
                // for div instruction
                if(sub.equals("00111")){
                    int v1 = Integer.parseInt(regValue.get(r1),2);
                    int v2 = Integer.parseInt(regValue.get(r2),2);
                    int quotient = v1/v2;
                    int remainder= v1%v2;
                    String q = String.format("%16s",Integer.toBinaryString(quotient)).replaceAll(" ","0");
                    String r = String.format("%16s",Integer.toBinaryString(remainder)).replaceAll(" ","0");

                    regValue.replace("R0",q);
                    regValue.replace("R1",r);
                }
                // for not instruction
                if(sub.equals("01101")){
                    StringBuffer sb = new StringBuffer();
                    String x = regValue.get(r2);
                    for (int j = 0; j < 16; j++) {
                       sb.append(~x.charAt(i));
                    }
                    String value = sb.toString();
                    regValue.replace(r1, value);
                }
                // for cmp instruction
                if(sub.equals("01110")){
                    int v1 = Integer.parseInt(regValue.get(r1),2);
                    int v2 = Integer.parseInt(regValue.get(r2),2);

                    if(v1>v2){
                        regValue.replace("FLAGS","0000000000000010");
                    }
                    else if(v1<v2){
                        regValue.replace("FLAGS","0000000000000100");
                    }
                    else if(v1==v2){
                        regValue.replace("FLAGS","0000000000000001");
                    }
                }
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeC(regValue);
                System.out.println(k);
            }
            else if(instructionD.containsValue(sub)){
                String reg1 = s.substring(5,8);
                String mem_addr = s.substring(8);
                memory.put(mem_addr,null);

                String r1 = "";

                for(Entry<String,String> entry : register.entrySet()){
                    String p = entry.getValue() ;
                    if(p.equals(reg1)){
                        r1 = r1 + entry.getKey();
                    }
                }

                // for str instruction 
                if(sub.equals("00101")){
                    String d = regValue.get(r1);
                    memory.replace(mem_addr,d);
                    ld++;
                }

                // for ld instruction
                if(sub.equals("00100")){
                    String d = memory.get(mem_addr);
                    regValue.replace(r1,d);
                }
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeD(regValue);
                System.out.println(k);
            }
            else if(instructionE.containsValue(sub)){
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeE(regValue);
                System.out.println(k);
            }
            else if(instructionF.containsValue(sub)){
                if(regValue.get("FLAGS")==tempFlags){
                    regValue.replace("FLAGS","0000000000000000");
                }
                String k = PC+" "+op.TypeF(regValue);
                System.out.println(k);
            }
            else {
                PC = String.format("%8s",Integer.toBinaryString(0)).replaceAll(" ","0");
                String k = PC ;
                System.out.println(k);
            }
        }
    }

    private static void SinglePrint(Scanner scan){
        for(int i = 0 ; i<data ; i++){
            String line = scan.nextLine();
            System.out.println(line);
        }
        for(Entry<String,String> entry : memory.entrySet()){
            String p = entry.getValue();
            System.out.println(p);
        }
        for(int i = data+ld ; i<256 ; i++){
            String line = "0000000000000000";
            System.out.println(line);
        }
    }

    private static void plot() throws IOException{
        File New = new File("New_Output.txt");
        PrintWriter plot = new PrintWriter(New);
        plot.println(data);
        plot.close();
    }
}

class Operation{
    String TypeA(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
    String TypeB(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
    String TypeC(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
    String TypeD(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
    String TypeE(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
    String TypeF(HashMap<String, String> regValue){
        String k = regValue.get("R0")+" "+regValue.get("R1")+" "+regValue.get("R2")+" "+regValue.get("R3")+" "+regValue.get("R4")+" "+regValue.get("R5")+" "+regValue.get("R6")+" "+regValue.get("FLAGS");
        return k;
    }
}