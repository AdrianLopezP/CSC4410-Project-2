/**
 * Models a thread-safe Blocking Queue.
 *
 * @author Alex Richardson
 * @author Adrian Lopez
 * @author Devin Rollins
 **/

import java.util.*;
import java.util.concurrent.*;

public class MyScheduler {
    private String job;
    LinkedBlockingQueue<Job> inQueue;
    LinkedBlockingQueue<Job> outQueue;
    int scalingFactorOut = 0;
    int scalingFactorIn = 0;
    
    //MAIN CONSTRUCOR
    public MyScheduler(int numJobs, String property) {
        this.inQueue = new LinkedBlockingQueue<Job>(numJobs*scalingFactorIn);

        this.outQueue = new LinkedBlockingQueue<Job>(numJobs*scalingFactorOut);
    }

    // Constructors
    public LinkedBlockingQueue<Job> getOutgoingQueue() {
        // Jobs being sent to the CPU
        //This is the queue where your code will put each Job that it has selected 
        //to run on the CPU in the order it has chosen
        return outQueue; 
    }

    public LinkedBlockingQueue<Job> getIncomingQueue() {
        // Jobs being sent from tester, to the scheduler
        return inQueue;
    }

    //MAX WAIT - FCFS(First Come First Serve) - Blocking Que
    public void maxWait(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){
        BlockingQueue<Job> newBQueue = new LinkedBlockingQueue<Job>();
        // While there are jobs in the incoming queue add them to the new Blocking Queue
        while (!in.isEmpty()){
            Job steve = in.remove();
            newBQueue.add(steve);
        }
        
        //add to out
        while (!newBQueue.isEmpty()){
            Job jobs = newBQueue.remove();
            out.add(jobs);
        }
        //send off to cpu
    }
    //AVG WAIT - SJF(Shortest Job First) - Priority queue
    public void avgWait(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){

    }

    //DEAEDLINE - EDF(Earliest Deadline First) - Priority Queue
    public void deadline(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){

    }

    //COMBINED - Fancy(Max wait + 2(Average Wait) ?????
     public void combined(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){

    }



    
    public void run(LinkedBlockingQueue<Job> inQueue, LinkedBlockingQueue<Job> outQueue) {
        maxWait(inQueue, outQueue);

    
    }
}