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
    private String property;
    private int numJobs;
    LinkedBlockingQueue<Job> inQueue;
    LinkedBlockingQueue<Job> outQueue;
    int scalingFactorOut = 1;
    int scalingFactorIn = 2;
    String propety;
    
    //MAIN CONSTRUCOR
    public MyScheduler(int numJobs, String property) {
        this.inQueue = new LinkedBlockingQueue<Job>(numJobs*scalingFactorIn);
        this.outQueue = new LinkedBlockingQueue<Job>(1);
        this.numJobs = numJobs;
        this.property = property;
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
    }
    
    //AVG WAIT - SJF(Shortest Job First) - Priority queue
    public void avgWait(LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out){
        Comparator<Job> c = (one,two)-> { 
            long oneLength = one.getLength();
            long twoLength = two.getLength();
            
            //compare lengths of jobs
            if (oneLength < twoLength){
                return -1;   //if return is negative, job one is shorter
            }
            if (oneLength == twoLength){
                return 0;  //if return 0, jobs are same length
            }
            return 1;   //if return is postive, job one is longer

            //return Long.compare(oneLength, twoLength);          
        };  
        
        //initialize priority queue with comparator c
        PriorityBlockingQueue<Job> priority = new PriorityBlockingQueue<>(numJobs, c);       

        // Take shortest job from inQueue then add to priority
        Thread inThread = new Thread(()->{
            int counter = 0;
            while (counter < numJobs){ // Stop when we reach the number of max jobs
                try{
                    //take job from inqueue
                    Job toPriority = in.take();
                    //put in priority queue, it should be sorted
                    priority.put(toPriority);
                    counter++;
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
                    out.put(fromPriority);                
                    otherCounter++;               
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        outThread.start();
    }   // */

    //DEAEDLINE - EDF(Earliest Deadline First) - Priority Queue
   public void deadline(int numJobs, LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out) {

                        // Try this with a Priority Blocking Queue 

        // Blocking Queue for jobs that won't make deadline
        BlockingQueue<Job> slowPokes = new LinkedBlockingQueue<Job>(numJobs);
        //determine wether or not job will make deadline
        Thread inThread = new Thread(()->{
            int counter = 0;
            while (counter < numJobs){ // Stop when we reach the number of max jobs
                try{
                    //take job from inqueue
                    Job steve = in.take();
                        //if job is gonna make the deadline, send to CPU
                    if (System.currentTimeMillis() + steve.getLength() <= steve.getDeadline()){ 
                        out.put(steve);
                        //System.out.println("job added to out queue");
                    } else {    //It's not gonna make the deadline, so send it to the queue of slowpokes
                        slowPokes.put(steve);
                        //System.out.println("job added to slow queue");
                    }
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  
            }
            
            while (!slowPokes.isEmpty()){ 
                try{  
                    out.put(slowPokes.take());         
                    // System.out.println("slow job added to Outqueue");                    
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
                //After all punctual jobs are completed, send these ones to the CPU
        });
        inThread.start();

        Thread outThread = new Thread(()->{
            //add to out
        });
        outThread.start();
    }

    //COMBINED - Fancy(Max wait + 2(Average Wait) ?????
    // Maybe?? SJF
    public void combined(int numJobs, LinkedBlockingQueue<Job> in, LinkedBlockingQueue<Job> out) {
    //    Comparator<Job> c = (one,two)-> { 
    //         long oneLength = one.getLength();
    //         long twoLength = two.getLength();  

    //         return Long.compare(oneLength, twoLength);
    //     };  
        
    //     //initialize priority queue with comparator c
    //     PriorityBlockingQueue<Job> priority = new PriorityBlockingQueue<>(numJobs, c);       
    //     // Take shortest job from inQueue then add to priority
    //     Thread inThread = new Thread(()->{
    //         int counter = 0;
    //         while (counter < numJobs){ // Stop when we reach the number of max jobs
    //             try{
    //                 //take job from inqueue
    //                 Job toPriority = in.take();
    //                 //put in priority queue, it should be sorted
    //                 priority.put(toPriority);
    //                 counter++;
    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             }  
    //         }     
    //     });
    //     inThread.start(); 

    //     // Take shortest job from priority and add to outQueue
    //     Thread outThread = new Thread(()->{
    //         int otherCounter = 0;
    //         //add to out
    //         while (otherCounter < numJobs){   // Keep adding jobs until you reach max Jobs 
    //             try{  
    //                 Job fromPriority = priority.take();
    //                 out.put(fromPriority);                
    //                 otherCounter++;               
    //             } catch(InterruptedException e){
    //                 e.printStackTrace();
    //             }
    //         }
    //     });
    //     outThread.start();
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
    }  
    
    public void run() {
        switch (property) {
            case "max wait":
                maxWait(numJobs, inQueue, outQueue);
                break;
            case "avg wait":
                avgWait(inQueue, outQueue);
            case "combined":
                combined(numJobs, inQueue, outQueue);
            case "deadlines":
                deadline(numJobs, inQueue, outQueue);
            default:
                break;
        }
    }
}