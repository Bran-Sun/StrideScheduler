import java.util.ArrayList;

public class Main {
    static  int quantum = 2;
    static  int stride_whole = 6;
    public static void main(String[] args) {
        ArrayList<Proc> procs = new ArrayList<Proc>();
        procs.add(new Proc("A", 1, stride_whole, 0, 8, quantum));
        procs.add(new Proc("B", 2, stride_whole, 0, 8, quantum));
        procs.add(new Proc("C", 3, stride_whole, 0, 8, quantum));

        StrideScheduler scheduler = new StrideScheduler(stride_whole, quantum, procs);
        scheduler.start_run();

        for (Proc p: procs) {
            p.printResult();
        }

    }
}
