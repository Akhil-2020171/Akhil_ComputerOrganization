import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Simple_Assembler{
    public static int data;
    public static HashMap<String,String> instructionA = new HashMap<String,String>();
    public static HashMap<String,String> instructionB = new HashMap<String,String>();
    public static HashMap<String,String> instructionC = new HashMap<String,String>();
    public static HashMap<String,String> instructionD = new HashMap<String,String>();
    public static HashMap<String,String> instructionE = new HashMap<String,String>();
    public static HashMap<String,String> instructionF = new HashMap<String,String>();
    public static HashMap<String,Integer> Variables   = new HashMap<String,Integer>(); // for handling variables
    public static HashMap<String,Integer> Labels      = new HashMap<String,Integer>(); // for  handling labels
    public static HashMap<String,String> register     = new HashMap<String,String>();
    public static void main(String[] args) throws IOException{
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

        Scanner input = new Scanner(System.in);

        File outputFile = new File("output.txt");
        PrintWriter output = new PrintWriter(outputFile);

        String line;
        int c = 0; // total number of lines
        int v = 0 ;// number of variable statements

        while (input.hasNextLine()) {
            line = input.nextLine();
            String[] s = line.split(" ");
            if(s.length==2){
                if(s[0].equals("var")){
                    Variables.put(s[1],c);
                    v++;
                }
            }
            if(s[0].endsWith(":")){
                s[0]= s[0].substring(0,s[0].length()-1);
                Labels.put(s[0],c-v);
            }
            c++;
            output.println(line);
        }

        input.close();
        data = c;
        output.close();
        
        Scanner sc = new Scanner(outputFile);

        Print(sc);
        System.exit(0);
    }
    
    public static void Print(Scanner scan){
        
        int t = 0 ; // last hlt
        int x = 0 ; // Variable error
        int kin = 0; // line number
        int k  = 0 ; // variable / label

        for(int i = 0 ; i<data ; i++){
            kin++;
            String[] s = scan.nextLine().split(" ");
            if(s.length==5){
                x++;
                if(s[0].endsWith(":")){
                    if(instructionA.containsKey(s[1]) && register.containsKey(s[2]) && register.containsKey(s[3]) && register.containsKey(s[4])){
                        Operation op = new Operation();
                        System.out.println(op.TypeA(instructionA,register,s[1],s[2],s[3],s[4]));
                    }
                    else if( instructionA.containsKey(s[1])==false || register.containsKey(s[2])==false || register.containsKey(s[3])==false || register.containsKey(s[4])==false){
                        if(instructionA.containsKey(s[1])==false){
                            System.out.println("Typo in Instruction in line number "+kin);
                        }
                        else System.out.println("Typo in Register name in line number "+kin); 
                    }
                }
                else if(instructionB.containsKey(s[1]) || instructionC.containsKey(s[1]) || instructionD.containsKey(s[1]) || instructionE.containsKey(s[1])){
                    System.out.println("ERROR : Wrong Syntax used in line number "+kin);
                }       
                else System.out.println("General Syntax Error in line number :"+kin); 
            }
            else if(s.length==4){
                x++;
                Operation op = new Operation();
                if(instructionA.containsKey(s[0]) && register.containsKey(s[1]) && register.containsKey(s[2]) && register.containsKey(s[3])){
                    System.out.println(op.TypeA(instructionA,register,s[0],s[1],s[2],s[3]));
                }
                else if( instructionA.containsKey(s[0])==false || register.containsKey(s[1])==false || register.containsKey(s[2])==false || register.containsKey(s[3])==false){
                        if(instructionA.containsKey(s[0])==false){
                            System.out.println("ERROR : Typo in Instruction in line number "+kin);
                        }
                        else System.out.println("ERROR : Typo in Register name in line number "+kin);
                }
                else{
                    if(s[0].endsWith(":")){
                        if(s[3].startsWith("$")){
                            s[3] = s[3].substring(1);
                            if(instructionB.containsKey(s[1])==false || register.containsKey(s[2]) ==false){
                                if(instructionB.containsKey(s[1]) == false){
                                    System.out.println("ERROR : Typo in Instruction in line number "+kin);
                                }
                                else{
                                    System.out.println("ERROR : Typo in Register name in line number "+kin);
                                }
                            }
                            if(instructionB.containsKey(s[1]) || instructionC.containsKey(s[1]) || instructionD.containsKey(s[1]) || instructionE.containsKey(s[1])){
                                System.out.println("ERROR : Wrong Syntax in line number "+kin);
                            }
                            try{
                                if(Integer.parseInt(s[3])<256 && Integer.parseInt(s[3])>0 && instructionB.containsKey(s[1]) && register.containsKey(s[2])){
                                    s[2] = String.format("%8s", Integer.toBinaryString(Integer.parseInt(s[2]))).replaceAll(" ", "0");
                                    System.out.println(op.TypeB(instructionB, register,s[1],s[2],s[3]));
                                }
                                else if(Integer.parseInt(s[3])<256 && Integer.parseInt(s[3])>0 && instructionB.containsKey(s[1]) && !register.containsKey(s[2])){
                                    System.out.println("ERROR : Typo in Registor name in line number "+kin);
                                }
                                else if(Integer.parseInt(s[3])<256 && Integer.parseInt(s[3])>0 && !instructionB.containsKey(s[1]) && register.containsKey(s[2])){
                                    System.out.println("ERROR : Typo in Instruction name in line number "+kin);
                                }
                                else System.out.println("ERROR : Typo in Instruction name or Register name in line number "+kin);
                            }
                            catch(Exception e){
                                System.out.println("ERROR : Illegal Immediate value in line number "+kin);
                            }
                        }
                        if(instructionC.containsKey(s[1]) && register.containsKey(s[2]) && register.containsKey(s[3])){
                            System.out.println(op.TypeC(instructionC, register , s[1] , s[2] , s[3]));
                        }
                        else if(instructionC.containsKey(s[1]) ==false || register.containsKey(s[2]) ==false || register.containsKey(s[3])==false){
                            if(instructionC.containsKey(s[1]) == false){
                                System.out.println("ERROR : Typo in Instruction in line number "+kin);
                            }
                            else System.out.println("ERROR : Typo in Register name in line number "+kin);
                        }
                        else if(instructionD.containsKey(s[1]) && register.containsKey(s[2])){
                            if(instructionD.containsKey(s[1]) && register.containsKey(s[2]) && Variables.containsKey(s[3])){
                                int value = data-1 + Variables.get(s[3]);
                                Variables.replace(s[3], value);
                                String p = String.format("%8s", Integer.toBinaryString(Variables.get(s[3]))).replaceAll(" ","0");
                                System.out.println(op.TypeD(instructionD, register,s[1],s[2],p));
                            }
                            else if(instructionD.containsKey(s[1]) && register.containsKey(s[2]) && !Variables.containsKey(s[3]) ){
                                System.out.println("ERROR : Use of Undefined Variable in line number "+kin);
                            }
                            else if(instructionD.containsKey(s[1]) && register.containsKey(s[2]) && Labels.containsKey(s[3])){
                                System.out.println("ERROR : Misuse of Label as Variable in line number "+kin);
                            }
                            else{
                                System.out.println(op.TypeD(instructionD, register, s[1],s[2], register.get(s[3])));
                            }
                        }
                        else if(instructionD.containsKey(s[1]) ==false || register.containsKey(s[2]) == false){
                            if(instructionD.containsKey(s[1]) == false){
                                System.out.println("ERROR : Typo in Instruction in line number "+kin);
                            }
                        }
                    }
                }
            }
            else if(s.length==3){
                x++;
                Operation op = new Operation();
                if(s[0].endsWith(":")){
                    if(instructionE.containsKey(s[1])){
                        if(instructionE.containsKey(s[1]) && Labels.containsKey(s[2])){
                            String p = String.format("%8s", Integer.toBinaryString(Labels.get(s[2]))).replaceAll(" ","0");
                            System.out.println(op.TypeE(instructionE, register, s[1], p));
                        }
                        else if(instructionE.containsKey(s[1]) && !Labels.containsKey(s[2])){
                            System.out.println("ERROR : Use of Undefined Label in line number "+kin);
                        }
                        else if(instructionE.containsKey(s[1]) && Variables.containsKey(s[2])){
                            System.out.println("ERROR : Misuse of Variable as Label in line number "+kin);
                        }
                        else System.out.println(op.TypeE(instructionE,register, s[1],s[2]));
                    }
                    else if(instructionE.containsKey(s[1]) ==false){
                        if(instructionE.containsKey(s[1]) == false){
                            System.out.println("ERROR : Typo in Instruction in line number "+kin);
                        }
                    }
                }
                else if(instructionD.containsKey(s[0]) && register.containsKey(s[1])){
                    if(instructionD.containsKey(s[0]) && register.containsKey(s[1]) && Variables.containsKey(s[2])){
                        int value = data-1 + Variables.get(s[2]);
                        Variables.replace(s[2], value);
                        String p = String.format("%8s", Integer.toBinaryString(Variables.get(s[2]))).replaceAll(" ","0");
                        System.out.println(op.TypeD(instructionD, register,s[0],s[1],p));
                    }
                    else if(instructionD.containsKey(s[0]) && register.containsKey(s[1]) && Labels.containsKey(s[2])){
                        System.out.println("ERROR : Misuse of Label as Variable in line number "+kin);
                    }
                    else if(instructionD.containsKey(s[0]) && register.containsKey(s[1]) && !Variables.containsKey(s[2])){
                        System.out.println("ERROR : Use of Undefined Variable in line number "+kin);
                    }
                    else if(instructionD.containsKey(s[1]) ==false || register.containsKey(s[2]) == false){
                        if(instructionD.containsKey(s[1]) == false){
                            System.out.println("ERROR : Typo in Instruction in line number "+kin);
                        }
                    }
                    else{
                        System.out.println(op.TypeD(instructionD, register, s[0],s[1], register.get(s[2])));
                    }
                }
                else if(s[2].startsWith("$")){
                    s[2] = s[2].substring(1);
                    if(instructionB.containsKey(s[0])==false || register.containsKey(s[1]) ==false){
                        if(instructionB.containsKey(s[0]) == false){
                            System.out.println("ERROR : Typo in Instruction in line number "+kin);
                        }
                        else System.out.println("ERROR : Typo in Register name in line number "+kin);
                    }
                    try{
                        if(Integer.parseInt(s[2])<256 && Integer.parseInt(s[2])>0 && instructionB.containsKey(s[0]) && register.containsKey(s[1])){
                            s[2] = String.format("%8s", Integer.toBinaryString(Integer.parseInt(s[2]))).replaceAll(" ", "0");
                            System.out.println(op.TypeB(instructionB, register,s[0],s[1],s[2]));
                        }
                        else if(Integer.parseInt(s[2])<256 && Integer.parseInt(s[2])>0 && instructionB.containsKey(s[0]) && !register.containsKey(s[1])){
                            System.out.println("ERROR : Typo in Registor name in line number "+kin);
                        }
                        else if(Integer.parseInt(s[2])<256 && Integer.parseInt(s[2])>0 && !instructionB.containsKey(s[0]) && register.containsKey(s[1])){
                            System.out.println("ERROR : Typo in Instruction name in line number "+kin);
                        }
                        else System.out.println("ERROR : Typo in Instruction name or Register name in line number "+kin);
                    }
                    catch(Exception e){
                        System.out.println("ERROR : Illegal Immediate value in line number "+kin);
                    }
                }
                else if(instructionC.containsKey(s[0]) && register.containsKey(s[1]) && register.containsKey(s[2])){
                    System.out.println(op.TypeC(instructionC, register , s[0] , s[1] , s[2]));
                }
                else if(instructionC.containsKey(s[0]) ==false || register.containsKey(s[1]) == false || register.containsKey(s[2]) == false){
                    if(instructionC.containsKey(s[0]) == false){
                        System.out.println("ERROR : Typo in Instruction in line number "+kin);
                        }
                    else System.out.println("ERROR : Typo in Register name in line number "+kin);
                }
            }
            else if(s.length==2){
                Operation op = new Operation();
                if(s[0].endsWith(":")){
                    x++;
                    if(instructionF.containsKey(s[1])){
                        System.out.println(op.TypeF(instructionF));
                        if(kin!=data){
                            System.out.println("ERROR : Hlt is not being used as Last Instuction!");
                        }
                        break;
                    }
                    else System.out.println("ERROR : Typo in Instruction in line number "+kin);
                }
                else if(s[0].equals("var")){
                    if(x==0){
                        Variables.put(s[1],k);
                        k++;
                    }
                    else if(x!=0) System.out.println("ERROR : Variable is not declared at the beginning.");
                    else continue;
                }
                else{
                    x++;
                    if(instructionE.containsKey(s[0]) && Variables.containsKey(s[1])){
                        System.out.println("ERROR : Misuse of Varaible as Label in line number "+kin);
                    }
                    else if(instructionE.containsKey(s[0]) && Labels.containsKey(s[1])){
                        String p = String.format("%8s", Integer.toBinaryString(Labels.get(s[1]))).replaceAll(" ","0");
                        System.out.println(op.TypeE(instructionE, register, s[0],p));
                    }
                    else if(instructionE.containsKey(s[0]) && !Labels.containsKey(s[1])){
                        System.out.println("ERROR : USe of Undefined Label in line number "+kin);
                    }
                    else if(!instructionE.containsKey(s[0]) && Labels.containsKey(s[1])){
                        System.out.println("ERROR : Typo in Instruction in line number "+kin);
                    }
                    else if(!instructionE.containsKey(s[0]) && !Labels.containsKey(s[1])){
                        System.out.println("ERROR : Typo in Instruction and Use of Undefined Label in line number "+kin);
                    }
                }
            }
            else if(s.length==1){
                x++;
                Operation op = new Operation();
                if(instructionF.containsKey(s[0])){
                    System.out.println(op.TypeF(instructionF));
                    if(kin!=data){
                        System.out.println("ERROR : Hlt is not being used as Last Instruction!");
                        
                    }
                    t++;
                    break;
                }
                else if(!instructionE.containsKey(s[0]) && t==0){
                    System.out.println("ERROR : Missing Hlt instrunction at the end!");
                }
                else System.out.println("ERROR : Typo in Instruction in line number : "+kin);
            }
        }
    }
}

class Operation {
    String TypeA(HashMap<String,String> A , HashMap<String,String> R, String ins ,String reg1, String reg2, String reg3){
        String k = A.get(ins)+"00"+R.get(reg1)+R.get(reg2)+R.get(reg3);
        return k;
    }

    String TypeB(HashMap<String,String> B , HashMap<String,String> R, String ins , String reg , String imm){
        String k = B.get(ins)+R.get(reg)+imm;
        return k;
    }

    String TypeC(HashMap<String,String> C , HashMap<String,String> R , String ins , String reg1 , String reg2){
        String k = C.get(ins)+"00000"+R.get(reg1)+R.get(reg2);
        return k;
    }

    String TypeD(HashMap<String,String> D , HashMap<String,String> R , String ins , String reg , String mem_addr){
        String k = D.get(ins)+R.get(reg)+mem_addr;
        return k;
    }

    String TypeE(HashMap<String,String> E , HashMap<String,String> R, String ins , String mem_addr){
        String k = E.get(ins)+"000"+mem_addr;
        return k;
    }

    String TypeF(HashMap<String,String> F){
        String k = F.get("hlt")+"00000000000";
        return k;
    }
}