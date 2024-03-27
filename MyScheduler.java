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
    
    
    public MyScheduler(int numJobs, String property) {
        // who knows what dis do
        this.inQueue = new LinkedBlockingQueue<>(numJobs*scalingFactorIn);
        // yo no se what dis do これをする分かりません
        this.outQueue = new LinkedBlockingQueue<>(numJobs*scalingFactorOut);

        // Properties to check for: avg wait, max wait, combined(Max wait plus 2x avg wait), and deadlines(number of jobs met deadline)

        // To optimize average wait, use 
       
        // To optimize for deadlines, use EDF(Earliest Deadline First)
        
        // To optimize for Job Length, use SJF(Shortest Job First)

    }

    // Constructors
    public LinkedBlockingQueue<Job> getOutgoingQueue() {
        // Jobs being sent to the CPU
        //This is the queue where your code will put each Job that it has selected 
        //to run on the CPU in the order it has chosen
        return MyScheduler; 
        // 

    }
    public LinkedBlockingQueue<Job> getIncomingQueue() {
        // Jobs being sent from tester, to the scheduler
        return inQueue;

    }
    public void run() {
    
    }
}