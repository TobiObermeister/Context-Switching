package Assignment3;
import java.util.*;
/**
 * 
 * @author Tobie Obermeister
 *
 */
public class SimProcess {

	private int pid;
	private String procName;
	private int totalInstructions;
	
	public SimProcess() {
		this(0,null,0);
	}
	
	public SimProcess(int pid, String procName, int totalInstructions) {
		this.pid=pid;
		this.procName=procName;
		this.totalInstructions=totalInstructions;
		
	}
	public int getPid() {
		return pid;
	}
		
	public ProcessState execute (int i) {
		System.out.println("PID: " + pid + " Process Name: " + procName + " Excecuting Instruction: " + (i+1));
		if (i >= (totalInstructions-1)) {
			return ProcessState.FINISHED;
		}
		else {
			Random random = new Random();
			Double probability = random.nextDouble();
			if (probability < 0.15) {
				return ProcessState.BLOCKED;
			}
			else {
				return ProcessState.READY;
			}

	}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("PID "+ pid);
		sb.append("Process Name "+ procName);
		sb.append("total Ins" + totalInstructions);
		return sb.toString();
	}
}
