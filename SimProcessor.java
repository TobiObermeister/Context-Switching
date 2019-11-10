package Assignment3;
import java.util.Random;
/**
 * 
 * @author Tobie Obermeister
 *
 */
public class SimProcessor {
	
	private SimProcess currentProcess;
	private int register1;
	private int register2;
	private int register3;
	private int register4;
	private int currInstruction;
	
	public SimProcessor() {
		
	}
	
	Random random = new Random();

	public int getRegister1() {
		return random.nextInt();
	}

	public int getRegister2() {
		return random.nextInt();
	}

	public int getRegister3() {
		return random.nextInt();
	}

	public int getRegister4() {
		return random.nextInt();
	}

	public void setRegister1(int register1) {
		this.register1 = register1;
	}

	public void setRegister2(int register2) {
		this.register2 = register2;
	}

	public void setRegister3(int register3) {
		this.register3 = register3;
	}

	public void setRegister4(int register4) {
		this.register4 = register4;
	}
	
	public int getCurrInstruction() {
			return currInstruction;
	}
	
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}

	public ProcessState executeNextInstruction() {
		ProcessState result = currentProcess.execute(currInstruction);
		currInstruction++;
		return result;
	}

	public void setCurrentProcess(SimProcess current) {
		this.currentProcess=current;		
	}
	
	public int getCurrentProcess() {
		return currentProcess.getPid();
	}

	
}
