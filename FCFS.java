import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

class FCFS {
    static String file;
    public static void main(String[] args){
        if(args.length==1){
            file = args[0];
            String line;
            ArrayList<Process> executingProcesses = new ArrayList<>();
            ArrayList<Process> finishedProcesses = new ArrayList<>();
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                while((line = br.readLine())!=null){
                    String[] processInfo = line.split(":");
                    String processID = processInfo[0];
                    String[] processBurstsArray = processInfo[1].split(";");
                    ArrayList<String> processBursts = new ArrayList<>();
                    processBursts.addAll(Arrays.asList(processBurstsArray));
                    Process process = new Process(processID,processBursts);
                    executingProcesses.add(process);
                }
            }
            catch(Exception e){
                System.out.println("Error Reading the File");
            }
            int time=0;
            System.out.println("Time\tPID\tBurst\tReturnTime\tWaitingTime");
            while(!executingProcesses.isEmpty()){
                for(int i=0;i<executingProcesses.size();){
                    if(executingProcesses.get(i).returnTime<=time){
                        String[] burst = executingProcesses.get(i).burst.get(0).split(",");
                        int cpuBurst = Integer.parseInt(burst[0].substring(1));
                        int ioBurst = Integer.parseInt(burst[1].substring(0,burst[1].length()-1));
                        executingProcesses.get(i).waitingTime += time - (executingProcesses.get(i).previousTimeEntry+executingProcesses.get(i).previousCpuBurst);
                        executingProcesses.get(i).previousTimeEntry = time;
                        executingProcesses.get(i).previousCpuBurst = cpuBurst;
                        if(ioBurst==-1){
                            executingProcesses.get(i).returnTime=time+cpuBurst;
                            finishedProcesses.add(executingProcesses.get(i));
                            System.out.println(time+"\t"+executingProcesses.get(i).id+"\t"+cpuBurst+","+ioBurst+"\t"+executingProcesses.get(i).returnTime+"\t\t"+executingProcesses.get(i).waitingTime);
                            executingProcesses.remove(i);
                        }
                        else{
                            executingProcesses.get(i).returnTime=time+cpuBurst+ioBurst;
                            System.out.println(time+"\t"+executingProcesses.get(i).id+"\t"+cpuBurst+","+ioBurst+"\t"+executingProcesses.get(i).returnTime+"\t\t"+executingProcesses.get(i).waitingTime);
                            executingProcesses.get(i).burst.remove(0);
                        }
                        time+=cpuBurst;
                        i=0;
                    }
                    else i++;
                }
                time++;
            }
            int turnAroundSum=0;
            int waitingTimeSum=0;
            for(int i=0;i<finishedProcesses.size();i++){
                turnAroundSum+=finishedProcesses.get(i).returnTime;
                waitingTimeSum+=finishedProcesses.get(i).waitingTime;
            }
            double avgTurnAround = turnAroundSum/(double)finishedProcesses.size();
            double avgWaitingTime = waitingTimeSum/(double)finishedProcesses.size();
            System.out.println("\navgTurnAround: "+String.format("%.2f", avgTurnAround)+"\navgWaitingTime: "+String.format("%.2f", avgWaitingTime));
        }
        else{
            System.out.println("Please provide name/path of the txt file!");
            System.exit(0);
        }
    }
}
class Process{
    String id;
    ArrayList<String> burst;
    int returnTime = 0;
    int waitingTime = 0;
    int previousCpuBurst = 0;
    int previousTimeEntry = 0;
    public Process(String id, ArrayList<String> burst){
        this.id = id;
        this.burst = burst;
    }
}