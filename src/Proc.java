import java.util.Comparator;

//name 不能是一样
public class Proc {
    public int pass, tickets, stride;
    public int begin_time, quantum;
    public int last_time, run_time;
    public int response_time, wait_time, turnaround_time;
    public boolean first_start;
    public String name;

    public Proc(String name, int tickets, int stride_whole, int begin_time, int run_time, int quantum) {
        this.name = name;
        this.tickets = tickets;
        this.stride = stride_whole / tickets;
        this.pass = this.stride;
        this.quantum = quantum;

        this.begin_time = begin_time;

        this.run_time = run_time;
        this.last_time = 0;
        this.response_time = this.wait_time = this.turnaround_time = 0;
        this.first_start = true;
    }

    public void ready_init(int global_pass) {
        this.pass = this.stride + global_pass;
    }

    public void finish(int time) {
        turnaround_time = time - begin_time;
    }

    void switch_out(int time) {
        run_time -= quantum;
        last_time = time;
        pass += stride;
    }

    void switch_in(int time) {
        wait_time += (time - last_time);
        if (first_start) {
            response_time = time;
            first_start = false;
        }

        last_time = time;
    }

    public boolean is_over(int time) {
        return (run_time + last_time == time);
    }

    public boolean is_slice_out(int time) {
        return (time - last_time == quantum);
    }

    void printResult() {
        System.out.println("------print proc: " + name);
        System.out.printf("response time: %d\n", response_time);
        System.out.printf("wait time: %d\n", wait_time);
        System.out.printf("turnaround time: %d\n", turnaround_time);
    }

    void printIntern() {
        System.out.printf("name: %s, last time: %d, pass: %d\n", name, last_time, pass);
    }
}
