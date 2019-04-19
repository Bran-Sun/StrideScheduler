import java.awt.*;
import java.util.*;
import java.util.List;

public class StrideScheduler {
    public TreeSet<Proc> processes; //pass, process
    public Queue<Proc> init_process; //begin_time, process

    public int global_pass, last_update, global_tickets, stride_whole, global_stride;
    public int quantum, time, unfinished;
    Proc current_proc;

    public StrideScheduler(int stride_whole, int quantum, List<Proc> procs) {
        global_tickets = 0;
        time = 0;
        global_pass = 0;
        global_stride = 0;
        this.stride_whole = stride_whole;
        last_update = 0;
        this.quantum = quantum;

        init_process = new LinkedList<Proc>();
        for (Proc p: procs) {
            init_process.offer(p);
        }

        processes = new TreeSet<Proc>(new ProcComparator());
        unfinished = procs.size();
    }

    public void update_global_pass() {
        int elapse = time - last_update;
        global_pass =  elapse / quantum * global_stride;
        last_update = time;
    }

    public void update_global_tickets(int tickets) {
        global_tickets += tickets;
        if (global_tickets == 0) global_stride = 0;
        else global_stride = stride_whole / global_tickets;
    }

    public void load_new_process() {
        while (true) {
            Proc proc = init_process.peek();
            if (proc != null && proc.begin_time == time) {
                init_process.remove();

                update_global_pass();
                proc.ready_init(global_pass);
                processes.add(proc);

                update_global_tickets(proc.tickets);
            } else {
                break;
            }
        }
    }

    public void stop_cur_proc() {
        Proc proc = processes.pollFirst();
        update_global_pass();
        update_global_tickets(-current_proc.tickets);
        current_proc.finish(time);
        current_proc = null;
        unfinished--;
    }

    public void switch_out() {
        Proc out_proc= processes.pollFirst();
        //System.out.printf("switch out: %d\n", processes.size());
        out_proc.switch_out(time);
        processes.add(out_proc);
        //System.out.printf("switch out add: %d\n", processes.size());
        current_proc = null;
    }

    public  void switch_in() {
        if (processes.isEmpty()) return;
        current_proc = processes.first();
        current_proc.switch_in(time);
    }

    public void start_run() {
        time = -1;
        while (unfinished > 0) {
            ++time;
            print();

            load_new_process();

            if (current_proc != null) {
                if (current_proc.is_over(time)) {
                    stop_cur_proc();
                } else if (current_proc.is_slice_out(time)) {
                    switch_out();
                }
            }

            if (current_proc == null) {
                switch_in();
            }
        }
    }

    public void print() {
        System.out.printf("-----time: %d, size: %d\n", time, processes.size());
        if (current_proc != null) current_proc.printIntern();
    }

}
