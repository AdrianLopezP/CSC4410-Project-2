/**
 * Models a thread-safe Blocking Queue.
 *
 * @author Alex Richardson
 * @author Adrian Lopez
 * @author Devin Rollins
 **/

import java.util.concurrent.*;
import java.util.*;
import java.util.Comparator;


public class MyScheduler {
    private String job;
    private int numJobs;
    LinkedBlockingQueue<Job> inQueue;
    LinkedBlockingQueue<Job> outQueue;
    int scalingFactorOut = 2;
    int scalingFactorIn = 2;
    
    //MAIN CONSTRUCOR
    public MyScheduler(int numJobs, String property) {
        this.inQueue = new LinkedBlockingQueue<Job>(numJobs*scalingFactorIn);
        this.outQueue = new LinkedBlockingQueue<Job>(numJobs*scalingFactorOut);
        this.numJobs = numJobs;
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
    public void maxWait(int numJobs, LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){
        BlockingQueue<Job> newBQueue = new LinkedBlockingQueue<Job>();
        Thread inThread = new Thread(()->{
            int counter = 0;
            // While there are jobs in the incoming queue add them to the new Blocking Queue
            while (counter < numJobs){ // Stop when we reach the number of max jobs
                try{
                    Job steve = in.take();  //Grab a job from the incoming queue and save it into the temp queue
                    newBQueue.put(steve);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  
                }     
        });
        inThread.start();

        Thread outThread = new Thread(()->{
            int otherCounter = 0;
            //add to out
            while (otherCounter < numJobs){   // Keep adding jobs until you reach max Jobs 
                try{
                    Job jobs = newBQueue.take();
                    out.put(jobs);     
                    otherCounter++;               
                } catch(InterruptedException e){
                    e.printStackTrace();
                }

            }
        });
        outThread.start();

        //send off to cpu
        // me understando. intelligent monkeys
    }
    
    //AVG WAIT - SJF(Shortest Job First) - Priority queue
    public void avgWait(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out, int numJobs){
        Comparator<Job> c = (one,two)-> {
            
            long oneLength = one.getLength();
            long twoLength = two.getLength();
            
            //compare lengths of jobs
            // if (oneLength < twolength){
            //     return -1;   //if return is negative, job one is shorter
            // }
            // if (oneLength == twolength){
            //     return 0;  //if return 0, jobs are same length
            // }
            // return 1;   //if return is postive, job one is longer

            return Long.compare(oneLength, twoLength);
                       
        };  
        
        PriorityBlockingQueue<Job> priority = new PriorityBlockingQueue<>(numJobs, c);       

        // Take shortest job from inQueue then add to priority
        Thread inThread = new Thread(()->{
            int counter = 0;
            while (counter < numJobs){ // Stop when we reach the number of max jobs
                try{
                    Job toPriority = in.take();
                    priority.put(toPriority);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  
            }     
        });
        inThread.start(); 

        // Take shortest job from priority and add to outQueue
        Thread outThread = new Thread(()->{
            int otherCounter = 0;
            //add to out
            while (otherCounter < numJobs){   // Keep adding jobs until you reach max Jobs 
                try{  
                    Job fromPriority = priority.take();
                    outQueue.put(fromPriority);
                    otherCounter++;               
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        outThread.start();
    }   // */

    //DEAEDLINE - EDF(Earliest Deadline First) - Priority Queue
   public void deadline(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out) {
    PriorityQueue<Job> priorityQueue = new PriorityQueue<>((a, b) -> Long.compare(a.getDeadline(), b.getDeadline()));
    while (!in.isEmpty() || !priorityQueue.isEmpty()) {
        try {
            if (!in.isEmpty()) {
                Job steve = in.take();
                priorityQueue.offer(steve);
            }
            if (!priorityQueue.isEmpty()) {
                Job steve = priorityQueue.poll();
                out.put(steve);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

    //COMBINED - Fancy(Max wait + 2(Average Wait) ?????
    public void combined(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out) {
        
    }



    
    public void run() {
        maxWait(numJobs, inQueue, outQueue);
        avgWait(inQueue, outQueue, numJobs);
        deadline(inQueue, outQueue);
        combined(inQueue, outQueue);

    }
}
