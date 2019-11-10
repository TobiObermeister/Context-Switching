package Assignment3;
import java.util.ArrayList;
import java.util.Random;
/**
 * 
 * @author Tobie Obermeister
 *
 */
public class ContextSwitch {
	 static void main (String [] args ) {	 
		//create an instance of SimProcessor
		SimProcessor simProcessor = new SimProcessor();
		final int quantum = 5;
		ArrayList <SimProcess> ready = new ArrayList <SimProcess>();
		ArrayList <SimProcess> blocked = new ArrayList <SimProcess>();
		ArrayList <ProcessControlBlock> readyPcbs = new ArrayList <ProcessControlBlock>();
		ArrayList <ProcessControlBlock> blockedPcbs = new ArrayList <ProcessControlBlock>();
		
		//make 10 process add them to the ready list
		ready.add(new SimProcess(1, "Excel", 155));
		ready.add(new SimProcess(2, "Word", 212));
		ready.add(new SimProcess(3, "Powerpoint", 310));
		ready.add(new SimProcess(4, "Access", 275));
		ready.add(new SimProcess(5, "SQL Server", 163));
		ready.add(new SimProcess(6, "Eclipse", 330));
		ready.add(new SimProcess(7, "Publisher", 290));
		ready.add(new SimProcess(8, "Notepad", 380));
		ready.add(new SimProcess(9, "Java", 175));
		ready.add(new SimProcess(10, "Android Studio", 230));
		
		//create one PCB per process and add to the readyPcbs list
		for (SimProcess x : ready) {
			readyPcbs.add(new ProcessControlBlock(x));
		}		
		//set a counter to keep track of the quantum 
		int counter=0;
		for (int i = 1; i <= 3000; i++) {
			//prints each step of the 3000 loop iteration
			System.out.print("Step "+ i + " ");
			//if the ready list is not empty
			if (!ready.isEmpty()) {
					//set the current process to the first process in the ready list
					SimProcess current = ready.get(0);
					//set the processor to the current process
					simProcessor.setCurrentProcess(current);					
					//start executing instructions
					ProcessState state = simProcessor.executeNextInstruction();		
					switch (state) {					
					case FINISHED:
						//removes process from ready list and readyPcbs list
						ready.remove(0);
						readyPcbs.remove(0);
						System.out.println("***Process completed***");
						//reset quantum counter to 0
						counter = 0;
						//restores another process
						RestoreProcess(ready, blocked, readyPcbs, blockedPcbs, simProcessor);						
						break;
					case BLOCKED:
						//reset quantum counter to 0
						counter = 0;
						System.out.println("***Process Blocked***");
						SaveProcess(ready, blocked, readyPcbs, blockedPcbs, simProcessor);
						//adds the process to the blocked list and blockedPcbs list
						blocked.add(ready.get(0));
						blockedPcbs.add(readyPcbs.get(0));						
						//removed the process from the ready list and ready pcb ready list
						ready.remove(0);	
						readyPcbs.remove(0);
						//restore the next ready process
						RestoreProcess(ready, blocked, readyPcbs, blockedPcbs, simProcessor);						
						break;
					case READY:
						counter += 1;
						if (counter == quantum) {
							System.out.println("***Quantum expired***");
							SaveProcess(ready, blocked, readyPcbs, blockedPcbs, simProcessor);
							//add back to the ready list of processes and readyPcbs list
							ready.add(ready.get(0));
							readyPcbs.add(readyPcbs.get(0));							
							//remove from the place it currently is because it was added back to the end
							ready.remove(0);
							readyPcbs.remove(0);
							RestoreProcess(ready, blocked, readyPcbs, blockedPcbs, simProcessor);			
							//set the counter back to 0
							counter=0;
						}
						break;	
					default:
						System.out.println();
					}
			}
			//if the ready list is empty
			else {
				wakeUp(ready,blocked,readyPcbs,blockedPcbs,simProcessor);
			}			
		}
	}
		public static void SaveProcess(ArrayList<SimProcess>ready, ArrayList<SimProcess>blocked,ArrayList<ProcessControlBlock> readyPcbs,
				ArrayList<ProcessControlBlock> blockedPcbs, SimProcessor simProcessor) {
			
			//save all register values and curr instruction from processor to pcb			
			int reg1 = simProcessor.getRegister1();
			readyPcbs.get(0).setRegister1(reg1);
			
			int reg2 = simProcessor.getRegister2();
			readyPcbs.get(0).setRegister2(reg2);
			
			int reg3 = simProcessor.getRegister3();
			readyPcbs.get(0).setRegister3(reg3);
			
			int reg4 = simProcessor.getRegister4();
			readyPcbs.get(0).setRegister4(reg4);
			
			int curr = simProcessor.getCurrInstruction();
			readyPcbs.get(0).setCurrInstruction(curr);	
			
			System.out.println("Context switch: Saving process: " + simProcessor.getCurrentProcess());
			System.out.println(		"Instruction: "+(curr+1)+ " R1: " +reg1+ " R2:" +reg2+" R3: "+reg3+" R4: "+reg4);
		}
			
		public static void RestoreProcess(ArrayList<SimProcess>ready, ArrayList<SimProcess>blocked,ArrayList<ProcessControlBlock> readyPcbs,
					ArrayList<ProcessControlBlock> blockedPcbs, SimProcessor simProcessor) {
			
			//if the readyPcbs is not empty, restore the next ready process
			if (readyPcbs.size() != 0) {
			//restoring the next ready process
			int r1 = readyPcbs.get(0).getRegister1();
			simProcessor.setRegister1(r1);
			
			int r2 = readyPcbs.get(0).getRegister2();
			simProcessor.setRegister2(r2);
			
			int r3 = readyPcbs.get(0).getRegister3();
			simProcessor.setRegister3(r3);
			
			int r4 = readyPcbs.get(0).getRegister4();
			simProcessor.setRegister4(r4);
			
			int ins = readyPcbs.get(0).getCurrInstruction();
			simProcessor.setCurrInstruction(ins);
			
			System.out.println("Restoring process " +ready.get(0).getPid());
			System.out.println(		"Instruction "+(ins+1)	+" R1: "+r1+" R2: "+r2+	" R3: "+r3+" R4: "+r4);
			}
			//otherwise wakeUp processes from the blocked list
			else {				
				wakeUp(ready,blocked,readyPcbs,blockedPcbs,simProcessor);
			}					
		}
		
		public static void wakeUp(ArrayList<SimProcess> ready, ArrayList <SimProcess> blocked, 
				ArrayList<ProcessControlBlock>readyPcbs,ArrayList<ProcessControlBlock>blockedPcbs,SimProcessor s) {
			if (blocked.size()!=0) {				
				Random random = new Random();
				for(int x = 0; x < blocked.size(); x++) {
					if (random.nextDouble()<.30) {
						ready.add(blocked.get(x));
						readyPcbs.add(blockedPcbs.get(x));
						blocked.remove(x);
						blockedPcbs.remove(x);
					}					
				}
				System.out.println("Processor is idling");				
				//once processes were woken up, restore the next ready process
				if (!ready.isEmpty()) {
				RestoreProcess(ready, blocked, readyPcbs, blockedPcbs, s);
				}
			}
			//if blocked is also empty
			else {
				System.out.println("Processor finished executing all instructions");
			}
		}
	}

