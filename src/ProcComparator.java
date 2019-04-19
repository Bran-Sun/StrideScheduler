import java.util.Comparator;

class ProcComparator implements Comparator<Proc>
{

    @Override
    public int compare(Proc o1, Proc o2) {
        if (o1.pass == o2.pass) return o1.name.compareTo(o2.name);
        return o1.pass - o2.pass;
    }
}
